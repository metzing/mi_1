package NNSolutionFive;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NNSolutionFive {
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
			BufferedReader br = new BufferedReader(new FileReader("input.txt"));

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
				String[] weightStrings = br.readLine().split("\t");
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
				String[] currentSampleString = br.readLine().split("\t");
				currentSample = new ArrayList<>();
				for (int j = 0; j < currentSampleString.length; j++) {
					currentSample.add(Double.parseDouble(currentSampleString[j]));
				}
				learningSamples.add(currentSample);
			}

			//Store validation samples
			List<List<Double>> validationSamples = new ArrayList<>();

			for (int i = 0; i < Math.ceil(numberOfSamples * (1 - learningSampleRatio)); i++) {
				String[] currentSampleString = br.readLine().split("\t");
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
						outputNeurons.get(k).setDesiredOutput(currentSample.get(k + sources.size()));
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

				//System.out.println(errorSqrSum / (validationSamples.size() * outputNeurons.size()));
			}

			OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream("nn_solution_five.txt"));

			//Finally...
			ow.write(architecture + System.getProperty("line.separator"));


			for (Neuron n : allNeurons) {
				printDoubleList(n.getAllWeights(),ow);
			}

			ow.flush();


		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void printDoubleList(List<Double> list, OutputStreamWriter bw) throws IOException {
		for (int i = 0; i < list.size(); i++) {
			bw.write(list.get(i).toString());
			if (i != list.size() - 1) bw.write(",");
		}
		bw.write(System.getProperty("line.separator"));
	}
}
