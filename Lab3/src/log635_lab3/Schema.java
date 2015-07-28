package log635_lab3;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.List;

public class Schema {
	private int nbLayer = 0;
	private int nbPerceptron[];
	private double permutation[];
	private List<List<Perceptron>> schema;
	private List<Propagator> proglist;
	private boolean started = false;
	private PipedWriter finalout;
	private int GUIcounter = 0;
	
	private Perceptron last;
	
	public Perceptron getLast() {
		return last;
	}

	/**
	 * Somme des truc retain (pour calcul des weught par retropropagation)
	 * (Crazy Yummi)
	 * @param layer : couche initial
	 * @return sum de couche initial to end
	 */
	public double getPermutation(int layer) {
		double sum = 0;
		for(int i = 0; i < layer; i++){ //iterate sur les couche superieur
			sum += permutation[i];
		}
		return sum;
	}
	
	public void addPermutation(int layer, double value) {
		permutation[layer] += value;
	}
	
	public List<List<Perceptron>> getSchema() {
		return schema;
	}


	/**
	 * Constructor
	 * @param nbPerceptron : number of perceptron by layer (tab)
	 * @param inputs : pipes of entries for schema (x1..xn)
	 * @param finalout : off pipe of schema 
	 */
	public Schema(int nbPerceptron[], PipedWriter[] inputs,PipedWriter finalout) {
		super();
		this.nbLayer = nbPerceptron.length; 
		this.permutation = new double[nbLayer];
		proglist = new ArrayList<Propagator>();
		for(int i = 0; i < nbLayer; i++)
		{
			permutation[i] = 0;
		}
		proglist = new ArrayList<>();
		this.nbPerceptron = nbPerceptron;
		this.finalout = finalout;
		createSchema(inputs);
	}
	
	public void LoadSchema(double[][][] wnex){	
		final int nblayer = wnex.length; //nb layer
		for(int i = 0; i < nblayer; i++){
			final int nbperceptbylayer = wnex[i].length; //nb nbperceptbylayer
			for(int j = 0; j < nbperceptbylayer; i++){
				schema.get(i).get(j).setInputWeights(wnex[i][j]); //update tous les poids
			}	
		}
	}

	
	/**
	 * Multiplexer, 
	 * DO NOT CALL IT unless you know what u doing
	 * @param pw
	 * @param dup
	 * @throws IOException
	 */
	private PipedWriter[][] createPropagators(final int sz, final int n, PipedWriter[][] LayerINpipes) {				
		PipedWriter[][] dup = new PipedWriter[LayerINpipes[n].length][sz];	//internal
		PipedWriter[][] indup = new PipedWriter[sz][LayerINpipes[n].length]; //needed for schema
		for(int l=0; l < LayerINpipes[n].length; l++){	//chaque input a un propagator si sz >1
			for(int k=0; k < sz; k++){
				dup[l][k] = new PipedWriter();
				indup[k][l] = dup[l][k];
			}
			try {
				Propagator pg = new Propagator(GUIcounter++,LayerINpipes[n][l],dup[l]);
				proglist.add(pg);
				pg.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.err.println("Fail to create Prepagators DEBUG ME");
			}
		}
		return indup;
	}

	
	/**
	 * Create a schema configuration with the perceptron
	 * This function ain't optimized but should be called that much so it's ok
	 * @param inputs
	 */
	private void createSchema(PipedWriter[] inputs){
		schema = new ArrayList<List<Perceptron>>();
		//PipedWriter[] pipesIn = inputs;
		
		PipedWriter[][] LayerOutpipes = new PipedWriter[this.nbLayer][];
		PipedWriter[][] LayerINpipes = new PipedWriter[this.nbLayer][];
		LayerINpipes[0] = inputs;
		
		for(int i=0; i < this.nbLayer; i++){ 
			List<Perceptron> layer = new ArrayList<>();
			final int sz = nbPerceptron[i]; // nb perceptron for this layer.
			LayerOutpipes[i] = new PipedWriter[sz]; // Number of outputs/ inputs for the next layer.
			
			if(i > 0){ // We only handle the first layer since its outputs becomes inputs. 
				LayerINpipes[i] = LayerOutpipes[i-1];
			}
			
			PipedWriter[][] indup = null;
			if(sz > 1){ //create Multiplexing for pipes
				indup = createPropagators(sz,i,LayerINpipes); 
			}
	
			
			for(int j=0; j < sz; j++) { //iterate foreach perceptron
				PipedWriter[] pipesInJ;
				if(sz > 1)
					pipesInJ = indup[j];	//separate array for each perceptron
				else
					pipesInJ = LayerINpipes[i];
				
				if(sz==1) //last one
					LayerOutpipes[i][j] = finalout;
				else
					LayerOutpipes[i][j] = new PipedWriter();
				
				try {
					Perceptron percep = new Perceptron(GUIcounter++,pipesInJ,LayerOutpipes[i][j]);
					layer.add(percep);
					if(sz == 1)
						last = percep;
				} catch (Exception e) {
					System.err.println("Fail to create perceptron DEBUG ME");
					e.printStackTrace();
				}
			}
			schema.add(layer);
		}
	}
	
	/**
	 * Simple function to start all perceptron on schema
	 */
	public void start(){
		if(started == false){
			started = true;
			int i=0;
			while(nbLayer > i){
				final int nbpercepcouche=nbPerceptron[i];
				int j=0;
				while(nbpercepcouche > j){
					schema.get(i).get(j).start(); //starting all perceptron
					j++;
				}
				i++;
			}
		}
	}
	
	/**
	 * Asking all perceptron to stop themself
	 */
	public void stop(){
		if(started == true){
			started = false;
			
			for(Propagator cur_prog : proglist)
				cur_prog.interrupt();
			
			int i=0;
			while(nbLayer > i){
				final int nbpercepcouche=nbPerceptron[i];
				int j=0;
				while(nbpercepcouche > j){
					schema.get(i).get(j).interrupt(); //stopping all perceptron
					j++;
				}
				i++;
			}
		}
	}
	
	/**
	 * Blocking function to wait till all perceptrons of schema 
	 * as finished to compute
	 */
	public void waitFinished(){
		int i=0;
		while(nbLayer > i){
			final int nbpercepcouche=nbPerceptron[i];
			int j=0;
			while(nbpercepcouche > j){
				if(schema.get(i).get(j).isAlive()){
					synchronized(schema.get(i).get(j)){
						try {
							schema.get(i).get(j).wait(); //waiting all perceptron
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					}
				}
				j++;
			}
			i++;
		}
	}
	
	/**
	 * Function to know how many percept we have in our schema
	 * @return
	 */
	public int getTotalPercept(){
		int nbPercept=0;
		for(int i = 0;i < nbPerceptron.length; i++ ){
			nbPercept += nbPerceptron[i];
		}
		return nbPercept;
	}
	
	/**
	 * Fonciton to display the whole schema configuration
	 */
	public void displaySchema(){
		int i=0;
		StringBuilder _sb = new StringBuilder("Schema configuration: nbLayer="+nbLayer+"\n");
		while(nbLayer > i){
			final int nbpercepcouche=nbPerceptron[i];
			int j=0;
			_sb.append("Layer"+i+" nbpercept="+nbpercepcouche+"\n");
			while(nbpercepcouche > j){
				Perceptron p = schema.get(i).get(j);
				_sb.append("\t perceptron="+j+" p="+p+"\n");
				j++;
			}
			i++;
		}
		System.out.println(_sb.toString());
	}
	
	/**
	 * Function to try out our schema
	 * Build a schema with given parameter
	 * Push data into schema to see how they propagate
	 * @param id : test id
	 * @param nbcouche : number of layer
	 * @param nbPerceptron : number of perceptron by layer
	 */
	private static void test_sch1(final int id, final int nbPerceptron[]){
		System.out.println("Testing schema "+id);
		
		PipedWriter[] inputPipes = new PipedWriter[2]; //x1 et x2
		inputPipes[0] = new PipedWriter();
		inputPipes[1] = new PipedWriter();
		
		PipedWriter finalout = new PipedWriter();
		PipedReader readfinal = new PipedReader();
		
		Schema sch = new Schema(nbPerceptron,inputPipes,finalout);
		sch.displaySchema();
		sch.start();
		
		try {
			readfinal.connect(finalout);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Utils.test_shoutData(inputPipes);
		List<Double> sch_out = Utils.readSortie(readfinal);
		System.out.println("Sortie sch1="+sch_out);
		sch.waitFinished(); //ensure all is done before quitting test
		
		FileWriter.write(sch);
	}
	
	/**
	 * Small main to try out our schema
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Schema main, quicktest");
		int nbPerceptron1[] = {1};
		test_sch1(1, nbPerceptron1);
		
		System.out.println("\n\n");
		int nbPerceptron2[] = {2,1};
		test_sch1(2, nbPerceptron2 );
		
		System.out.println("\n\n");
		int nbPerceptron3[] = {4,2,1};
		test_sch1(3, nbPerceptron3 );
	}
}
