package NNSolutionFive;

/**
 * Gives a way to ask a Neuron about it's current output
 */
interface NeuronInput {
	/**
	 * Asks for a Neuron's current output (with the activation function applied)
	 * @return Output of the Neuron
	 */
	double getOutput();
}