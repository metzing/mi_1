package NNSolutionThree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Márton on 10/10/2016.
 */
public class HiddenNeuron implements NeuronInput,Weightable {
	private int inputCount;
	private double bias;
	private List<Double> weights;
	private List<NeuronInput> inputs;

	public HiddenNeuron(List<NeuronInput> _inputs) {
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

	@Override
	public double getInput() {
		double out = 0;
		for (int i = 0; i < inputCount; i++) {
			out += weights.get(i) * inputs.get(i).getInput();
		}
		out += bias;
		return Math.max(0.0, out);
	}
}