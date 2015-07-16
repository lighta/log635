package log635_lab3;

public class perceptron {

	private float[] inputs;
	private float[] inputWeights;
	private float bias;
	private float biasWeight;
	private float output;
	
	public perceptron(float[] inputs)
	{
		this.inputs = inputs;
	}
	public perceptron(float[] inputs, float[] inputWeights, float bias, float biasWeight)
	{
		this.inputs = inputs;
		this.inputWeights = inputWeights;
		this.bias = bias;
		this.biasWeight = biasWeight;
	}
}
