package NNSolutionThree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Márton on 10/10/2016.
 */
public class HiddenNeuron extends Neuron implements NeuronInput {
	public HiddenNeuron(List<NeuronInput> _inputs) {
		outputs = new ArrayList<>();
		inputs = new ArrayList<>();
		inputs.addAll(_inputs);
		hasDerivates = false;
	}

	@Override
	public List<Double> getDerivates() {
		if (hasDerivates) return derivates;
		//TODO actual shit here
		return null;
	}

	@Override
	public void addOutput(Neuron _output) {
		outputs.add(_output);
	}

	@Override
	public double getInput() {
		double out = 0;
		for (int i = 0; i < inputs.size(); i++) {
			out += weights.get(i) * inputs.get(i).getInput();
		}
		out += bias;
		return Math.max(0.0, out);
	}
}