package log635_lab3;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	public static void test_shoutData(PipedWriter[] inputPipes){
		//simulate shout data
		try {
			inputPipes[0].write("0\n1\n0\n1\n"); //0101
			inputPipes[1].write("0\n0\n1\n1\n"); //0011
			
			inputPipes[0].close();	
			inputPipes[1].close();
		} catch (IOException e) {
			e.printStackTrace();
		} //0101
	}
	
	public static Double readOneSortie(PipedReader entree){
		if(entree==null) //useless but whatever
			return null;

		double val=-1;
		boolean EOL = false;
		StringBuilder lineOfText = new StringBuilder();
		try {	
			while(EOL == false){
				int value; // The value read from the pipe.
				value = entree.read();
				char ch = (char) value;
				if (value == -1) { // Pipe is closed.
					break;
				} else {
					lineOfText.append(ch);
				}
				if (value == '\n') {  // End of line	
					val = Double.parseDouble( lineOfText.toString() );
					EOL = true;
					//System.out.println("val="+val);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return val;
	}
	
	public static List<Double> readSortie(PipedReader entree){
		if(entree==null) //useless but whatever
			return null;
		
		List<Double> valsortie = new ArrayList<>();
		while(true){
			double val=readOneSortie(entree);
			if (val == -1) // Pipe is closed.
				break;
			valsortie.add(val);
		}
		
		try {
			entree.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valsortie;
	}
}
