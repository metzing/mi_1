package NNSolutionThree;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a "generic" Layer (either NeuronInput or HiddenNeuron)
 */
public class Layer {
	private List<NeuronInput> inputs;
	private List<HiddenNeuron> neurons;

	/**
	 * Creates an empty layer object
	 */
	public Layer(){
		inputs = new ArrayList<>();
		neurons = new ArrayList<>();
	}

	/**
	 * Adds a NeuronInput to the Layer
	 * @param ni the said NeuronInput
	 */
	public void addInput (NeuronInput ni){
		inputs.add(ni);
	}

	/**
	 * Adds a HiddenNeuron to the Layer
	 * @param hn the said HiddenNeuron
	 */
	public void addInput (HiddenNeuron hn){
		neurons.add(hn);
		inputs.add(hn);
	}

	/**
	 * Returns the inputs' list.
	 * @return The NeuronInputs stored in the object
	 */
	public List<NeuronInput> getInputs(){
		return inputs;
	}

	/**
	 * Adds an output reference to the HiddenNeurons in the object
	 * @param n The output to be added
	 */
	public void addOutput(Neuron n){
		for (HiddenNeuron hn :neurons){
			hn.addOutgoingConnection(n);
		}
	}
}
