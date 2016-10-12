package NNSolutionThree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Márton on 10/10/2016.
 */
public class HiddenNeuron extends Neuron implements NeuronInput {
	public HiddenNeuron(List<NeuronInput> _inputs, int _indexInLayer) {
		outputs = new ArrayList<>();
		inputs = new ArrayList<>();
		inputs.addAll(_inputs);
		hasDerivates = false;
		indexInLayer = _indexInLayer;
	}

	@Override
	public double getDelta() {
		if (hasDelta) return delta;

		hasDelta = true;
		delta = 0;

		for (Neuron n : outputs) {
			delta += n.getDelta()*n.getWeight(indexInLayer);
		}

		delta *= ReLUDer(getRawOutput());

		return delta;
	}

	@Override
	public void addOutput(Neuron _output) {
		outputs.add(_output);
	}

	@Override
	public double getInput() {
		return Math.max(0.0, getRawOutput());
	}

	private double getRawOutput(){
		double out = 0;
		for (int i = 0; i < inputs.size(); i++) {
			out += weights.get(i) * inputs.get(i).getInput();
		}
		out += bias;
		return out;
	}

	private double ReLUDer(double d){
		return d>0 ? 1 : 0;
	}
}