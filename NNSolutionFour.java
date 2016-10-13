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
			int numberofEpochs = Integer.parseInt(learningParametersString[0]);
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
				//We create a new Layer of HiddenNeurons with their number being in the said Integer
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
			for (int i = 0; i < numberofEpochs; i++) {
				//Learn from all learning samples
				for (int j = 0; j < learningSamples.size(); j++) {
					//Set inputs for this sample
					currentSample = learningSamples.get(j);
					for (int k = 0; k < sources.size() - 1; k++) {
						sources.get(k).setOutput(currentSample.get(k));
					}

					//Set desired outputs
					for (int k = sources.size(); k < currentSample.size(); k++) {
						outputNeurons.get(k - sources.size()).setDesiredOutput(currentSample.get(k));
					}

					//Modify the weights and biases
					for (Neuron n : allNeurons) {
						List<Double> newWeights = new ArrayList<>();
						double newBias;

						List<Double> previousWeights = n.getAllWeights();
						double previousBias = previousWeights.get(previousWeights.size() - 1);
						previousWeights.remove(previousWeights.size() - 1);

						for (int k = 0; k < previousWeights.size(); k++){
							newWeights.add(previousWeights.get(k) +  2 * mu * n.getDelta() * n.getInputAt(k));
						}

						printDoubleList(n.getDerivates());

						newBias = previousBias + 2 * mu * n.getDelta();
						n.setWeights(newWeights,newBias);
					}

					for (Neuron n : allNeurons){
						n.markDirty();
					}
				}

				double sum = 0;

				//Get error square sum from validation samples
				for (int j = 0; j < validationSamples.size(); j++) {
					//Set inputs for this sample
					currentSample = validationSamples.get(j);
					for (int k = 0; k < sources.size() - 1; k++) {
						sources.get(k).setOutput(currentSample.get(k));
					}

					//Set desired outputs
					for (int k = sources.size(); k < currentSample.size(); k++) {
						outputNeurons.get(k - sources.size()).setDesiredOutput(currentSample.get(k));
					}



					for (OutputNeuron n : outputNeurons) {
						sum+=n.getDelta()*n.getDelta();
					}
				}

				System.out.println(sum);
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
}
