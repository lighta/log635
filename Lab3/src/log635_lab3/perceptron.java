package log635_lab3;

import java.io.PipedReader;
import java.io.PipedWriter;
import java.lang.Math;

import com.sun.java.swing.plaf.windows.WindowsTreeUI.ExpandedIcon;

public class perceptron extends Thread {
	boolean outputSent;
	
	private PipedReader[] inputPipes;
	private double[] inputWeights;
	private double bias;
	private double biasWeight;
	private PipedWriter outPipe;
	
	public perceptron(PipedWriter[] inputPipes, PipedWriter outPipe)
	{
		bias = 1;
		biasWeight = 1;
		this.outPipe = outPipe;
		int i=0;
		while(this.inputPipes.length < i++){ // Randomize every weights.
			inputWeights[i] = Math.random();
		}
		connect(inputPipes,outPipe);
	}
	
	public double calcOutput(double[] inputs)
	{
		double sum = 0, out;
		// Calc the perceptron value.
		for(int i=0; i < inputs.length-1; i++)
		{
			sum += inputs[i] * inputWeights[i];
		}
		sum += bias * biasWeight;
		// Activation function in this case sigmoid.
		out = ( 1/ (1 +  Math.exp( -sum ) ) );
		out -= inputs[inputs.length] * inputWeights[inputWeights.length];
		return out;
	}
	
	private void connect(PipedWriter[] inputPipes, PipedWriter outPipe){
		try 
		{
			// Connect InputPipes to upstream filters
			for(int i=0; i < this.inputPipes.length-1; i++)
			{
				this.inputPipes[i] = new PipedReader();
				this.inputPipes[i].connect(inputPipes[i]);
			}
			System.out.println("FormatFilter:: connected to upstream filters.");
		} 
		catch (Exception Error) 
		{
			System.out.println("FormatFilter:: Error connecting input pipes.");
		} 
		try 
		{
			// Connect outputPipe to downstream filter
			this.outPipe = outPipe;
			System.out.println("FormatFilter:: connected to downstream filter.");
		} 
		catch (Exception Error) 
		{
			System.out.println("FormatFilter:: Error while connecting output pipe.");
		} 
	}
	
	private void disconnect(){
		try 
		{
			int i = 0;
			while(inputPipes.length < i++)
			{ 
				inputPipes[i].close();
			}
			System.out.println("StatusFilter:: inputs pipes closed.");
			
			outPipe.close();
			System.out.println("StatusFilter:: output pipe closed.");
			
		} 
		catch (Exception Error) 
		{
			System.out.println("StatusFilter:: Error while closing pipes.");
		} 
	}
	
	
	public void run() {		
		try {
			outputSent = false;
			while (!outputSent) 
			{
				double[] inputsValue = new double[inputPipes.length];
				int i = 0;
				
				//@TODO pe test avec dataoutputstream
				while(inputPipes.length < i++) // For each inputPipes.
				{ 
					boolean EOL = false; // End Of Line.
					while(EOL == false)
					{
						String lineOfText = null;
						char[] ch = new char[1]; // Char array is required to turn since pipes read one char at a time.
						int value; // The value read from the pipe.
						value = inputPipes[i].read();
						ch[0] = (char) value;
						if (value == -1) { // Pipe is closed.
							outputSent = true;
						} else {
							lineOfText += new String( ch);
						}
						if (value == '\n') {  // End of line	
							inputsValue[i] = Double.parseDouble( lineOfText );
							EOL = true;
						}
					}
				}
				double res = calcOutput(inputsValue);
				outPipe.write(res + "\n");
				outPipe.flush();
			} 
		} 
		catch (Exception error) 
		{
			System.out.println("StatusFilter:: Interrupted.");
		} 
		disconnect();
	}
	
}
