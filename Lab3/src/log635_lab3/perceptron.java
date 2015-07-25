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
	
	public perceptron(PipedWriter[] inPipes, PipedWriter outPipe) throws Exception
	{
		this.bias = 1;
		this.biasWeight = 1;
		this.outPipe = outPipe;
		this.isSigmoid = true;
		
		inputPipes = new PipedReader[inPipes.length];
		for(int i=0; i < inPipes.length; i++){ // Randomize every weights.
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
		System.out.println("Perceptron entering calc");
		double sum = 0, out=0;
		// Calc the perceptron value.
		for(int i=0; i < inputs.length; i++)
		{
			sum += inputs[i] * inputWeights[i];
			System.out.println("Perceptron in["+i+"]="+(inputs[i] * inputWeights[i]));
		}
		sum += bias * biasWeight;
		System.out.println("Perceptron bias="+(bias * biasWeight)+" sum="+sum+" mode="+((isSigmoid)?"sigmoid":"seuil"));
					
		if(isSigmoid){
			// Activation function in this case sigmoid.
			out = ( 1/ (1 +  Math.exp( -sum ) ) );
		}
		else {
			out = (sum >= 0)?1:0; //1 or 0
		}
		
		System.out.println("Perceptron calc done => out="+out);
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
			for(int i=0; i < this.inputPipes.length; i++)
			{ 
				inputPipes[i].close();
				System.out.println("Perceptron["+i+"]:: inputs pipes closed.");
			}
			System.out.println("Perceptron:: inputs pipes closed.");
			
			outPipe.close();
			System.out.println("Perceptron:: output pipe closed.");
			
		} 
		catch (Exception Error) 
		{
			System.out.println("Perceptron:: Error while closing pipes.");
		} 
	}
	
	
	public void run() {		
		try {
			int j = 0;
			boolean closepipe[] = new boolean[inputPipes.length];
			for(int i=0; i < this.inputPipes.length; i++)
				closepipe[j] = false; // For each inputPipes.

			System.out.println("Perceptron started");
			
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
							System.out.println("Perceptron["+i+"] received="+inputsValue[i]);
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
			System.out.println("Perceptron:: Interrupted. error="+error);
			error.printStackTrace();
		} 
		disconnect();
	}
	
	private static List<Double> readSortie(PipedWriter sortie){
		PipedReader entree = new PipedReader();
		List<Double> valsortie = new ArrayList<>();
		try {
			boolean done=false;
			entree.connect(sortie);
			
			while(done==false){
				double val=0;
				boolean EOL = false;
				StringBuilder lineOfText = new StringBuilder();
				
				while(EOL == false){
					int value; // The value read from the pipe.
					value = entree.read();
					
					char ch = (char) value;
					if (value == -1) { // Pipe is closed.
						done=true;
						break;
					} else {
						lineOfText.append(ch);
					}
					if (value == '\n') {  // End of line	
						val = Double.parseDouble( lineOfText.toString() );
						EOL = true;
						//System.out.println("val="+val);
						valsortie.add(val);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valsortie;
	}
	
	public static void main(String[] args) {
		System.out.println("Perception main, quicktest");
		PipedWriter[] inputPipes = new PipedWriter[2]; //x1 et x2
		double inputPipesW[] = new double[2];

		for(int i=0; i < inputPipes.length; i++){
			inputPipes[i] = new PipedWriter();
			inputPipesW[i] = 1.0;
		}
		PipedWriter EToutPipe = new PipedWriter();
		PipedWriter OUoutPipe = new PipedWriter();
		//PipedWriter[] inpipe, double[] inputWeights, double bias, double biasWeight,PipedWriter outpipe, boolean isSigmoid
		perceptron ETpercep, OUpercep;
		try {
			ETpercep = new perceptron(inputPipes,inputPipesW,-1.5,1,EToutPipe,false);
		//	OUpercep = new perceptron(inputPipes,inputPipesW,-0.5,1,OUoutPipe,false);
			ETpercep.start();
		//	OUpercep.start();
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
		List<Double> sETpercep = readSortie(EToutPipe);
		System.out.println("Sortie ET perceptron sETpercep="+sETpercep);
		
	//	List<Double> sOUpercep = readSortie(OUoutPipe);
	//	System.out.println("Sortie OU perceptron sETpercep="+sOUpercep);	
	}
}
