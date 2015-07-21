package log635_lab3;

import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.List;

public class schema {
	private int nbcouche = 0;
	private int nbperceptron[];
	private List<List<perceptron>> schema;
	
	
	public List<List<perceptron>> getSchema() {
		return schema;
	}

	public void setSchema(List<List<perceptron>> schema) {
		this.schema = schema;
	}

	public schema(int nbcouche, int nbperceptron[], PipedWriter[] inputs) {
		super();
		this.nbcouche = nbcouche; //nb couche
		this.nbperceptron = nbperceptron; //by couche
		
		createSchema(inputs);
		
	}

	private void createSchema(PipedWriter[] inputs){
		int i = 0;
		schema = new ArrayList<List<perceptron>>();
		
		PipedWriter[] pipesin = inputs;
		
		while(this.nbcouche > i++){ //pour toute les couche
			List<perceptron> couche = new ArrayList<>();
			int j = nbperceptron[i]; //nbde perceptron pour la couche
			PipedWriter[] outpipe = new PipedWriter[j];
			
			if(i>0){ //on creer seulement pour la 1er couche, le reste est mapper
				pipesin = outpipe;
			}
			
			while(0 > j--){
				outpipe[j] = new PipedWriter();
				perceptron percep = new perceptron(pipesin,outpipe[j],false);
				couche.add(percep);
			}
			schema.add(couche);
		}
	}
	
	
}
