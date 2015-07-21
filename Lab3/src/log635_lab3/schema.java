package log635_lab3;

import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.List;

public class schema {
	int nbcouche = 0;
	int nbperceptron[];
	List<List<perceptron>> schema;
	
	
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
			List couche = new ArrayList<perceptron>();
			int j = nbperceptron[i]; //nbde perceptron pour la couche
			PipedWriter[] outpipe = new PipedWriter[j];
			
			if(i>1){ //on creer seulement pour la 1er couche, le reste est mapper
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

	//learn
	public boolean learn(){
		double sum = 0;
		boolean success = false;
		while(!success)
		{
			
		}
		return false;
	}
}
