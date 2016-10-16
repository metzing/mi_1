package NNSolutionFive;

import java.util.*;

/**
 * Represents an output to the Neural Network
 */
public class OutputNeuron extends Neuron {
	double desiredOutput;

	double error;

	/**
	 * Creates an OutputNeuron object and connects it to the NeuronInputs of the previous layer
	 *
	 * @param previousLayer The previous layer
	 * @param _indexInLayer This Neuron's index in it's layer
	 */
	public OutputNeuron(Layer previousLayer, int _indexInLayer) {
		inputs = new ArrayList<>();
		inputs.addAll(previousLayer.getInputs());
		hasDerivates = false;
		hasError = false;
		indexInLayer = _indexInLayer;
	}

	/**
	 * Returns the delta value of this Neuron
	 *
	 * @return The delta value of this Neuron
	 */
	@Override
	public double getDelta() {
		return 1;
	}

	/**
	 * Returns the output value of this Neuron, with the activation function applied
	 *
	 * @return The output of this Neuron
	 */
	@Override
	public double getOutput() {
		double d = getRawOutput();
		return d;
	}

	public void setDesiredOutput(double d){
		desiredOutput = d;
	}

	public double getError(boolean validation) {
		if (validation){
			//This is needed so that in the next epoch's first learning sample recalculates this.
			hasError = false;

			error = desiredOutput - getOutput();
		}
		else {
			if (hasError) return error;
			hasError = true;

			error = desiredOutput - getOutput();
		}
		return error;
	}
}
