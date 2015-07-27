package log635_lab3;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Vector;

public class Network {
	public static int DEF_VALIDATION_RATE = 10; // Valid rate is calculated in a modulo so in short 1/var.
	public static double DEF_ALLOWED_ER = 5; 	// not in percent value. but in actual value. 
	public static int DEF_PRIMITIVE_CNT = 11;	// 11 + 1(quality)
	public static int[] DEF_PERCNT_BY_LAYER = {11,6,3,1};
	public static int DEF_NB_LAYER = 4;
	public static double DEF_LEARNING_RATE = 0.1;
	
	private double learningRate;
	private int nbLayer;
	private int[] perceptronCntByLayer;
	private int primitiveCnt;
	private double allowedError; 
	public int validationLineRate; 
	
	private double derivedNetworkError;
	private Schema sch;
	private PipedWriter[] inpipe; //inpipe of all schema
	private PipedWriter finalout; //outpipe of all the schema
	private PipedReader infinal; //outpipe of all the schema
	
	public Network() {
		this.learningRate = DEF_LEARNING_RATE;
		this.nbLayer = DEF_NB_LAYER;
		this.perceptronCntByLayer = DEF_PERCNT_BY_LAYER;
		this.primitiveCnt = DEF_PRIMITIVE_CNT;
		this.allowedError = DEF_ALLOWED_ER;
		this.validationLineRate = DEF_VALIDATION_RATE;
		createNetwork();
	}
	
	public Network(double learningRate, int nbLayer,
			int[] perceptronCntByLayer, int primitiveCnt
			, double allowedError) {
		super();
		this.learningRate = learningRate;
		this.nbLayer = nbLayer;
		this.perceptronCntByLayer = perceptronCntByLayer;
		this.primitiveCnt = primitiveCnt;		
		this.allowedError = allowedError;
		createNetwork();
	}

	/// Accessors
	
	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(final double learningRate) {
		this.learningRate = learningRate;
	}

	public int getValidationLineRate() {
		return validationLineRate;
	}

	public void setValidationLineRate(final int validationLineRate) {
		this.validationLineRate = validationLineRate;
	}
	
	/// end Accessor

	/**
	 * Function to create dyn object associated with the Network
	 * (creating pipe and schema)
	 */
	private void createNetwork(){
		// Create the neural network.
		this.derivedNetworkError = 0;
		inpipe = new PipedWriter[this.primitiveCnt];
		for (int i = 0; i < inpipe.length; i++)
			inpipe[i] = new PipedWriter();
		finalout = new PipedWriter();
		
		sch = new Schema(nbLayer, perceptronCntByLayer, inpipe, finalout);
		
		infinal = new PipedReader();
		try {
			infinal.connect(finalout);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Function to push some data into schema
	 * @param data : data to push into
	 */
	private void pushData(final Vector<Double> data, boolean all){
		int sz = data.size();
		if(!all)
			sz-=1; //remove last data to be pushed (for learning)
		
		for(int k=0; k < sz; k++){ 
			try {
				inpipe[k].write(data.get(k)+"\n");
				inpipe[k].flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Function to start learning the data.
	 * @param lb3fr : data to learn
	 * @param maxTry : numnber of max loop authorized
	 * @return : wheter Epsilon was respected
	 */
	public boolean learn(final Lab3FileReader lb3fr, final int maxTry){
		
		derivedNetworkError = 100;
		int nbLayer=sch.getSchema().size();
		int nbPercept = sch.getTotalPercept();
		int nbtry = 0;								//to avoid infinite loop
		sch.start();
		
		while(derivedNetworkError > allowedError && maxTry>nbtry)
		{
			//for all data to learn
			for(int i=0; i<lb3fr.GetLearningSetSize(); i++){
				final Vector<Double> data = lb3fr.GetLearningSetDataRows(i);
				//System.out.println("data="+data);
				pushData(data,false);
				final double res = Utils.readOneSortie(infinal); //waiting schema as finish to compute with that value
				final double expected = data.lastElement();
				retroPropagation(sch.getSchema().size()-1, sch.getSchema().get(nbLayer-1).size()-1, 0,expected);
				
				//Erreur moyenne quadratique emq = √[(e1² + e2² + … +en²) / n] ;
				derivedNetworkError /= nbPercept;
				derivedNetworkError = Math.sqrt(derivedNetworkError);	

				System.out.println("res="+res+" expected="+expected+" derivedNetworkError="+derivedNetworkError);
			}
			nbtry++;
			derivedNetworkError %= 100; //back in percent
			System.out.println("derivedNetworkError="+derivedNetworkError+" allowedError="+allowedError+" nbtry="+nbtry );
		}
		return (derivedNetworkError > allowedError);
	}
	
	// fonction gradient (eq 18)
	private void retroPropagation(final int layer,final int percept, double permutation,final double expected)
	{
		final int nbLayer = sch.getSchema().size();
		
		//maj EQM (si on passe 2fous dans le meme percept fuck)
		final double e = sch.getSchema().get(layer).get(percept).getLocalError(expected);
		if(e!=0) //avoid flush
			derivedNetworkError = e*e;
		
		if(layer == nbLayer-1){
			double temp = sch.getSchema().get(layer).get(percept).modifyWeight(learningRate, 0, true);
			sch.addPermutation(layer, temp);  
			retroPropagation(layer-1, sch.getSchema().get(layer-1).size()-1, 0, 0); //descent couche inf
		}
		else if(percept == 0){ //dernier perceptron du layer
			if(layer == 0){ //derniere couche
				sch.getSchema().get(layer).get(percept).modifyWeight(learningRate, sch.getPermutation(layer + 1), false);
			}
			else {
				permutation += sch.getSchema().get(layer).get(percept).modifyWeight(learningRate, sch.getPermutation(layer + 1), false);
				sch.addPermutation(layer, permutation);
				retroPropagation(layer-1, sch.getSchema().get(layer-1).size()-1, 0, 0); //descent couche inf
			}
		}
		else { //on itere dans le layer
			permutation += sch.getSchema().get(layer).get(percept).modifyWeight(learningRate, sch.getPermutation(layer + 1), false);
			retroPropagation(layer, percept - 1, permutation, 0); //descent next percepts
		}
	}

	public void report() {
		sch.displaySchema();
	}

}
