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
	private boolean finished = false;
	
	
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
	 * Create a schema configuration with the perceptron
	 * This function ain't optimized but should be called that much so it's ok
	 * @param inputs
	 */
	private void createSchema(PipedWriter[] inputs){
		schema = new ArrayList<List<perceptron>>();
		PipedWriter[] pipesIn = inputs;
		
		for(int i=0; i < this.nbLayer; i++){ 
			List<perceptron> layer = new ArrayList<>();
			final int sz = nbPerceptron[i]; // nb perceptron for this layer.
			PipedWriter[] outpipe = new PipedWriter[sz]; // Number of outputs/ inputs for the next layer.
			
			if(i > 0){ // We only handle the first layer since its outputs becomes inputs. 
				pipesIn = outpipe;
			}
			
			for(int j=0; j < sz; j++)	//iterate foreach perceptron
			{ 
				PipedWriter[] pipesInJ;
				if(i > 0 && j >0)
					pipesInJ = pipesIn.clone();	//separate array for each perceptron
				else
					pipesInJ = pipesIn;
				
				if(sz==1) //last one
					outpipe[j] = finalout;
				else
					outpipe[j] = new PipedWriter();
				
				try {
					perceptron percep = new perceptron(pipesInJ,outpipe[j]);
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
	 * @return
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
	
	
	
	private static void shout_data(PipedWriter[] inputPipes){
		//simulate shout data
		try {
			inputPipes[0].write("0\n1\n0\n1\n"); //0101
			inputPipes[1].write("0\n0\n1\n1\n"); //0011
			
			inputPipes[0].close();	
			inputPipes[1].close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //0101
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
		shout_data(inputPipes);
		sch.waitFinished();
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
		shout_data(inputPipes);

	}
	
	public static void main(String[] args) {
		System.out.println("Schema main, quicktest");
		test_sch1();
		test_sch2();
	}
}
