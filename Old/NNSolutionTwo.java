package NNSolutionTwo;

import java.io.*;
import java.util.*;

public class NNSolutionTwo {
	private static List<InputNeuron> inputNeurons;
	private static List<OutputNeuron> outputNeurons;
	private static List<Neuron> allNeurons;

	public static void main(String[] args) {
		try {
			//Init input, output Lists
			inputNeurons = new ArrayList<>();
			outputNeurons = new ArrayList<>();
			allNeurons = new ArrayList<>();

			//Init input Stream
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			//Get first line of input
			String architecture = br.readLine();
			String[] inputValues = architecture.split(",");

			//Get Input, Output counts
			int InputCount = Integer.parseInt(inputValues[0]),
					OutputCount = Integer.parseInt((inputValues[inputValues.length - 1]));

			//The prev layer of either HiddenNeurons or Inputs
			List<NeuronInput> prevLayer = new ArrayList<>();

			//Set up Input Layer
			for (int i = 0; i < InputCount; i++) {
				InputNeuron currentInputNeuron = new InputNeuron();
				inputNeurons.add(currentInputNeuron);
				prevLayer.add(currentInputNeuron);
			}

			List<NeuronInput> currentLayer = new ArrayList<>();

			//For each Integer in the first line (except first and last)
			for (int i = 1; i < inputValues.length - 1; i++) {
				//We create a new Layer of HiddenNeurons with their number being in the said Integer
				for (int j = 0; j < Integer.parseInt(inputValues[i]); j++) {
					HiddenNeuron currentNeuron = new HiddenNeuron(prevLayer);
					currentLayer.add(currentNeuron);
					allNeurons.add(currentNeuron);
				}
				prevLayer.clear();
				prevLayer.addAll(currentLayer);
				currentLayer.clear();
			}

			//And last lets create the outputs
			for (int i = 0; i < OutputCount; i++) {
				OutputNeuron currentNeuron = new OutputNeuron(prevLayer);
				outputNeurons.add(currentNeuron);
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

			//Queue inputs
			Integer InputCicles = Integer.parseInt(br.readLine());

			for (int i = 0; i < InputCicles; i++) {
				String[] inputStrings = br.readLine().split(",");
				for (int j = 0; j < inputNeurons.size(); j++) {
					inputNeurons.get(j).QueueInputValue(Double.parseDouble(inputStrings[j]));
				}
			}

			//Finally...
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
			bw.write(InputCicles.toString() + "\n");
			for (int i = 0; i < InputCicles; i++) {
				List<Double> outputs = GetNextOutputList();
				for (int j = 0; j < outputs.size(); j++) {
					bw.write(outputs.get(j).toString());
					if (!(j == outputs.size() - 1)) bw.write(',');
				}
				bw.write("\n");
			}
			bw.flush();

		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static List<Double> GetNextOutputList() {
		List<Double> outs = new ArrayList<>();
		for (InputNeuron in : inputNeurons) {
			in.OnNextInput();
		}
		for (OutputNeuron on : outputNeurons) {
			outs.add(on.getOutput());
		}
		return outs;
	}

	interface NeuronInput {
		double getInput();
	}

	static class InputNeuron implements NeuronInput {
		private int CurrentIndex;
		private List<Double> values;

		public InputNeuron() {
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
		public double getInput() {
			return values.get(CurrentIndex);
		}
	}

	static abstract class Neuron {
		protected double bias;
		protected List<Double> weights;
		protected boolean hasDerivates;
		protected List<Double> derivates;
		protected List<NeuronInput> inputs;
		protected List<Neuron> outputs;

		public void setWeights(List<Double> _weights, double _bias) {
			weights = new ArrayList<>();
			weights.addAll(_weights);
			bias = _bias;
		}

		public void addOutput(Neuron _output) {
			outputs.add(_output);
		}

		public abstract List<Double> getDerivates();
	}

	static class HiddenNeuron extends Neuron implements NeuronInput {
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
		public double getInput() {
			double out = 0;
			for (int i = 0; i < inputs.size(); i++) {
				out += weights.get(i) * inputs.get(i).getInput();
			}
			out += bias;
			return Math.max(0.0, out);
		}
	}

	static class OutputNeuron extends Neuron {
		public OutputNeuron(List<NeuronInput> _inputs) {
			inputs = new ArrayList<>();
			inputs.addAll(_inputs);
		}

		@Override
		public List<Double> getDerivates() {
			return null;
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


}
