package NNSolutionFive;

/**
 * Represents a source to the Neural Network
 */
public class Source implements NeuronInput {
	private double output;

	/**
	 * Constructs a new, empty Source object
	 */
	public Source() {}

	public void setOutput(double d){
		output = d;
	}

	/**
	 * Returns the current output value
	 *
	 * @return The current output value
	 */
	@Override
	public double getOutput() {
		return output;
	}
}