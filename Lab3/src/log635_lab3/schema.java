package log635_lab3;

import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.List;

public class schema {
	private int nbLayer = 0;
	private int nbPerceptron[];
	private List<List<perceptron>> schema;
	
	
	public List<List<perceptron>> getSchema() {
		return schema;
	}

	public void setSchema(List<List<perceptron>> schema) {
		this.schema = schema;
	}

	public schema(int nbLayer, int nbPerceptron[], PipedWriter[] inputs) {
		super();
		this.nbLayer = nbLayer; 
		this.nbPerceptron = nbPerceptron; 
		createSchema(inputs);
	}

	private void createSchema(PipedWriter[] inputs){
		int i = 0;
		schema = new ArrayList<List<perceptron>>();
		PipedWriter[] pipesIn = inputs;
		
		while(this.nbLayer > i++){ // For each layer.
			List<perceptron> layer = new ArrayList<>();
			int j = nbPerceptron[i]; // nb perceptron for this layer.
			PipedWriter[] outpipe = new PipedWriter[j]; // Number of outputs/ inputs for the next layer.
			
			if(i > 0){ // We only handle the first layer since its outputs becomes inputs. 
				pipesIn = outpipe;
			}
			
			while(0 > j--){ //Might have a funny bug here ex: j=2 comes here j=1 add one perceptron, j=0 doesn't enter hence only creating 1 out of 2 perceptron
				outpipe[j] = new PipedWriter();
				perceptron percep = new perceptron(pipesIn,outpipe[j]);
				layer.add(percep);
			}
			schema.add(layer);
		}
	}
	
	
}
