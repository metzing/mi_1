package NNSolutionFive;

import java.util.*;

/**
 * Represents a simple Perceptron
 */
public abstract class Neuron implements NeuronInput {
	//General properties of this Neuron
	protected double bias;
	protected List<Double> weights;

	//This Neuron's index in it's layer
	protected int indexInLayer;

	//In and output references of this cell
	protected List<NeuronInput> inputs;
	protected List<Neuron> outputs;

	//Whether the derivates has been calculated or not
	protected boolean hasDerivates;
	//The derivates
	protected List<Double> derivates;

	//Whether the delta value has been calculated or not
	protected boolean hasDelta;
	//The delta value
	protected double delta;

	private List<Double> newWeights;
	private double newBias;

	protected boolean hasError;

	/**
	 * Asks for a Neuron's current output (with the activation function applied)
	 * @return Output of the Neuron
	 */
	public abstract double getOutput();

	/**
	 * Gets the delta value
	 * @return The delta value
	 */
	protected abstract double getDelta();

	/**
	 * Sets the weights and the bias of this object
	 * @param _weights
	 * @param _bias
	 */
	public void setWeights(List<Double> _weights, double _bias) {
		weights = new ArrayList<>();
		weights.addAll(_weights);
		bias = _bias;
	}

	/**
	 * Returns the weight of one of the inputs
	 * @param index
	 * @return
	 */
	public double getWeight(int index) {
		return weights.get(index);
	}

	/**
	 * Returns a list with dem derivates
	 * @return Derivates of weights and (at the last position) the bias
	 */
	public List<Double> getDerivates() {
		//if(hasDerivates) return derivates;

		derivates = new ArrayList<>();
		hasDerivates = true;

		for (NeuronInput in : inputs){
			derivates.add(getDelta()*in.getOutput());
		}

		derivates.add(getDelta());

		return derivates;
	}

	/**
	 * Returns the raw output of this neuron (with the activation function not applied)
	 * @return Raw output
	 */
	protected double getRawOutput(){
		double out = 0;

		for (int i = 0; i < inputs.size(); i++) {
			out += weights.get(i) * inputs.get(i).getOutput();
		}

		out += bias;

		return out;
	}

	public List<Double> getAllWeights(){
		List<Double> copy = new ArrayList<>(weights);
		copy.add(bias);
		return copy;
	}

	public void learn(double mu, double error) {
		newWeights = new ArrayList<>();
		List<Double> der = getDerivates();

		for (int i=0; i<weights.size(); i++){
			newWeights.add(weights.get(i) + der.get(i) * 2 * mu * error);
		}

		newBias = bias + der.get(der.size()-1) * 2 * mu * error;
	}

	public void onNextLearningCycle() {
		hasDelta = false;
		hasDerivates = false;
		hasError = false;

		weights = newWeights;
		bias = newBias;
	}
}