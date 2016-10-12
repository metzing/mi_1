package NNSolutionOne;

import java.io.*;
import java.util.*;

public class NNSolutionOne {

	public static void main(String[] args) {
		try {
			//<read>
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String input = br.readLine();

			String[] inputValues = input.split(",");

			int InputCount = Integer.parseInt(inputValues[0]),
					OutputCount = Integer.parseInt((inputValues[inputValues.length - 1]));

			List<Integer> hiddenLayersCounts = new ArrayList<>();

			for (int i = 1; i < inputValues.length - 1; i++) {
				hiddenLayersCounts.add(Integer.parseInt(inputValues[i]));
			}
			//</read>
			//<write>
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
			bw.write(InputCount + ",");
			for (int item : hiddenLayersCounts) {
				bw.write(item + ",");
			}
			bw.write(OutputCount + "\n");

			int previousLayerCount = InputCount;
			Random r = new Random();
			for (int i = 0; i < hiddenLayersCounts.size(); i++) {
				for (int j = 0; j < hiddenLayersCounts.get(i); j++) {
					for (int k = 0; k < previousLayerCount; k++) {
						bw.write(r.nextGaussian() * (0.1) + ", ");
					}
					bw.write("0.0\n");
				}
				previousLayerCount = hiddenLayersCounts.get(i);
			}
			for (int i = 0; i < OutputCount; i++) {
				for (int j = 0; j < previousLayerCount; j++){
					bw.write(r.nextGaussian() * (0.1) + ", ");
				}
				bw.write("0.0\n");
			}
			bw.flush();
			//</write>
		} catch (Exception e) {
			System.err.println(e.toString());
			e.printStackTrace();
		}
	}
}
