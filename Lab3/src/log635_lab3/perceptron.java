package log635_lab3;

import java.io.PipedReader;
import java.io.PipedWriter;
import java.lang.Math;

import com.sun.java.swing.plaf.windows.WindowsTreeUI.ExpandedIcon;

public class perceptron extends Thread {
	boolean done;
	
	private PipedReader[] inputs;
	private double[] inputWeights;
	private double bias;
	private double biasWeight;
	private PipedWriter output;
	private boolean isSigmoid;
	
	public perceptron(PipedWriter[] inpipe, PipedWriter outpipe, boolean isSigmoid)
	{
		bias = 1;
		biasWeight = 1;
		this.isSigmoid = isSigmoid;
		this.output = output;
		
		int i=0;
		while(inputs.length < i++){ //donne un chiffre random a chaque weight
			inputWeights[i] = Math.random();
		}
		connect(inpipe,outpipe);
	}
	
	public perceptron(PipedWriter[] inpipe, double[] inputWeights, double bias, double biasWeight,PipedWriter outpipe, boolean isSigmoid)
	{
		this.inputWeights = inputWeights;
		this.bias = bias;
		this.biasWeight = biasWeight;
		this.isSigmoid = isSigmoid;
		this.output = output;
		connect(inpipe,outpipe);
	}
	
	private void connect(PipedWriter[] inpipe, PipedWriter outpipe){
		try {
			// Connect InputPipes to upstream filters
			for(int i=0; i < inputs.length-1; i++){
				this.inputs[i] = new PipedReader();
				this.inputs[i].connect(inpipe[i]);
			}
			System.out.println("FormatFilter:: connected to upstream filters.");
		} catch (Exception Error) {
			System.out.println("FormatFilter:: Error connecting input pipes.");
		} // try/catch

		try {
			// Connect outputPipe to downstream filter
			//PipedWriter outputPipe = new PipedWriter();
			this.output = outpipe;
			System.out.println("FormatFilter:: connected to downstream filter.");
		} catch (Exception Error) {
			System.out.println("FormatFilter:: Error connecting output pipe.");
		} // catch
	}
	
	public double calc(double[] inputs)
	{
		double sum = 0, out;
		boolean success;
		
		for(int i=0; i < inputs.length-1; i++)
		{
			sum += inputs[i] * inputWeights[i];
		}
		
		sum += bias * biasWeight;
		
		if(isSigmoid)
		{
			out = ( 1/ (1 +  Math.exp( -sum ) ) );
			out -= inputs[inputs.length] * inputWeights[inputWeights.length];
		}
		else
		{
			out = sum - inputs[inputs.length] * inputWeights[inputWeights.length];
		}
		return out;

	}
	
	public void run() {		
		try {
			done = false;
			while (!done) {
				double[] inputsd = null;
				int i=0;
				
				while(inputs.length < i++){ //prend l'entree de chaque input
					String lineOfText = null;
					char[] ch = new char[1]; // char array is required to turn char into a string
					int intch; // the integer value read from the pipe
					
					intch = inputs[i].read();
					ch[0] = (char) intch;
					if (intch == -1) { // pipe is closed
						done = true;
					} else {
						lineOfText += new String( ch);
					}
					if (intch == '\n') {  // end of line	
						inputsd[i] = Double.parseDouble( lineOfText );
					}
				}
				double res = calc(inputsd);
				output.write(res+"\n");
				output.flush();
			} // while
		} catch (Exception error) {
			System.out.println("StatusFilter:: Interrupted.");
		} // try/catch
		disconnect(); //closing pipe
	}
	
	private void disconnect(){
		try {
			int i=0;
			while(inputs.length < i++){ //ferme chaque input
				inputs[i].close();
			}
			System.out.println("StatusFilter:: inputs pipes closed.");
			output.close();
			System.out.println("StatusFilter:: output pipe closed.");
		} catch (Exception Error) {
			System.out.println("StatusFilter:: Error closing pipes.");
		} // try/catch
	}
	
}
