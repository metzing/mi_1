package NNSolutionThree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Metzing on 12/10/2016 14:33.
 */
public abstract class Neuron {
	protected double bias;
	protected List<Double> weights;
	protected boolean hasDerivates;
	protected List<Double> derivates;
	protected List<NeuronInput> inputs;
	protected List<Neuron> outputs;

	public void setWeights(List<Double> _weights, double _bias) {
		weights = new ArrayList<>();
		weights.addAll(_weights);
		bias = _bias;
	}

	public abstract List<Double> getDerivates();
}
