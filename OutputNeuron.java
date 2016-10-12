package NNSolutionThree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Márton on 10/10/2016.
 */
public class OutputNeuron extends Neuron {
	public OutputNeuron(List<NeuronInput> _inputs, int _indexxInLayer) {
		inputs = new ArrayList<>();
		inputs.addAll(_inputs);
		hasDerivates = false;
	}

	@Override
	public double getDelta(){
		return 1.0;
	}

	public double getOutput() {
		double out = 0;
		for (int i = 0; i < inputs.size(); i++) {
			out += weights.get(i) * inputs.get(i).getInput();
		}
		out += bias;
		return out;
	}
}