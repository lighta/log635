package log635_lab3;

import java.io.PipedWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Main {
	
	private static double learningRate = 0.1;
	private static int nbLayer = 2;
	private static int[] perceptronCntByLayer = {2,1};
	private static int primitiveCnt = 12;
	private static double derivedNetworkError;
	private static double allowedError = 0.5; // not in percent value. but in actual value. 
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
			System.out.println(lb3fr.GetNormLearningSetDataRows(i));
		}
		
		System.out.println("#######################################");
		
		// Print the entire validation set.
		for (int i = 0; i < lb3fr.GetValidationSetSize(); i++)
		{
			System.out.println(lb3fr.GetNormValidationSetDataRows(i));
		}
		
		System.out.println("#######################################");
		
	
		// Create the neural network.
		PipedWriter[] inpipe = new PipedWriter[7];
		for (int i = 0; i < inpipe.length; i++)
			inpipe[i] = new PipedWriter();
		PipedWriter finalout = new PipedWriter();
		Schema neuralNetwork = new Schema(nbLayer, perceptronCntByLayer, inpipe, finalout);
		
		/*
		int[] nbper = {7,6,5};
		PipedWriter[] inpipe = new PipedWriter[7];
		schema sch = new schema(3, nbper, inpipe);
		learn(100000,30,sch);
		*/
	}
	
	//learn
	public static boolean learn(final Schema sch){
		derivedNetworkError = 100;
		int nbLayer=sch.getSchema().size();
		int nbPercept = 0;
		for(int i = 0;i < perceptronCntByLayer.length; i++ )
		{
			nbPercept += perceptronCntByLayer[i];
		}
		while(derivedNetworkError > allowedError)
		{
			if(derivedNetworkError == 100)
			{
				derivedNetworkError = 0;
			}
			sch.start();
			recursive(sch.getSchema().size(), sch.getSchema().get(nbLayer).size(), 0, sch, derivedNetworkError);
			derivedNetworkError /= nbPercept;
			Math.sqrt(derivedNetworkError);	
		}
		return false;
	}
	
	public static void recursive(int layer,  int percept, double permutation, Schema sch, double EQM)
	{
		int lastLayer = sch.getSchema().size();
		derivedNetworkError =+ sch.getSchema().get(layer).get(percept).getLocalError();
		if(layer == lastLayer)
		{
			double temp = sch.getSchema().get(layer).get(percept).modifyWeight(learningRate, 0, true);
			Schema.addPermutation(temp, layer);
			recursive(layer-1, sch.getSchema().get(layer-1).size(), 0, sch, derivedNetworkError);
		}
		else if(percept == 0)
		{
			if(layer == 0)
			{
				sch.getSchema().get(layer).get(percept).modifyWeight(learningRate, sch.getPermutation(layer + 1), false);
			}
			permutation += sch.getSchema().get(layer).get(percept).modifyWeight(learningRate, sch.getPermutation(layer + 1), false);
			Schema.addPermutation(permutation, layer);
			recursive(layer-1, sch.getSchema().get(layer-1).size(), 0, sch, derivedNetworkError);
		}
		else
		{
			permutation += sch.getSchema().get(layer).get(percept).modifyWeight(learningRate, sch.getPermutation(layer + 1), false);
			recursive(layer, percept - 1, permutation, sch, derivedNetworkError);
		}
	}
}

