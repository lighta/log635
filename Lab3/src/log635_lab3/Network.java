package log635_lab3;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Network {
	public static int DEF_VALIDATION_RATE = 10; // Valid rate is calculated in a modulo so in short 1/var.
	public static double DEF_ALLOWED_ER = 5; 	// not in percent value. but in actual value. 
	public static int DEF_PRIMITIVE_CNT = 11;	// 11 + 1(quality)
	public static int[] DEF_PERCNT_BY_LAYER = {11,3,1};
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
		this.perceptronCntByLayer = DEF_PERCNT_BY_LAYER;
		this.primitiveCnt = DEF_PRIMITIVE_CNT;
		this.allowedError = DEF_ALLOWED_ER;
		this.validationLineRate = DEF_VALIDATION_RATE;
		
		this.nbLayer = perceptronCntByLayer.length;
		createNetwork();
	}
	
	public Network(double learningRate,
			int[] perceptronCntByLayer, int primitiveCnt
			, double allowedError) {
		super();
		this.learningRate = learningRate;
		this.perceptronCntByLayer = perceptronCntByLayer;
		this.nbLayer = perceptronCntByLayer.length;
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
		
		sch = new Schema(perceptronCntByLayer, inpipe, finalout);
		
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
	
	public double calc_EQM(List<Double> le){
		double EQM = 0;
		for(double cur_le : le){
			EQM = cur_le*cur_le;
		}
		EQM /= le.size();
		EQM = Math.sqrt(EQM);
		return EQM;
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
		
		while(derivedNetworkError > allowedError && maxTry>nbtry) //EQM > Epsilon
		{
			//for all data to learn
			for(int i=0; i<lb3fr.GetLearningSetSize(); i++){
				final Vector<Double> data = lb3fr.GetNormLearningSetDataRows(i);
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
			derivedNetworkError %= 100; //back in percent

			
			nbtry++;
			
			if((nbtry % validationLineRate)==0){ //chaque x validationaRate on effectue un test de validation
				//for all validation
				List<Double> expecteds = new ArrayList<>();
				for(int i=0; i<lb3fr.GetLearningSetSize(); i++){
					final Vector<Double> testdata = lb3fr.GetLearningSetDataRows(i);
					pushData(testdata,false);
					expecteds.add(testdata.lastElement());
				}
				List<Double> res = Utils.readSortie(infinal);
				
				List<Double> le = new ArrayList<>();
				int i=0;
				for(double cur_ex : expecteds){
					double erl = Math.abs(res.get(i) - cur_ex);
					le.add(erl);
				}
				double EQM_test = calc_EQM(le);
				System.out.println("EQM_test="+EQM_test);
			}
			
			System.out.println("derivedNetworkError="+derivedNetworkError+" allowedError="+allowedError+" nbtry="+nbtry );
		}
		sch.stop();
		return (derivedNetworkError < allowedError);
	}
	
	// fonction gradient (eq 18)
	private void retroPropagation(final int layer,final int percept, double permutation,final double expected)
	{	
		final List<List<Perceptron>> tmp_sch = sch.getSchema(); //avoid multiple dereference
		//maj EQM (si on passe 2fous dans le meme percept fuck)
		final double e = tmp_sch.get(layer).get(percept).getLocalError(expected);
		if(e!=0) //avoid flush
			derivedNetworkError = e*e;
		
		if(layer == nbLayer-1){
			double temp = tmp_sch.get(layer).get(percept).modifyWeight(learningRate, 0, true);
			sch.addPermutation(layer, temp);  
			retroPropagation(layer-1, tmp_sch.get(layer-1).size()-1, 0, 0); //descent couche inf
		}
		else if(percept == 0){ //dernier perceptron du layer
			if(layer == 0){ //derniere couche
				tmp_sch.get(layer).get(percept).modifyWeight(learningRate, sch.getPermutation(layer + 1), false);
			}
			else {
				permutation += tmp_sch.get(layer).get(percept).modifyWeight(learningRate, sch.getPermutation(layer + 1), false);
				sch.addPermutation(layer, permutation);
				retroPropagation(layer-1, tmp_sch.get(layer-1).size()-1, 0, 0); //descent couche inf
			}
		}
		else { //on itere dans le layer
			permutation += tmp_sch.get(layer).get(percept).modifyWeight(learningRate, sch.getPermutation(layer + 1), false);
			retroPropagation(layer, percept - 1, permutation, 0); //descent next percepts
		}
	}

	public void report() {
		sch.displaySchema();
	}

}
