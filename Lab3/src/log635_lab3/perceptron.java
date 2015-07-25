package log635_lab3;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class perceptron extends Thread {
	boolean running;
	
	private PipedReader[] inputPipes;
	private double[] inputWeights;
	private double bias;
	private double biasWeight;
	private PipedWriter outPipe;
	boolean isSigmoid;
	final int GUI;		//unique identifier
	
	public perceptron(final int GUI, PipedWriter[] inPipes, PipedWriter outPipe) throws Exception
	{
		this.GUI=GUI;
		this.bias = 1;
		this.biasWeight = 1;
		this.outPipe = outPipe;
		this.isSigmoid = true;
		
		inputPipes = new PipedReader[inPipes.length];
		inputWeights = new double[inPipes.length];
		for(int i=0; i < inPipes.length; i++){ // Randomize every weights.
			inputWeights[i] = Math.random();
		}
		connect(inPipes,outPipe);
	}
	
	public perceptron(final int GUI, PipedWriter[] inPipes, double[] inputWeights, double bias, double biasWeight,PipedWriter outpipe, boolean isSigmoid) throws Exception
 	{		 	
		this.GUI=GUI;
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
		System.out.println("Perceptron["+GUI+"] entering calc");
		double sum = 0, out=0;
		// Calc the perceptron value.
		for(int i=0; i < inputs.length; i++)
		{
			sum += inputs[i] * inputWeights[i];
			System.out.println("Perceptron["+GUI+"] in["+i+"]="+(inputs[i] * inputWeights[i]));
		}
		sum += bias * biasWeight;
		System.out.println("Perceptron["+GUI+"] bias="+(bias * biasWeight)+" sum="+sum+" mode="+((isSigmoid)?"sigmoid":"seuil"));
					
		if(isSigmoid){
			// Activation function in this case sigmoid.
			out = ( 1/ (1 +  Math.exp( -sum ) ) );
		}
		else {
			out = (sum >= 0)?1:0; //1 or 0
		}
		
		System.out.println("Perceptron["+GUI+"] calc done => out="+out);
		return out;
	}
	
	private void connect(PipedWriter[] inputPipes, PipedWriter outPipe) throws Exception{
		System.out.println("Perceptron["+GUI+"] init");
		try 
		{
			// Connect InputPipes to upstream filters
			for(int i=0; i < this.inputPipes.length; i++)
			{
				this.inputPipes[i] = new PipedReader();
				this.inputPipes[i].connect(inputPipes[i]);
				System.out.println("Perceptron["+GUI+"]["+i+"]:: connected to upstream filters.");
			}
			System.out.println("Perceptrons["+GUI+"]:: connected to upstream filters.");
		} 
		catch (Exception Error) 
		{
			System.out.println("Perceptrons["+GUI+"]:: Error connecting input pipes.");
			throw Error;
		} 
		try 
		{
			// Connect outputPipe to downstream filter
			this.outPipe = outPipe;
			System.out.println("Perceptrons["+GUI+"]:: connected to downstream filter.");
		} 
		catch (Exception Error) 
		{
			System.out.println("Perceptrons["+GUI+"]:: Error while connecting output pipe.");
			throw Error;
		} 
		System.out.println("Perceptron["+GUI+"] connected");
	}
	
	private void disconnect(){
		try 
		{
			for(int i=0; i < this.inputPipes.length; i++)
			{ 
				inputPipes[i].close();
				System.out.println("Perceptron["+GUI+"]["+i+"]:: inputs pipes closed.");
			}
			System.out.println("Perceptron["+GUI+"]:: inputs pipes closed.");
			
			outPipe.close();
			System.out.println("Perceptron["+GUI+"]:: output pipe closed.");
			
		} 
		catch (Exception Error) 
		{
			System.out.println("Perceptron["+GUI+"]:: Error while closing pipes.");
		} 
	}
	
	@Override
	public void interrupt() {
		super.interrupt();
		running = false; //gracefully shutdown
	}
	
	@Override
	public void destroy() {
		super.destroy();
		running = false;
		disconnect(); //hard disconection
	}
	
	public void run() {		
		try {
			int j = 0;
			boolean closepipe[] = new boolean[inputPipes.length];
			for(int i=0; i < this.inputPipes.length; i++)
				closepipe[j] = false; // For each inputPipes.

			System.out.println("Perceptron["+GUI+"] started");
			
			running = true;
			while (running) 
			{
				double[] inputsValue = new double[inputPipes.length];
				
				//@TODO pe test avec dataoutputstream
				for(int i=0; i < this.inputPipes.length; i++)
				{ 
					running = false;
					if(closepipe[i] == true){ //skip it
						continue;
					}
					running = true; //on a tjr au moins un pipe ouvert
					
					boolean EOL = false; // End Of Line.
					StringBuilder lineOfText = new StringBuilder();
					
					while(EOL == false)
					{
						int value; // The value read from the pipe.
						value = inputPipes[i].read();
						char ch = (char) value;
						//System.out.println("Perceptron["+i+"] read="+value);
						
						if (value == -1) { // Pipe is closed.
							closepipe[i] = true;
							break; // EOL = true;
						} else {
							lineOfText.append(ch);
						}
						if (value == '\n') {  // End of line	
							inputsValue[i] = Double.parseDouble( lineOfText.toString() );
							EOL = true;
							System.out.println("Perceptron["+GUI+"]["+i+"] received="+inputsValue[i]);
						}
					}
				}
				double res = calcOutput(inputsValue);
				outPipe.write(res + "\n");
				outPipe.flush();
			}
			synchronized(this){
				this.notifyAll(); //tell them we have finish
			}
			System.out.println("Perceptron["+GUI+"] stopped");
		} 
		catch (Exception error) 
		{
			System.out.println("Perceptron["+GUI+"]:: Interrupted. error="+error);
			error.printStackTrace();
		} 
		disconnect();
	}
	
	
	
	public static void main(String[] args) {
		System.out.println("Perception main, quicktest");
		PipedWriter[] inputPipes = new PipedWriter[2]; //x1 et x2
		PipedWriter[] OUinputPipes = new PipedWriter[2]; //x1 et x2
		double inputPipesW[] = new double[2];

		for(int i=0; i < inputPipes.length; i++){
			inputPipes[i] = new PipedWriter();
			OUinputPipes[i] = new PipedWriter();
			inputPipesW[i] = 1.0;
		}
		PipedWriter EToutPipe = new PipedWriter();
		PipedWriter OUoutPipe = new PipedWriter();
		//PipedWriter[] inpipe, double[] inputWeights, double bias, double biasWeight,PipedWriter outpipe, boolean isSigmoid
		perceptron ETpercep, OUpercep;
		try {
			ETpercep = new perceptron(0,inputPipes,inputPipesW,-1.5,1,EToutPipe,false);
			OUpercep = new perceptron(1,OUinputPipes,inputPipesW,-0.5,1,OUoutPipe,false);
			ETpercep.start();
			OUpercep.start();
		} catch (Exception e) {
			System.exit(1);
		}
		
		PipedReader ETentree = null, OUentree = null;
		try { //make ourself ready to receive data
			ETentree = new PipedReader();
			OUentree = new PipedReader();
			ETentree.connect(EToutPipe);
			OUentree.connect(OUoutPipe);
		}
		catch (Exception e) {
			System.exit(1);
		}
		
		utils.test_shoutData(inputPipes);
		utils.test_shoutData(OUinputPipes);
		
		List<Double> sETpercep = utils.readSortie(ETentree);
		System.out.println("Sortie ET perceptron sETpercep="+sETpercep);
		
		List<Double> sOUpercep = utils.readSortie(OUentree);
		System.out.println("Sortie OU perceptron sOUpercep="+sOUpercep);	
	}
}
