package log635_lab3;

import java.util.ArrayList;
import java.util.List;

public class schema {
	int nbcouche = 0;
	int nbperceptron[];
	List<List<perceptron>> schema;
	
	
	public schema(int nbcouche, int nbperceptron[], double[] inputs) {
		super();
		this.nbcouche = nbcouche; //nb couche
		this.nbperceptron = nbperceptron; //by couche
		
		createSchema(inputs);
		
	}

	private void createSchema(double[] inputs){
		int i = this.nbcouche;
		schema = new ArrayList<List<perceptron>>();
		while(0 > i--){
			List couche = new ArrayList<perceptron>();
			int j = nbperceptron[i];
			while(0 > j--){
				//public perceptron(double[] inputs, boolean isSigmoid)
				perceptron percep = new perceptron(inputs,false);
				couche.add(percep);
			}
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
