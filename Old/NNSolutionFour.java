package NNSolutionFour;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class NNSolutionFour {
	private static List<Source> sources;
	private static List<OutputNeuron> outputNeurons;
	private static List<Neuron> allNeurons;

	public static void main(String[] args) {
		try {
			//Init input, output Lists
			sources = new ArrayList<>();
			outputNeurons = new ArrayList<>();
			allNeurons = new ArrayList<>();

			//Init input Stream
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			//Get learning parameters
			String[] learningParametersString = br.readLine().split(",");
			int numberOfEpochs = Integer.parseInt(learningParametersString[0]);
			double mu = Double.parseDouble(learningParametersString[1]);
			double learningSampleRatio = Double.parseDouble(learningParametersString[2]);

			//Get architecture of input
			String architecture = br.readLine();
			String[] inputValues = architecture.split(",");

			//Get Source, Output counts
			int InputCount = Integer.parseInt(inputValues[0]),
					OutputCount = Integer.parseInt((inputValues[inputValues.length - 1]));

			Layer previousLayer = new Layer();

			//Set up Source Layer
			for (int i = 0; i < InputCount; i++) {
				Source currentSource = new Source();
				sources.add(currentSource);
				previousLayer.addInput(currentSource);
			}

			Layer currentLayer = new Layer();

			//Set up Hidden Layer(s)
			for (int i = 1; i < inputValues.length - 1; i++) {
				//We create a new Layer of HiddenNeurons
				for (int j = 0; j < Integer.parseInt(inputValues[i]); j++) {
					HiddenNeuron currentNeuron = new HiddenNeuron(previousLayer, j);
					currentLayer.addInput(currentNeuron);
					previousLayer.addOutput(currentNeuron);
					allNeurons.add(currentNeuron);
				}
				previousLayer = currentLayer;
				currentLayer = new Layer();
			}

			//Set up output layer
			for (int i = 0; i < OutputCount; i++) {
				OutputNeuron currentNeuron = new OutputNeuron(previousLayer, i);
				outputNeurons.add(currentNeuron);
				previousLayer.addOutput(currentNeuron);
				allNeurons.add(currentNeuron);
			}

			//Set the weights of Neurons
			for (Neuron w : allNeurons) {
				String[] weightStrings = br.readLine().split(",");
				List<Double> weights = new ArrayList<>();

				for (int i = 0; i < weightStrings.length - 1; i++) {
					weights.add(Double.parseDouble(weightStrings[i]));
				}

				w.setWeights(weights, Double.parseDouble(weightStrings[weightStrings.length - 1]));
			}

			//Get number of input samples
			Integer numberOfSamples = Integer.parseInt(br.readLine());

			//Store learning samples
			List<List<Double>> learningSamples = new ArrayList<>();
			List<Double> currentSample;

			for (int i = 0; i < Math.floor(numberOfSamples * learningSampleRatio); i++) {
				String[] currentSampleString = br.readLine().split(",");
				currentSample = new ArrayList<>();
				for (int j = 0; j < currentSampleString.length; j++) {
					currentSample.add(Double.parseDouble(currentSampleString[j]));
				}
				learningSamples.add(currentSample);
			}

			//Store validation samples
			List<List<Double>> validationSamples = new ArrayList<>();

			for (int i = 0; i < Math.ceil(numberOfSamples * (1 - learningSampleRatio)); i++) {
				String[] currentSampleString = br.readLine().split(",");
				currentSample = new ArrayList<>();
				for (int j = 0; j < currentSampleString.length; j++) {
					currentSample.add(Double.parseDouble(currentSampleString[j]));
				}
				validationSamples.add(currentSample);
			}

			//For each epoch
			for (int i = 0; i < numberOfEpochs; i++) {
				//Learn from all learning samples
				for (int j = 0; j < learningSamples.size(); j++) {
					//Set inputs for this sample
					currentSample = learningSamples.get(j);
					for (int k = 0; k < sources.size(); k++) {
						sources.get(k).setOutput(currentSample.get(k));
					}

					//Set desired outputs
					for (int k = 0; k < currentSample.size() - sources.size(); k++) {
						outputNeurons.get(k).setDesiredOutput(currentSample.get(sources.size() + k));
						double error = outputNeurons.get(k).getError(false);

						//Modify the weights and biases
						for (Neuron n : allNeurons) {
							if (!outputNeurons.contains(n)) {
								n.learn(mu, error);
							}
						}

						outputNeurons.get(k).learn(mu,error);
					}

					//Mark cell dirty for the next iteration
					for (Neuron n : allNeurons) {
						n.onNextLearningCycle();
					}
				}

				double errorSqrSum = 0;
				//For each validation sample
				for (int j = 0; j < validationSamples.size(); j++) {
					currentSample = validationSamples.get(j);
					for (int k = 0; k < sources.size(); k++) {
						sources.get(k).setOutput(currentSample.get(k));
					}

					double thisError;

					//Set desired outputs
					for (int k = 0; k < currentSample.size() - sources.size(); k++) {
						outputNeurons.get(k).setDesiredOutput(currentSample.get(k + sources.size()));
						thisError = outputNeurons.get(k).getError(true);
						errorSqrSum += thisError * thisError;
					}
				}

				System.out.println(errorSqrSum / (validationSamples.size() * outputNeurons.size()));
			}

			//Finally...
			System.out.println(architecture);


			for (Neuron n : allNeurons) {
				printDoubleList(n.getAllWeights());
			}


		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void printDoubleList(List<Double> list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i));
			if (i != list.size() - 1) System.out.print(",");
		}
		System.out.print("\n");
	}

	/**
	 * Represents a source to the Neural Network
	 */
	public static class Source implements NeuronInput {
		private double output;

		/**
		 * Constructs a new, empty Source object
		 */
		public Source() {}

		public void setOutput(double d){
			output = d;
		}

		/**
		 * Returns the current output value
		 *
		 * @return The current output value
		 */
		@Override
		public double getOutput() {
			return output;
		}
	}

	/**
	 * Represents a simple Perceptron
	 */
	public static abstract class Neuron implements NeuronInput {
		//General properties of this Neuron
		protected double bias;
		protected List<Double> weights;

		//This Neuron's index in it's layer
		protected int indexInLayer;

		//In and output references of this cell
		protected List<NeuronInput> inputs;
		protected List<Neuron> outputs;

		//Whether the derivates has been calculated or not
		protected boolean hasDerivates;
		//The derivates
		protected List<Double> derivates;

		//Whether the delta value has been calculated or not
		protected boolean hasDelta;
		//The delta value
		protected double delta;

		private List<Double> newWeights;
		private double newBias;

		protected boolean hasError;

		/**
		 * Asks for a Neuron's current output (with the activation function applied)
		 * @return Output of the Neuron
		 */
		public abstract double getOutput();

		/**
		 * Gets the delta value
		 * @return The delta value
		 */
		protected abstract double getDelta();

		/**
		 * Sets the weights and the bias of this object
		 * @param _weights
		 * @param _bias
		 */
		public void setWeights(List<Double> _weights, double _bias) {
			weights = new ArrayList<>();
			weights.addAll(_weights);
			bias = _bias;
		}

		/**
		 * Returns the weight of one of the inputs
		 * @param index
		 * @return
		 */
		public double getWeight(int index) {
			return weights.get(index);
		}

		/**
		 * Returns a list with dem derivates
		 * @return Derivates of weights and (at the last position) the bias
		 */
		public List<Double> getDerivates() {
			//if(hasDerivates) return derivates;

			derivates = new ArrayList<>();
			hasDerivates = true;

			for (NeuronInput in : inputs){
				derivates.add(getDelta()*in.getOutput());
			}

			derivates.add(getDelta());

			return derivates;
		}

		/**
		 * Returns the raw output of this neuron (with the activation function not applied)
		 * @return Raw output
		 */
		protected double getRawOutput(){
			double out = 0;

			for (int i = 0; i < inputs.size(); i++) {
				out += weights.get(i) * inputs.get(i).getOutput();
			}

			out += bias;

			return out;
		}

		public List<Double> getAllWeights(){
			List<Double> copy = new ArrayList<>(weights);
			copy.add(bias);
			return copy;
		}

		public void learn(double mu, double error) {
			newWeights = new ArrayList<>();
			List<Double> der = getDerivates();

			for (int i=0; i<weights.size(); i++){
				newWeights.add(weights.get(i) + der.get(i) * 2 * mu * error);
			}

			newBias = bias + der.get(der.size()-1) * 2 * mu * error;
		}

		public void onNextLearningCycle() {
			hasDelta = false;
			hasDerivates = false;
			hasError = false;

			weights = newWeights;
			bias = newBias;
		}
	}

	/**
	 * Represents a Neuron in one of the hidden layers
	 */
	public static class HiddenNeuron extends Neuron {
		/**
		 * Creates a HiddenNeuron object and connects it to the NeuronInputs of the previous layer
		 *
		 * @param previousLayer The previous layer
		 * @param _indexInLayer This Neuron's index in it's layer
		 */
		public HiddenNeuron(Layer previousLayer, int _indexInLayer) {
			outputs = new ArrayList<>();
			inputs = new ArrayList<>();
			inputs.addAll(previousLayer.getInputs());
			hasDerivates = false;
			indexInLayer = _indexInLayer;
		}

		/**
		 * Returns the delta value of this Neuron
		 *
		 * @return The delta value of this Neuron
		 */
		@Override
		public double getDelta() {
			if (hasDelta) return delta;

			hasDelta = true;
			delta = 0;

			for (Neuron n : outputs) {
				delta += n.getDelta() * n.getWeight(indexInLayer);
			}

			delta *= ReLUDer(getRawOutput());

			return delta;
		}

		/**
		 * Returns the output value of this Neuron, with the activation function applied
		 *
		 * @return The output of this Neuron
		 */
		@Override
		public double getOutput() {
			return Math.max(0.0, getRawOutput());
		}

		/**
		 * Adds an outgoing connection from this HiddenNeuron to one of the Neurons in the next layer
		 *
		 * @param _output The Neuron in the next layer
		 */
		public void addOutgoingConnection(Neuron _output) {
			outputs.add(_output);
		}

		/**
		 * Returns the value of the derivate of the ReLU function at "d"
		 *
		 * @param d The value where you need the derivate
		 * @return The value of the said function at "d"
		 */
		private double ReLUDer(double d) {
			return d > 0 ? 1 : 0;
		}
	}

	/**
	 * Represents an output to the Neural Network
	 */
	public static class OutputNeuron extends Neuron {
		double desiredOutput;

		double error;

		/**
		 * Creates an OutputNeuron object and connects it to the NeuronInputs of the previous layer
		 *
		 * @param previousLayer The previous layer
		 * @param _indexInLayer This Neuron's index in it's layer
		 */
		public OutputNeuron(Layer previousLayer, int _indexInLayer) {
			inputs = new ArrayList<>();
			inputs.addAll(previousLayer.getInputs());
			hasDerivates = false;
			hasError = false;
			indexInLayer = _indexInLayer;
		}

		/**
		 * Returns the delta value of this Neuron
		 *
		 * @return The delta value of this Neuron
		 */
		@Override
		public double getDelta() {
			return 1;
		}

		/**
		 * Returns the output value of this Neuron, with the activation function applied
		 *
		 * @return The output of this Neuron
		 */
		@Override
		public double getOutput() {
			double d = getRawOutput();
			return d;
		}

		public void setDesiredOutput(double d){
			desiredOutput = d;
		}

		public double getError(boolean validation) {
			if (validation){
				//This is needed so that in the next epoch's first learning sample recalculates this.
				hasError = false;

				error = desiredOutput - getOutput();
			}
			else {
				if (hasError) return error;
				hasError = true;

				error = desiredOutput - getOutput();
			}
			return error;
		}
	}

	/**
	 * Gives a way to ask a Neuron about it's current output
	 */
	interface NeuronInput {
		/**
		 * Asks for a Neuron's current output (with the activation function applied)
		 * @return Output of the Neuron
		 */
		double getOutput();
	}

	/**
	 * Represents a Layer of Neurons
	 */
	public static class Layer {
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
}
