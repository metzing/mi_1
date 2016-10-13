package NNSolutionFour;

import java.util.ArrayList;

/**
 * Represents a Neuron in one of the hidden layers
 */
public class HiddenNeuron extends Neuron {
	/**
	 * Creates a HiddenNeuron object and connects it to the NeuronInputs of the previous layer
	 *
	 * @param previousLayer The previous layer
	 * @param _indexInLayer This Neuron's index in it's layer
	 */
	public HiddenNeuron(Layer previousLayer, int _indexInLayer) {
		outputs = new ArrayList<>();
		inputs = new ArrayList<>();
		inputs.addAll(previousLayer.getInputs());
		hasDerivates = false;
		indexInLayer = _indexInLayer;
	}

	/**
	 * Returns the delta value of this Neuron
	 *
	 * @return The delta value of this Neuron
	 */
	@Override
	public double getDelta() {
		if (hasDelta) return delta;

		hasDelta = true;
		delta = 0;

		for (Neuron n : outputs) {
			delta += n.getDelta() * n.getWeight(indexInLayer);
		}

		delta *= ReLUDer(getRawOutput());

		return delta;
	}

	/**
	 * Returns the output value of this Neuron, with the activation function applied
	 *
	 * @return The output of this Neuron
	 */
	@Override
	public double getOutput() {
		return Math.max(0.0, getRawOutput());
	}

	/**
	 * Adds an outgoing connection from this HiddenNeuron to one of the Neurons in the next layer
	 *
	 * @param _output The Neuron in the next layer
	 */
	public void addOutgoingConnection(Neuron _output) {
		outputs.add(_output);
	}

	/**
	 * Returns the value of the derivate of the ReLU function at "d"
	 *
	 * @param d The value where you need the derivate
	 * @return The value of the said function at "d"
	 */
	private double ReLUDer(double d) {
		return d > 0 ? 1 : 0;
	}

	@Override
	protected double getDeltaForDerivates() {
		return getDelta();
	}
}