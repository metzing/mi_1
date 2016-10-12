package NNSolutionThree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Metzing on 12/10/2016 14:33.
 */
public abstract class Neuron {
	protected int indexInLayer;
	protected double bias;
	protected List<Double> weights;
	protected boolean hasDerivates;
	protected List<Double> derivates;
	protected List<NeuronInput> inputs;
	protected List<Neuron> outputs;
	protected boolean hasDelta;
	protected double delta;

	public void setWeights(List<Double> _weights, double _bias) {
		weights = new ArrayList<>();
		weights.addAll(_weights);
		bias = _bias;
	}

	public double getWeight(int index) {
		return weights.get(index);
	}

	public List<Double> getDerivates() {
		if(hasDerivates) return derivates;

		derivates = new ArrayList<>();
		hasDerivates = true;

		for (NeuronInput in : inputs){
			derivates.add(getDelta()*in.getInput());
		}

		derivates.add(getDelta());

		return derivates;
	}

	public abstract double getDelta();
}
