package NNSolutionTwo;

import java.util.*;

/**
 * Created by Márton on 10/10/2016.
 */
public class OutputNeuron implements Weightable{
	private int inputCount;
	private double bias;
	private List<Double> weights;
	private List<NeuronInput> inputs;

	public OutputNeuron(List<NeuronInput> _inputs) {
		inputCount = _inputs.size();
		inputs = new ArrayList<>();
		inputs.addAll(_inputs);
	}

	@Override
	public void setWeights(List<Double> _weights, double _bias){
		weights = new ArrayList<>();
		weights.addAll(_weights);
		bias = _bias;
	}

	public double getOutput() {
		double out = 0;
		for (int i = 0; i < inputCount; i++) {
			out += weights.get(i) * inputs.get(i).getInput();
		}
		out += bias;
		return out;
	}
}