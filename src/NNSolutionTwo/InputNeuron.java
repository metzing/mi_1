package NNSolutionTwo;

import java.util.*;

public class InputNeuron implements NeuronInput{
	private int CurrentIndex;
	private List<Double> values;

	public InputNeuron(){
		values = new ArrayList<>();
		CurrentIndex = -1;
	}

	public void QueueInputValue(double d){
		values.add(d);
	}

	public void OnNextInput(){
		CurrentIndex++;
	}
	@Override
	public double getInput() {
		return values.get(CurrentIndex);
	}
}
