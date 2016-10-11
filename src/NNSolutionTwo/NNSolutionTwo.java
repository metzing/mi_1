package NNSolutionTwo;

import java.io.*;
import java.util.*;

public class NNSolutionTwo {
	private static List<InputNeuron> inputNeurons;
	private static List<OutputNeuron> outputNeurons;
	private static List<Weightable> weightableNeurons;

	public static void main(String[] args) {
		try {
			//Init input, output Lists
			inputNeurons = new ArrayList<>();
			outputNeurons = new ArrayList<>();
			weightableNeurons = new ArrayList<>();

			//Init input Stream
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			//Get first line of input
			String architecture = br.readLine();
			String[] inputValues = architecture.split(",");

			//Get InputNeuron, Output counts
			int InputCount = Integer.parseInt(inputValues[0]),
					OutputCount = Integer.parseInt((inputValues[inputValues.length - 1]));

			//The prev layer of either HiddenNeurons or Inputs
			List<NeuronInput> prevLayer = new ArrayList<>();

			//Set up InputNeuron Layer
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
					weightableNeurons.add(currentNeuron);
				}
				prevLayer.clear();
				prevLayer.addAll(currentLayer);
				currentLayer.clear();
			}

			//And last lets create the outputs
			for (int i = 0; i < OutputCount; i++) {
				OutputNeuron currentNeuron = new OutputNeuron(prevLayer);
				outputNeurons.add(currentNeuron);
				weightableNeurons.add(currentNeuron);
			}

			//Set the weights of Neurons
			for (Weightable w : weightableNeurons) {
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
}
