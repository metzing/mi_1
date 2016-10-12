package NNSolutionThree;

import java.util.ArrayList;

/**
 * Created by Márton on 10/10/2016.
 */
public class OutputNeuron extends Neuron {
	/**
	 * Creates an OutputNeuron object and connects it to the NeuronInputs of the previous layer
	 * @param previousLayer The previous layer
	 * @param _indexInLayer This Neuron's index in it's layer
	 */
	public OutputNeuron(Layer previousLayer, int _indexInLayer) {
		inputs = new ArrayList<>();
		inputs.addAll(previousLayer.getInputs());
		hasDerivates = false;
		indexInLayer = _indexInLayer;
	}

	/**
	 * Returns the delta value of this Neuron
	 * @return The delta value of this Neuron
	 */
	@Override
	public double getDelta(){
		return 1.0;
	}

	/**
	 * Returns the output value of this Neuron, with the activation function applied
	 * @return The output of this Neuron
	 */
	@Override
	public double getOutput() {
		return getRawOutput();
	}
}