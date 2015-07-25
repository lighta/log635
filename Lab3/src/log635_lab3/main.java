package log635_lab3;

import java.io.PipedWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class main {
	
	private double learningRate = 0.1;
	private static int nbLayer = 2;
	private static int[] perceptronCntByLayer = {2,1};
	private static int primitiveCnt = 12;
	private static int validationLineRate = 10; // Valid rate is calculated in a modulo so in short 1/var.
	
	public static void main(String[] args) {
		Lab3FileReader lb3fr = new Lab3FileReader("data/Donnees_sources.csv", primitiveCnt, validationLineRate);
		
		// Print headers.
		for (int i = 0; i < lb3fr.GetHeaders().size(); i++)
		{
			System.out.println(lb3fr.GetHeaders().elementAt(i));
		}
		
		System.out.println("#######################################");
	
		// Print the entire learning set.
		for (int i = 0; i < lb3fr.GetLearningSetSize(); i++)
		{
			System.out.println(lb3fr.GetLearningSetDataRows(i));
		}
		
		System.out.println("#######################################");
		
		// Print the entire validation set.
		for (int i = 0; i < lb3fr.GetValidationSetSize(); i++)
		{
			System.out.println(lb3fr.GetValidationSetDataRows(i));
		}
		
		System.out.println("#######################################");
		
	
		// Create the neural network.
		PipedWriter[] inpipe = new PipedWriter[7];
		for (int i = 0; i < inpipe.length; i++)
			inpipe[i] = new PipedWriter();
		PipedWriter finalout = new PipedWriter();
		schema neuralNetwork = new schema(nbLayer, perceptronCntByLayer, inpipe, finalout);
		
		/*
		int[] nbper = {7,6,5};
		PipedWriter[] inpipe = new PipedWriter[7];
		schema sch = new schema(3, nbper, inpipe);
		learn(100000,30,sch);
		*/
	}
	
	//learn
	public static boolean learn(final int maxtotaltry, final int maxtry, final schema sch){
		boolean success = false;
		int nbtry=0, nbtotaltry=0;
		int i=0,j=0;
		
		final int nbcouche=sch.getSchema().size();
		while(nbcouche > i++){
			final int nbpercepcouche=sch.getSchema().get(i).size();
			while(nbpercepcouche > j++){
				sch.getSchema().get(i).get(j).start(); //starting all perceptron
			}
		}
		while(!success && maxtotaltry > nbtotaltry++){
			while(maxtry > nbtry++)
			{
				//foreach readline learndata
					//push data into pipe input
					//check result per line (wait schema end)
					//if not success
						//update reseau (weight)
			}
			//foreach readline testdata`
				//test
				//if errorResult < epsilon
					//report and end
		}
		return false;
	}
}

