package init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Initialization {

    private static List<Double> iterateThroughOutputsInInput(double[] inputs) {
        ArrayList<Double> ret = new ArrayList<>();
        for(int i = 0; i < inputs.length; i++) {
            ret.add(inputs[i]);
        }
        return ret;
    }

    private static Map<Integer, List<Double>> iterateThroughWeightsInOutput(double[][] weights) {
        Map<Integer, List<Double>> weightInOutputMap = new HashMap<>();

        for(int i = 0; i < weights.length; i++) {
            List<Double> weightInTemp = new ArrayList<>();
            for(int j = 0; j < weights[i].length; j++) {
                weightInTemp.add(weights[i][j]);
            }
            weightInOutputMap.put(i, weightInTemp);
        }

        return weightInOutputMap;
    }

    private static List<Map<Integer, List<Double>>> iterateThroughWeightsInHidden(double[][][] weights) {
        List<Map<Integer, List<Double>>> weightInHiddenMap = new ArrayList<>();
        for(int i = 0; i < weights.length; i++) {
            Map<Integer, List<Double>> tempMap = new HashMap<>();
            for (int j = 0; j < weights[i].length; j++) {
                List<Double> weightInTemp = new ArrayList<>();
                for (int k = 0; k < weights[i][j].length; k++) {
                    weightInTemp.add(weights[i][j][k]);
                }
                tempMap.put(j, weightInTemp);
            }
            weightInHiddenMap.add(tempMap);
        }
        return weightInHiddenMap;
    }

    /**
     * Initializes weights with two input neurons, two neurons on one hidden layer and one output neuron
     * @return
     * Weights
     * @see Weights
     */
    public static Weights initPreInitializedTwoInputTwoHiddenOneOutputNeuron() {
        List<Double> outputsInInput;
        Map<Integer, List<Double>> weightInOutputMap;
        List<Map<Integer, List<Double>>> weightInHiddenMap;

        // We have two input neurons in addition to the BIAS. That means, each of them should have an
        // input value (it's also the output value at the same time). We initialize it here.
        double[] tempIn = { 0.3, 0.2 };
        outputsInInput = iterateThroughOutputsInInput(tempIn);

        // We have one output neurons. Every neuron contains three input weights.
        double[][] tempOut = new double[][] {
                {0.2, 0.4, 0.3}, //Input weights of first output neuron
        };
        weightInOutputMap = iterateThroughWeightsInOutput(tempOut);

        // We have one hidden layer which contains two neurons. Every neuron has three input weights
        double[][][] tempHidden = new double[][][] {
                {
                        {0.3, 0.8, 0.5}, {-0.2, -0.6, 0.7},
                }
        };

        weightInHiddenMap = iterateThroughWeightsInHidden(tempHidden);

        return new Weights(outputsInInput, weightInOutputMap, weightInHiddenMap);
    }

    /**
     * Initializes weights with two input neurons, two neurons on one hidden layer and one output neuron
     * @return
     * Weights
     * @see Weights
     */
    public static Weights initPreInitializedTwoInputTwoHiddenTwoOutputNeurons() {
        List<Double> outputsInInput;
        Map<Integer, List<Double>> weightInOutputMap;
        List<Map<Integer, List<Double>>> weightInHiddenMap;

        // We have two input neurons in addition to the BIAS. That means, each of them should have an
        // input value (it's also the output value at the same time). We initialize it here.
        double[] tempIn = { 0.3, 0.2 };
        outputsInInput = iterateThroughOutputsInInput(tempIn);

        // We have one output neurons. Every neuron contains three input weights.
        double[][] tempOut = new double[][] {
                {0.2, 0.4, 0.3}, {0.1, 0.3, 0.6} //Input weights of first output neuron
        };
        weightInOutputMap = iterateThroughWeightsInOutput(tempOut);

        // We have one hidden layer which contains two neurons. Every neuron has three input weights
        double[][][] tempHidden = new double[][][] {
                {
                        {0.3, 0.8, 0.5}, {-0.2, -0.6, 0.7},
                }
        };

        weightInHiddenMap = iterateThroughWeightsInHidden(tempHidden);

        return new Weights(outputsInInput, weightInOutputMap, weightInHiddenMap);
    }

    /**
     * Initializes the network with pre-defined weights in order to test.
     * @return
     * Weights
     * @see Weights
     */
    public static Weights initTwoInputTwoHiddenTwoOutputNeurons() {
        List<Double> outputsInInput;
        Map<Integer, List<Double>> weightInOutputMap;
        List<Map<Integer, List<Double>>> weightInHiddenMap;

        // We have two input neurons in addition to the BIAS. That means, each of them should have an
        // input value (it's also the output value at the same time). We initialize it here.
        double[] tempIn = { 0.3, 0.2 };
        outputsInInput = iterateThroughOutputsInInput(tempIn);

        // We have two output neurons. Every neuron contains three input weights.
        double[][] tempOut = new double[][] {
                {0.2, 0.4, 0.3}, //Input weights of first output neuron
                {0.1, -0.4, 0.9} //Input weights of second output neuron
        };
        weightInOutputMap = iterateThroughWeightsInOutput(tempOut);

        // We have one hidden layer which contains two neurons. Every neuron has three input weights
        double[][][] tempHidden = new double[][][] {
                {
                        {0.3, 0.8, 0.5}, {-0.2, -0.6, 0.7},
                }
        };
        weightInHiddenMap = iterateThroughWeightsInHidden(tempHidden);

        return new Weights(outputsInInput, weightInOutputMap, weightInHiddenMap);
    }

    public static Weights initTwoInputTwoHiddenTwoOutputNeuronsTwoHiddenLayers() {
        List<Double> outputsInInput;
        Map<Integer, List<Double>> weightInOutputMap;
        List<Map<Integer, List<Double>>> weightInHiddenMap;

        // We have two input neurons in addition to the BIAS. That means, each of them should have an
        // input value (it's also the output value at the same time). We initialize it here.
        double[] tempIn = { 0.3, 0.2 };
        outputsInInput = iterateThroughOutputsInInput(tempIn);

        // We have two output neurons. Every neuron contains three input weights.
        double[][] tempOut = new double[][] {
                {0.2, 0.4, 0.3}, //Input weights of first output neuron
                {0.1, -0.4, 0.9} //Input weights of second output neuron
        };
        weightInOutputMap = iterateThroughWeightsInOutput(tempOut);

        // We have two hidden layers which contain two neurons each. Every neuron has three input weights
        double[][][] tempHidden = new double[][][] {
                {
                        {0.3, 0.8, 0.5}, {-0.2, -0.6, 0.7},
                },
                {
                        {0.1, 0.6, -0.9}, {0.4, -0.3, -0.2}
                }
        };
        weightInHiddenMap = iterateThroughWeightsInHidden(tempHidden);

        return new Weights(outputsInInput, weightInOutputMap, weightInHiddenMap);
    }

    public static Weights initTwoInputTwoHiddenTwoOutputNeuronsThreeHiddenLayers() {
        List<Double> outputsInInput;
        Map<Integer, List<Double>> weightInOutputMap;
        List<Map<Integer, List<Double>>> weightInHiddenMap;

        // We have two input neurons in addition to the BIAS. That means, each of them should have an
        // input value (it's also the output value at the same time). We initialize it here.
        double[] tempIn = { 0.3, 0.2 };
        outputsInInput = iterateThroughOutputsInInput(tempIn);

        // We have two output neurons. Every neuron contains three input weights.
        double[][] tempOut = new double[][] {
                {0.2, 0.4, 0.3}, //Input weights of first output neuron
                {0.1, -0.4, 0.9} //Input weights of second output neuron
        };
        weightInOutputMap = iterateThroughWeightsInOutput(tempOut);

        // We have two hidden layers which contain two neurons each. Every neuron has three input weights
        double[][][] tempHidden = new double[][][] {
                {
                        {0.3, 0.8, 0.5}, {-0.2, -0.6, 0.7},
                },
                {
                        {0.1, 0.6, -0.9}, {0.4, -0.3, -0.2}
                },
                {
                        {0.2, 0.1, -0.5}, {-0.5, 0.4, 0.9}
                }
        };
        weightInHiddenMap = iterateThroughWeightsInHidden(tempHidden);

        return new Weights(outputsInInput, weightInOutputMap, weightInHiddenMap);
    }
}
