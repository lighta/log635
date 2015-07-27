package log635_lab3;

import java.util.*;
import java.io.*;

public class Lab3FileReader {

	private Vector<String> vHeaders;
	private Vector<Vector<Double>> vLearningSet;
	private Vector<Vector<Double>> vValidationSet;
	private Vector<Vector<Double>> vNormLearningSet;
	private Vector<Vector<Double>> vNormValidationSet;
	
	public Lab3FileReader(String strFilePath, int learningLineVSValidLine) {	
		vHeaders = new Vector<String>();
		vLearningSet = new Vector<Vector<Double>>();
		vValidationSet = new Vector<Vector<Double>>();

		try {
			BufferedReader br;
			String oneLine;
			String[] splitString;
			double dblOneItem;
			int rowNumber = 0;
			
			// Read and store the headers.
			br = new BufferedReader(new FileReader(strFilePath));
		    if ((oneLine = br.readLine()) != null && oneLine.length() > 0) {
		    	splitString = oneLine.split(";");
		    	for (int i = 0; i < splitString.length; i++) {
		    		vHeaders.add(splitString[i]);
		    	}
		    }
		    br.close();
		    
		    // Second pass for the data.
		    br = new BufferedReader(new FileReader(strFilePath));
		    br.readLine(); // skip first line (Header line).
		    while ((oneLine = br.readLine()) != null && oneLine.length() > 0) {
		    	splitString = oneLine.split(";");
		    	Vector<Double> vline = new Vector<Double>();
		    	
		    	// Fill the temporary line with data.
		    	for (int i = 0; i < splitString.length; i++)
		    	{
		    		dblOneItem = Double.parseDouble(splitString[i]);
		    		vline.add(dblOneItem);
		    	}
		    	// Add the line to the correct set.
		    	if (rowNumber % learningLineVSValidLine == 0) {
		    		vValidationSet.add(vline);
		    	}	else {
		    		vLearningSet.add(vline);
		    	}
		    	
		    	rowNumber++;
		    }
		    br.close();
		    vNormValidationSet = Normalizer.scale(vValidationSet, 1, 5, 6);
			vNormLearningSet = Normalizer.scale(vLearningSet, 1, 5, 6);
		}
		catch (Exception ex)
		{
			System.err.println("ERROR: something bad happened. It's probably your fault.");
			System.err.println(ex.getMessage());
		}

	}
	
	
	public Vector<String> GetHeaders() {
		return vHeaders;
	}
	
	public Vector<Double> GetLearningSetDataRows(int i) {
		return vLearningSet.elementAt(i);
	}
	
	public Vector<Double> GetValidationSetDataRows(int i) {
		return vValidationSet.elementAt(i);
	}
	
	public Vector<Double> GetNormLearningSetDataRows(int i) {
		return vNormLearningSet.elementAt(i);
	}
	
	public Vector<Double> GetNormValidationSetDataRows(int i) {
		return vNormValidationSet.elementAt(i);
	}

	public int GetLearningSetSize() {
		return vLearningSet.size();
	}
	
	public int GetValidationSetSize() {
		return vValidationSet.size();
	}
	
	
	public void printAll(){
		this.printHeader();
		System.out.println("#######################################");
		this.printLearningSet();
		System.out.println("#######################################");
		this.printValidationSet();
		System.out.println("#######################################");
	}
	
	public void printHeader(){
		// Print headers.
		for (int i = 0; i < this.GetHeaders().size(); i++)
		{
			System.out.println(this.GetHeaders().elementAt(i));
		}
	}
	
	public void printLearningSet(){
		// Print the entire learning set.
		for (int i = 0; i < this.GetLearningSetSize(); i++)
		{
			System.out.println(this.GetNormLearningSetDataRows(i));
		}
	}

	public void printValidationSet(){
		// Print the entire validation set.
		for (int i = 0; i < this.GetValidationSetSize(); i++)
		{
			System.out.println(this.GetNormValidationSetDataRows(i));
		}
	}
}
