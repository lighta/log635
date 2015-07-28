package log635_lab3;

import java.io.PipedReader;
import java.io.PipedWriter;
import java.lang.Math;
import java.util.List;

public class Perceptron extends Thread {
	private boolean running;
	
	private PipedReader[] inputPipes;
	private double[] inputWeights;
	public double[] delta;
	
	private double bias;
	private double biasWeight;
	private PipedWriter outPipe;
	private double localError = 0;
	private double activatedPercepTotal = 0;
	private boolean isSigmoid;
	private final int GUI;		//unique identifier

	private boolean debug = false;
	
	/**
	 * Main Constructeur
	 * Genere des poids aleatoires
	 * @param GUI : Identifiant du percepteurs
	 * @param inPipes : x1..xn
	 * @param outPipe : sortie
	 * @throws Exception : si erreur a la creation (connect)
	 */
	public Perceptron(final int GUI,final PipedWriter[] inPipes,final PipedWriter outPipe) throws Exception
	{
		this.GUI=GUI;
		this.bias = 0.5;
		this.biasWeight = 1;
		this.outPipe = outPipe;
		this.isSigmoid = true;
		
		inputPipes = new PipedReader[inPipes.length];
		inputWeights = new double[inPipes.length];
		delta = new double[inPipes.length];
		for(int i=0; i < inPipes.length; i++){ // Randomize every weights.
			inputWeights[i] = Math.random();
		}
		connect(inPipes,outPipe);
	}
	
	/**
	 * Constructeur avec tous les parametres d'un perceptron specifie.
	 * (Utils pour tests)
	 * @param GUI : Identifiant du percepteurs
	 * @param inPipes : x1..xn
	 * @param inputWeights : w1..wn
	 * @param bias : x0
	 * @param biasWeight : w0
	 * @param outpipe : sortie
	 * @param isSigmoid : mode de sortie
	 * @throws Exception : si erreur a la creation (connect)
	 */
	public Perceptron(final int GUI,
			final PipedWriter[] inPipes, final double[] inputWeights,
			final double bias, final double biasWeight,
			final PipedWriter outpipe,
			final boolean isSigmoid) throws Exception
 	{		 	
		this.GUI=GUI;
		this.inputWeights = inputWeights;		
		this.bias = bias;
		this.biasWeight = biasWeight;	
		this.isSigmoid = isSigmoid;		
		this.outPipe = outpipe;	
		
		inputPipes = new PipedReader[inPipes.length];
		delta = new double[inPipes.length];
		connect(inPipes,outpipe);
 	}

	public double[] getInputsWeights()
	{
		return inputWeights;
	}
	
	public double getBias()
	{
		return bias;
	}
	
	public double getBiasWeights()
	{
		return biasWeight;
	}
	
	
	/**
	 * Function to calculate the output of this perceptron 
	 * wheter is in Sigmoid mode or Seuil
	 * @param inputs : list of double from the differents input
	 * @return perceptron output
	 */
	private double calcOutput(final double[] inputs)
	{
		StringBuilder _sb = null;
		double percepTotal = 0;
		if(debug ){
			_sb = new StringBuilder();
			_sb.append("Perceptron["+GUI+"] entering calc\n");
		}
		// Calc the perceptron value.
		for(int i=0; i < inputs.length; i++)
		{
			percepTotal += inputs[i] * inputWeights[i];
			if(debug)
				_sb.append("\t in["+i+"]="+inputs[i]+" w="+inputWeights[i]+" inw="+(inputs[i]*inputWeights[i]+"\n") );
		}
		percepTotal += bias * biasWeight;
		if(debug)
			_sb.append("\t bias="+(bias * biasWeight)+" sum="+percepTotal+" mode="+((isSigmoid)?"sigmoid":"seuil")+"\n");
					
		if(isSigmoid){
			// Activation function in this case sigmoid.
			activatedPercepTotal = ( 1/ (1 +  Math.exp( -percepTotal ) ) ) * percepTotal;
		}
		else {
			activatedPercepTotal = (percepTotal >= 0)?1:0; //1 or 0
		}
		
		if(debug){
			_sb.append("\t calc done => out="+activatedPercepTotal);
			System.out.println(_sb);
		}
		return activatedPercepTotal;
	}
	
	
	
	public double getLocalError()
	{
		return localError;
	}
	
	public double getOutput(){
		return activatedPercepTotal;
	}
	
	public void calcLocalError(double desiredOutput){
		localError = desiredOutput - activatedPercepTotal;
	}
	
	
	public double calc_gradient(boolean lastLayer){
		double grad=1;
		if(lastLayer){ //derniere couche
			grad = (localError * activatedPercepTotal * (1 - activatedPercepTotal));
		}
		else {
			grad =  activatedPercepTotal * (1 - activatedPercepTotal);
		}
		return grad;
	}
	
	public double modifyWeight(double learningFactor, double grad)
	{
		double delta = learningFactor * grad * activatedPercepTotal;
		//on update tous nos poid
		int i=0;
		while ( i < inputWeights.length){
			inputWeights[i] += delta;
			i++;
		}
		return delta;
	}
	
	
	/**
	 * Function to connect to input and output stream
	 * @param inputPipes : inpipe to connect to (get data from)
	 * @param outPipe  : outpipe to connect to (push data to)
	 * @throws Exception if we can't connect
	 */
	private void connect(final PipedWriter[] inputPipes,final PipedWriter outPipe) throws Exception{
		System.out.println("Perceptron["+GUI+"] init");
		try 
		{
			// Connect InputPipes to upstream filters
			for(int i=0; i < this.inputPipes.length; i++)
			{
				this.inputPipes[i] = new PipedReader();
				this.inputPipes[i].connect(inputPipes[i]);
				//System.out.println("Perceptron["+GUI+"]["+i+"]:: connected to upstream filters.");
			}
			//System.out.println("Perceptrons["+GUI+"]:: connected to upstream filters.");
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
			//System.out.println("Perceptrons["+GUI+"]:: connected to downstream filter.");
		} 
		catch (Exception Error) 
		{
			System.out.println("Perceptrons["+GUI+"]:: Error while connecting output pipe.");
			throw Error;
		} 
		System.out.println("Perceptron["+GUI+"] connected");
	}
	
	/**
	 * Function to close streams
	 */
	private void disconnect(){
		try 
		{
			for(int i=0; i < this.inputPipes.length; i++)
			{ 
				if(inputPipes[i] != null)
					inputPipes[i].close();
				//System.out.println("Perceptron["+GUI+"]["+i+"]:: inputs pipes closed.");
			}
			//System.out.println("Perceptron["+GUI+"]:: inputs pipes closed.");
			if(outPipe != null)
				outPipe.close();
			//System.out.println("Perceptron["+GUI+"]:: output pipe closed.");	
		} 
		catch (Exception Error) 
		{
			System.out.println("Perceptron["+GUI+"]:: Error while closing pipes.");
		}
		System.out.println("Perceptron["+GUI+"] disconnected");
	}
	
	/**
	 * Function to stop a perceptron
	 */
	@Override
	public void interrupt() {
		super.interrupt();
		running = false;
		disconnect(); //hard disconection
	}
	
	/**
	 * Perceptron handler
	 * receive data from input then call calc
	 */
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
					
					inputsValue[i] = -1;
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
							//System.out.println("Perceptron["+GUI+"]["+i+"] received="+inputsValue[i]);
						}
					}
				}
				if(running && inputsValue[0]!=-1){
					double res = calcOutput(inputsValue);
					outPipe.write(res + "\n");
					outPipe.flush();
				}
			}
			synchronized(this){
				this.notifyAll(); //tell them we have finish
			}
			System.out.println("Perceptron["+GUI+"] stopped");
		} 
		catch (Exception error) 
		{
			System.out.println("Perceptron["+GUI+"]:: Interrupted. error="+error);
			//error.printStackTrace();
		} 
		disconnect();
	}
	
	
	public double[] getInputWeights() {
		return inputWeights;
	}

	public void setInputWeights(int i, double iw) {
		this.inputWeights[i] = iw;
	}
	
	public void setInputWeights(double[] inputWeights) {
		this.inputWeights = inputWeights;
	}
	
	@Override
	public String toString() {
		String perstr = "Perceptron GUI="+GUI+" nbinput="+inputPipes.length+"\n";
		perstr += "\t bias="+bias+" bw="+biasWeight+"\n";
		for(int i=0; i<inputPipes.length; i++){
			perstr += "\t w"+i+"="+inputWeights[i]+"\n";
		}
		return perstr;
	}
	
	
	/**
	 * Small main to try out perceptron is behaving well
	 * @param args
	 */
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
		Perceptron ETpercep, OUpercep;
		try {
			ETpercep = new Perceptron(0,inputPipes,inputPipesW,-1.5,1,EToutPipe,false);
			OUpercep = new Perceptron(1,OUinputPipes,inputPipesW,-0.5,1,OUoutPipe,false);
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
		
		Utils.test_shoutData(inputPipes);
		Utils.test_shoutData(OUinputPipes);
		
		List<Double> sETpercep = Utils.readSortie(ETentree);
		System.out.println("Sortie ET perceptron sETpercep="+sETpercep);
		
		List<Double> sOUpercep = Utils.readSortie(OUentree);
		System.out.println("Sortie OU perceptron sOUpercep="+sOUpercep);	
	}
}
