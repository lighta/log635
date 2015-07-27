package log635_lab3;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Vector;

public class Network {
	public static int DEF_VALIDATION_RATE = 10; // Valid rate is calculated in a modulo so in short 1/var.
	public static double DEF_ALLOWED_ER = 0.5; 	// not in percent value. but in actual value. 
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
	private void pushData(final Vector<Double> data){
		for(int k=0; k<data.size(); k++){
			try {
				inpipe[k].write(data.get(k)+"\n");
				inpipe[k].flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//learn
	public boolean learn(final Lab3FileReader lb3fr){
		
		
		derivedNetworkError = 100;
		int nbLayer=sch.getSchema().size();
		int nbPercept = 0;
		for(int i = 0;i < perceptronCntByLayer.length; i++ ){
			nbPercept += perceptronCntByLayer[i];
		}
		sch.start();
		
		while(derivedNetworkError > allowedError)
		{
			if(derivedNetworkError == 100){
				derivedNetworkError = 0;
			}
			//for all data to learn
			for(int i=0; i<lb3fr.GetLearningSetSize(); i++){
				final Vector<Double> data = lb3fr.GetLearningSetDataRows(i);
				pushData(data);
				final double res = Utils.readOneSortie(infinal);
				//
				
				recursive(sch.getSchema().size(), sch.getSchema().get(nbLayer).size(), 0, derivedNetworkError);
				derivedNetworkError /= nbPercept;
				Math.sqrt(derivedNetworkError);	
			}
			
			
		}
		return false;
	}
	
	private void recursive(final int layer,final int percept, double permutation, final double EQM)
	{
		final int lastLayer = sch.getSchema().size(); //this is actually nbLayer
		derivedNetworkError =+ sch.getSchema().get(layer).get(percept).getLocalError();
		if(layer == lastLayer)
		{
			double temp = sch.getSchema().get(layer).get(percept).modifyWeight(learningRate, 0, true);
			sch.addPermutation(temp, layer);
			recursive(layer-1, sch.getSchema().get(layer-1).size(), 0, derivedNetworkError);
		}
		else if(percept == 0)
		{
			if(layer == 0)
			{
				sch.getSchema().get(layer).get(percept).modifyWeight(learningRate, sch.getPermutation(layer + 1), false);
			}
			permutation += sch.getSchema().get(layer).get(percept).modifyWeight(learningRate, sch.getPermutation(layer + 1), false);
			sch.addPermutation(permutation, layer);
			recursive(layer-1, sch.getSchema().get(layer-1).size(), 0, derivedNetworkError);
		}
		else
		{
			permutation += sch.getSchema().get(layer).get(percept).modifyWeight(learningRate, sch.getPermutation(layer + 1), false);
			recursive(layer, percept - 1, permutation, derivedNetworkError);
		}
	}
}
