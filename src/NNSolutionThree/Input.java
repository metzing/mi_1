package NNSolutionThree;

import java.util.ArrayList;
import java.util.List;

public class Input implements NeuronInput {
	private int CurrentIndex;
	private List<Double> values;

	public Input() {
		values = new ArrayList<>();
		CurrentIndex = -1;
	}

	public void QueueInputValue(double d) {
		values.add(d);
	}

	public void OnNextInput() {
		CurrentIndex++;
	}

	@Override
	public void addOutput(Neuron n){}

	@Override
	public double getInput() {
		return values.get(CurrentIndex);
	}
}
