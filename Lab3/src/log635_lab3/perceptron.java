package log635_lab3;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.lang.Math;

public class perceptron extends Thread {
	boolean running;
	
	private PipedReader[] inputPipes;
	private double[] inputWeights;
	private double bias;
	private double biasWeight;
	private PipedWriter outPipe;
	boolean isSigmoid;
	
	public perceptron(PipedWriter[] inPipes, PipedWriter outPipe) throws Exception
	{
		this.bias = 1;
		this.biasWeight = 1;
		this.outPipe = outPipe;
		this.isSigmoid = true;
		
		int i=0;
		inputPipes = new PipedReader[inPipes.length];
		while(inPipes.length < i++){ // Randomize every weights.
			inputWeights[i] = Math.random();
		}
		connect(inPipes,outPipe);
	}
	
	public perceptron(PipedWriter[] inPipes, double[] inputWeights, double bias, double biasWeight,PipedWriter outpipe, boolean isSigmoid) throws Exception
 	{		 	
		this.inputWeights = inputWeights;		
		this.bias = bias;
		this.biasWeight = biasWeight;	
		this.isSigmoid = isSigmoid;		
		this.outPipe = outpipe;	
		
		inputPipes = new PipedReader[inPipes.length];
		connect(inPipes,outpipe);
 	}
	
	public double calcOutput(double[] inputs)
	{
		double sum = 0, out=0;
		// Calc the perceptron value.
		for(int i=0; i < inputs.length; i++)
		{
			sum += inputs[i] * inputWeights[i];
		}
		sum -= bias * biasWeight;
					
		if(isSigmoid){
			// Activation function in this case sigmoid.
			out = ( 1/ (1 +  Math.exp( -sum ) ) );
		}
		else {
			out = (sum >= 0)?1:0; //1 or 0
		}
		return out;
	}
	
	private void connect(PipedWriter[] inputPipes, PipedWriter outPipe) throws Exception{
		System.out.println("Perceptron init stared");
		try 
		{
			// Connect InputPipes to upstream filters
			for(int i=0; i < this.inputPipes.length; i++)
			{
				this.inputPipes[i] = new PipedReader();
				this.inputPipes[i].connect(inputPipes[i]);
				System.out.println("Perceptron["+i+"]:: connected to upstream filters.");
			}
			System.out.println("Perceptrons:: connected to upstream filters.");
		} 
		catch (Exception Error) 
		{
			System.out.println("Perceptrons:: Error connecting input pipes.");
			throw Error;
		} 
		try 
		{
			// Connect outputPipe to downstream filter
			this.outPipe = outPipe;
			System.out.println("Perceptrons:: connected to downstream filter.");
		} 
		catch (Exception Error) 
		{
			System.out.println("Perceptrons:: Error while connecting output pipe.");
			throw Error;
		} 
		System.out.println("Perceptron init done (connected)");
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
			int i = 0;
			boolean closepipe[] = new boolean[inputPipes.length];
			while(inputPipes.length < i++)	{  // For each inputPipes.
				closepipe[i] = false;
			}
			System.out.println("Perceptron stared");
			
			running = true;
			while (running) 
			{
				double[] inputsValue = new double[inputPipes.length];
				i = 0;
				
				//@TODO pe test avec dataoutputstream
				while(inputPipes.length < i++) // For each inputPipes.
				{ 
					running = false;
					if(closepipe[i] == true) //skip it
						continue;
					running = true; //on a tjr au moins un pipe ouvert
					
					boolean EOL = false; // End Of Line.
					while(EOL == false)
					{
						String lineOfText = null;
						char[] ch = new char[1]; // Char array is required to turn since pipes read one char at a time.
						int value; // The value read from the pipe.
						value = inputPipes[i].read();
						ch[0] = (char) value;
						if (value == -1) { // Pipe is closed.
							closepipe[i] = true;
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
			
			System.out.println("Perceptron stopped");
		} 
		catch (Exception error) 
		{
			System.out.println("StatusFilter:: Interrupted.");
		} 
		disconnect();
	}
	
	
	public static void main(String[] args) {
		System.out.println("Perception main, quicktest");
		PipedWriter[] inputPipes = new PipedWriter[2]; //x1 et x2
		double inputPipesW[] = new double[2];
		int i=0;
		while(i< inputPipes.length){
			inputPipes[i] = new PipedWriter();
			inputPipesW[i] = 1.0;
			i++;
		}
		PipedWriter outPipe = new PipedWriter();
		//PipedWriter[] inpipe, double[] inputWeights, double bias, double biasWeight,PipedWriter outpipe, boolean isSigmoid
		perceptron ETpercep;
		try {
			ETpercep = new perceptron(inputPipes,inputPipesW,-0.5,1,outPipe,false);
			ETpercep.start();
		} catch (Exception e) {
			System.exit(1);
		}
		
		try {
			inputPipes[0].write("0\n1\n0\n1\n"); //0101
			inputPipes[1].write("0\n0\n1\n1\n"); //0011
			//we hot like that that we know we can close the stream now
			inputPipes[0].close();	
			inputPipes[1].close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PipedReader outPipeEnd = new PipedReader();
		try {
			outPipeEnd.connect(outPipe);
			boolean done=false; 
			
			String lineOfText = null;
			
			while(done==false){
				double val=0;
				boolean EOL = false;
				while(EOL == false){
					char[] ch = new char[1]; // Char array is required to turn since pipes read one char at a time.
					int value; // The value read from the pipe.
					value = outPipeEnd.read();
					ch[0] = (char) value;
					if (value == -1) { // Pipe is closed.
						done=true;
					} else {
						lineOfText += new String( ch);
					}
					if (value == '\n') {  // End of line	
						val = Double.parseDouble( lineOfText );
						EOL = true;
						System.out.println("val="+val);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
