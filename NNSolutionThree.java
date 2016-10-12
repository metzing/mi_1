package NNSolutionThree;

import java.io.*;
import java.util.*;

public class NNSolutionThree {
	private static List<Input> inputs;
	private static List<OutputNeuron> outputNeurons;
	private static List<Neuron> allNeurons;


	public static void main(String[] args) {
		try {
			//Init input, output Lists
			inputs = new ArrayList<>();
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
				Input currentInput = new Input();
				inputs.add(currentInput);
				prevLayer.add(currentInput);
			}

			List<NeuronInput> currentLayer = new ArrayList<>();

			//For each Integer in the first line (except first and last)
			for (int i = 1; i < inputValues.length - 1; i++) {
				//We create a new Layer of HiddenNeurons with their number being in the said Integer
				for (int j = 0; j < Integer.parseInt(inputValues[i]); j++) {
					HiddenNeuron currentNeuron = new HiddenNeuron(prevLayer, j);
					currentLayer.add(currentNeuron);
					for (NeuronInput n : prevLayer) {
						n.addOutput(currentNeuron);
					}
					allNeurons.add(currentNeuron);
				}
				prevLayer.clear();
				prevLayer.addAll(currentLayer);
				currentLayer.clear();
			}

			//And last lets create the outputs
			for (int i = 0; i < OutputCount; i++) {
				OutputNeuron currentNeuron = new OutputNeuron(prevLayer, i);
				outputNeurons.add(currentNeuron);
				for (NeuronInput n : prevLayer) {
					n.addOutput(currentNeuron);
				}
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
				for (int j = 0; j < inputs.size(); j++) {
					inputs.get(j).QueueInputValue(Double.parseDouble(inputStrings[j]));
				}
			}

			//Finally...
			System.out.println(architecture);
			for (Neuron n: allNeurons) {
				printDoubleList(n.getDerivates());
			}


		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void printDoubleList(List<Double> list){
		for (int i=0; i<list.size();i++){
			System.out.print(list.get(i));
			if (i!=list.size()-1) System.out.print(",");
		}
		System.out.print("\n");
	}
}
