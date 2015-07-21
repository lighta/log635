package log635_lab3;

import java.lang.Math;

import com.sun.java.swing.plaf.windows.WindowsTreeUI.ExpandedIcon;

public class perceptron {

	private double[] inputs;
	private double[] inputWeights;
	private double bias;
	private double biasWeight;
	private double output;
	private boolean isSigmoid;
	
	public perceptron(double[] inputs, boolean isSigmoid)
	{
		this.inputs = inputs;
		this.isSigmoid = isSigmoid;
		bias = 1;
		biasWeight = 1;
		int i=0;
		while(inputs.length < i++){ //donne un chiffre random a chaque weight
			inputWeights[i] = Math.random();
		}
	}
	
	public perceptron(double[] inputs, double[] inputWeights, double bias, double biasWeight, boolean isSigmoid)
	{
		this.inputs = inputs;
		this.inputWeights = inputWeights;
		this.bias = bias;
		this.biasWeight = biasWeight;
		this.isSigmoid = isSigmoid;
	}
	
	public boolean learn()
	{
		double sum = 0;
		boolean success = false;
		while(!success)
		{
		
			for(int i=0; i < inputs.length-1; i++)
			{
				sum += inputs[i] * inputWeights[i];
			}
			
			sum += bias * biasWeight;
			
			if(isSigmoid)
			{
				output = ( 1/ (1 +  Math.exp( -sum ) ) );
				output -= inputs[inputs.length] * inputWeights[inputWeights.length];
				success = ( 1/ (1 +  Math.exp( -sum ) ) >=0 );
			}
			else
			{
				output = sum - inputs[inputs.length] * inputWeights[inputWeights.length];
				success = sum >= 0;
			}
		}
		return false;
	}
	
}
