package NNSolutionThree;

import java.util.*;

/**
 * Represents an input to the Neural Network
 */
public class Source implements NeuronInput{
	//The values queued to be source values
	private List<Double> values;
	//The index of the current value in the list
	private int CurrentIndex;

	/**
	 * Constructs a new, empty Source object
	 */
	public Source() {
		values = new ArrayList<>();
		CurrentIndex = 0;
	}

	/**
	 * Adds a value to the waiting queue
	 * @param d
	 */
	public void QueueInputValue(double d) {
		values.add(d);
	}

	/**
	 * Jumps to the next value in the queue
	 */
	public void OnNextInput() {
		CurrentIndex++;
	}

	/**
	 * Returns the current output value
	 * @return The current output value
	 */
	@Override
	public double getOutput() {
		return values.get(CurrentIndex);
	}
}
