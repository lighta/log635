package log635_lab3;

import java.util.*;
import java.io.*;

public class Lab3FileReader {

	private Vector<String> vHeaders;
	Vector<Vector<Double>> vDataColumns;
	Vector<Vector<Double>> vDataColumnsEvenNumberedRows;
	Vector<Vector<Double>> vDataColumnsOddNumberedRows;
	
	Vector<Vector<Double>> vDataLineEvenNumberedRows;
	Vector<Vector<Double>> vDataLineOddNumberedRows;
	
	
	public Lab3FileReader(String strFilePath, int primitiveCnt) {
		
		vHeaders = new Vector<String>();
		vDataColumns = new Vector<Vector<Double>>();
		vDataColumnsEvenNumberedRows = new Vector<Vector<Double>>();
		vDataColumnsOddNumberedRows = new Vector<Vector<Double>>();
		vDataLineEvenNumberedRows = new Vector<Vector<Double>>();
		vDataLineOddNumberedRows = new Vector<Vector<Double>>();
		
		try {
			BufferedReader br;
			String oneLine;
			String[] splitString;
			double dblOneItem;
			int rowNumber = 0;
			
			// Pass once for the headers, just read one line
			br = new BufferedReader(new FileReader(strFilePath));
		    
		    if ((oneLine = br.readLine()) != null && oneLine.length() > 0) {
		    	splitString = oneLine.split(";");
		    	for (int i = 0; i < splitString.length; i++) {
		    		vHeaders.add(splitString[i]);
		    	}
		    }
		    
		    // Initialize every sub-vector
		    for (int i = 0; i < vHeaders.size(); i++) {
		    	vDataColumns.addElement(new Vector<Double>());
		    	vDataColumnsEvenNumberedRows.addElement(new Vector<Double>());
		    	vDataColumnsOddNumberedRows.addElement(new Vector<Double>());
	    	}
		    
		    // Second pass for the data
		    br = new BufferedReader(new FileReader(strFilePath));
		    br.readLine(); // skip first line
		    while ((oneLine = br.readLine()) != null && oneLine.length() > 0) {
		    	splitString = oneLine.split(";");
		    	Vector<Double> vline = new Vector<Double>();
		    	
		    	for (int i = 0; i < splitString.length; i++)
		    	{
		    		dblOneItem = Double.parseDouble(splitString[i]);
		    		vline.add(dblOneItem);
		    		
		    		//vDataColumns.elementAt(i).add(dblOneItem);
		    		if (rowNumber % 2 == 0) {
		    			vDataColumnsEvenNumberedRows.elementAt(i).add(dblOneItem);
		    		}
		    		else {
		    			vDataColumnsOddNumberedRows.elementAt(i).add(dblOneItem);
		    		}
		    	}
		    	
		    	if (rowNumber % 2 == 0) {
		    		vDataLineEvenNumberedRows.add(vline);
		    	}	else {
		    		vDataLineOddNumberedRows.add(vline);
		    	}
		    	
		    	rowNumber++;
		    }
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
	
	public Vector<Double> GetDataColumn(int i) {
		return vDataColumns.elementAt(i);
	}
	
	public Vector<Double> GetDataColumnEvenRowsOnly(int i) {
		return vDataColumnsEvenNumberedRows.elementAt(i);
	}
	
	public Vector<Double> GetDataColumnEvenRowsOnly() {
		return vDataColumnsEvenNumberedRows.get(0);
	}
	
	public Vector<Double> GetDataColumnOddRowsOnly(int i) {
		return vDataColumnsOddNumberedRows.elementAt(i);
	}
	
	public Vector<Double> GetDataLineOddRowsOnly(int i) {
		return vDataLineOddNumberedRows.elementAt(i);
	}
	
	public Vector<Double> GetDataLineEvenRowsOnly(int i) {
		return vDataLineEvenNumberedRows.elementAt(i);
	}
}
