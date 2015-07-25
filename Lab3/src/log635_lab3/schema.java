package log635_lab3;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.List;

public class schema {
	private int nbLayer = 0;
	private int nbPerceptron[];
	private List<List<perceptron>> schema;
	private boolean started = false;
	private PipedWriter finalout;
	
	
	public List<List<perceptron>> getSchema() {
		return schema;
	}

	public void setSchema(List<List<perceptron>> schema) {
		this.schema = schema;
	}

	public schema(int nbLayer, int nbPerceptron[], PipedWriter[] inputs,PipedWriter finalout) {
		super();
		this.nbLayer = nbLayer; 
		this.nbPerceptron = nbPerceptron;
		this.finalout = finalout;
		createSchema(inputs);
	}

	
	/**
	 * Multiplexer, 
	 * DO NOT CALL IT unless you know what u doing
	 * @param pw
	 * @param dup
	 * @throws IOException
	 */
	private void createPropagators(PipedWriter pw[],PipedWriter[][] dup) throws IOException {		
		int i=0;
		for(PipedWriter cur_pw : pw){
			Propagator pg = new Propagator(cur_pw,dup[i]);
			pg.start();
			i++;
		}
	}

	
	/**
	 * Create a schema configuration with the perceptron
	 * This function ain't optimized but should be called that much so it's ok
	 * @param inputs
	 */
	private void createSchema(PipedWriter[] inputs){
		int GUIcounter = 0;
		schema = new ArrayList<List<perceptron>>();
		PipedWriter[] pipesIn = inputs;
		
		PipedWriter[][] LayerOutpipes = new PipedWriter[this.nbLayer][];
		
		for(int i=0; i < this.nbLayer; i++){ 
			List<perceptron> layer = new ArrayList<>();
			final int sz = nbPerceptron[i]; // nb perceptron for this layer.
			LayerOutpipes[i] = new PipedWriter[sz]; // Number of outputs/ inputs for the next layer.
			
			if(i > 0){ // We only handle the first layer since its outputs becomes inputs. 
				pipesIn = LayerOutpipes[i-1];
			}
			
			PipedWriter[][] dup = null;
			if(sz > 0){ //create Multiplexing for pipes
				dup = new PipedWriter[pipesIn.length][sz];
				for(int l=0; l < pipesIn.length; l++){
					for(int k=0; k < sz; k++){
						dup[l][k] = new PipedWriter();
					}
				}
				try {
					createPropagators(pipesIn,dup);
				} catch (IOException e1) {
					System.err.println("Fail to create Prepagators DEBUG ME");
					//e1.printStackTrace();
				}
			}
			
			for(int j=0; j < sz; j++) { //iterate foreach perceptron
				PipedWriter[] pipesInJ;
				if(sz > 0)
					pipesInJ = dup[j];	//separate array for each perceptron
				else
					pipesInJ = pipesIn;
				
				if(sz==1) //last one
					LayerOutpipes[i][j] = finalout;
				else
					LayerOutpipes[i][j] = new PipedWriter();
				
				try {
					perceptron percep = new perceptron(GUIcounter,pipesInJ,LayerOutpipes[i][j]);
					GUIcounter++;
					layer.add(percep);
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

	
	private static void test_sch1(){
		System.out.println("Testing schema 1");
		int nbcouche = 1;
		int nbPerceptron[] = {1};
		PipedWriter[] inputPipes = new PipedWriter[2]; //x1 et x2
		inputPipes[0] = new PipedWriter();
		inputPipes[1] = new PipedWriter();
		
		PipedWriter finalout = new PipedWriter();
		PipedReader readfinal = new PipedReader();
		
		schema sch = new schema(nbcouche,nbPerceptron,inputPipes,finalout);
		sch.start();
		
		try {
			readfinal.connect(finalout);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		utils.test_shoutData(inputPipes);
		List<Double> sch_out = utils.readSortie(readfinal);
		System.out.println("Sortie sch1="+sch_out);
		sch.waitFinished(); //ensure all is done before quitting test
	}
	
	private static void test_sch2(){
		System.out.println("Testing schema 2");
		int nbcouche = 2;
		int nbPerceptron[] = {2,1};
		PipedWriter[] inputPipes = new PipedWriter[2]; //x1 et x2
		inputPipes[0] = new PipedWriter();
		inputPipes[1] = new PipedWriter();
		
		PipedWriter finalout = new PipedWriter();
		PipedReader readfinal = new PipedReader();
		
		schema sch = new schema(nbcouche,nbPerceptron,inputPipes,finalout);
		sch.start();
		
		try {
			readfinal.connect(finalout);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<Double> sch_out = utils.readSortie(readfinal);
		System.out.println("Sortie sch2="+sch_out);
		utils.test_shoutData(inputPipes);
		sch.waitFinished(); //ensure all is done before quitting test
	}
	
	public static void main(String[] args) {
		System.out.println("Schema main, quicktest");
		//test_sch1();
		test_sch2();
	}
}
