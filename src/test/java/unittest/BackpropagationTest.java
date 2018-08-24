package unittest;

import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.application.training.ActivationFunctionType;
import de.nitschmann.tefdnn.application.training.TrainingType;
import init.Initialization;
import init.Weights;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class BackpropagationTest {

    private Weights weights;

    /**
     * Tests the forwardpass through the network with two input neurons, two hidden neurons on one hidden layer
     * and two output neurons
     */
    @Test
    public void testForwardTwoInputTwoHiddenTwoOutputNeuronsOneHiddenLayer() {
        this.weights = Initialization.initTwoInputTwoHiddenTwoOutputNeurons();
        NeuralNetwork nn = new NeuralNetwork();
        // initialize the network with weights
        nn.initNetwork(weights.getOutputsInInput(), weights.getWeightInHidden(), weights.getWeightInOutput());
        // set training type to backpropagation
        nn.setTrainingType(TrainingType.BACKPROPAGATION);
        nn.setActivationFunction(ActivationFunctionType.SIGMOID);
        // important! Sets the output value of the input neurons
        nn.setInput(nn, new double[] {0.7, 0.6});
        // doesn't really matter what values we give but it should be the same length as the number of output neurons
        // it's basically needed for calculating the error. since we don't need to calculate an error in testing environment,
        // we just pseudo initialize it.
        double[][] estimatedValues = new double[][] {{ 0.0, 0.0 }};
        nn.setEstimatedResults(estimatedValues);
        ArrayList<Double> results = nn.test(nn);
        ArrayList<Double> expected = new ArrayList<>();
        expected.add(0.6546591654993577);
        expected.add(0.5499865411657244);
        Assert.assertArrayEquals(expected.toArray(), results.toArray());
    }

    /**
     * Tests the forwardpass through the network with two input neurons, two hidden neurons on one hidden layer
     * and two output neurons
     */
    @Test
    public void testForwardTwoInputTwoHiddenTwoOutputNeuronsTwoHiddenLayer() {
        this.weights = Initialization.initTwoInputTwoHiddenTwoOutputNeuronsTwoHiddenLayers();
        NeuralNetwork nn = new NeuralNetwork();
        // initialize the network with weights
        nn.initNetwork(weights.getOutputsInInput(), weights.getWeightInHidden(), weights.getWeightInOutput());
        // set training type to backpropagation
        nn.setTrainingType(TrainingType.BACKPROPAGATION);
        nn.setActivationFunction(ActivationFunctionType.SIGMOID);
        nn.setLearningRate(1.0);
        // important! Sets the output value of the input neurons
        nn.setInput(nn, new double[] {0.7, 0.6});
        // doesn't really matter what values we give but it should be the same length as the number of output neurons
        // it's basically needed for calculating the error. since we don't need to calculate an error in testing environment,
        // we just pseudo initialize it.
        double[][] estimatedValues = new double[][] {{ 0.0, 0.0 }};
        nn.setEstimatedResults(estimatedValues);
        ArrayList<Double> results = nn.test(nn);
        ArrayList<Double> expected = new ArrayList<>();
        expected.add(0.6390515930232902);
        expected.add(0.5873949005156732);
        Assert.assertArrayEquals(expected.toArray(), results.toArray());
    }

    /**
     * Tests the forwardpass through the network with two input neurons, two hidden neurons on one hidden layer
     * and two output neurons
     */
    @Test
    public void testForwardTwoInputTwoHiddenTwoOutputNeuronsThreeHiddenLayer() {
        this.weights = Initialization.initTwoInputTwoHiddenTwoOutputNeuronsThreeHiddenLayers();
        NeuralNetwork nn = new NeuralNetwork();
        // initialize the network with weights
        nn.initNetwork(weights.getOutputsInInput(), weights.getWeightInHidden(), weights.getWeightInOutput());
        // set training type to backpropagation
        nn.setTrainingType(TrainingType.BACKPROPAGATION);
        nn.setActivationFunction(ActivationFunctionType.SIGMOID);
        nn.setLearningRate(1.0);
        // important! Sets the output value of the input neurons
        nn.setInput(nn, new double[] {0.7, 0.6});
        // doesn't really matter what values we give but it should be the same length as the number of output neurons
        // it's basically needed for calculating the error. since we don't need to calculate an error in testing environment,
        // we just pseudo initialize it.
        double[][] estimatedValues = new double[][] {{ 0.0, 0.0 }};
        nn.setEstimatedResults(estimatedValues);
        ArrayList<Double> results = nn.test(nn);
        ArrayList<Double> expected = new ArrayList<>();
        expected.add(0.6371653062381484);
        expected.add(0.5967178245722511);
        Assert.assertArrayEquals(expected.toArray(), results.toArray());
    }

    /**
     * Not yet done
     */
    @Test
    public void testBackprop() {
        this.weights = Initialization.initTwoInputTwoHiddenTwoOutputNeurons();
        NeuralNetwork nn = new NeuralNetwork();
        nn.initNetwork(weights.getOutputsInInput(), weights.getWeightInHidden(), weights.getWeightInOutput());
        nn.setTrainingType(TrainingType.BACKPROPAGATION);
        nn.setActivationFunction(ActivationFunctionType.SIGMOID);
        nn.setLearningRate(0.3);
        nn.setMomentum(0.5);
        nn.setMaxEpoch(0);
        nn.setTargetLoss(0.1);
        nn.setInput(nn, new double[] {0.7, 0.6});
        double[][] trainingData = new double[][] {
                {1.0, 1.0},
                {1.0, 1.0},
                {1.0, 0.0}
        };
        double[][] estimatedValues = new double[][] {
                {0.0, 1.0},
                {0.0, 1.0},
                {1.0, 0.0}
        };
        nn.setTrainSet(trainingData);
        nn.setEstimatedResults(estimatedValues);
        nn.setTrainingType(TrainingType.BACKPROPAGATION);

        NeuralNetwork trainedNet = nn.train(nn);
        boolean newWeightsAreOkay = true;
        double[][] estimatedWeightOutsInputLayer = new double[][] {
                {0.2919152244942841, -0.19417607788110997},
                {0.7939286873560839, -0.5977559729258883},
                {0.4899017616324842, 0.7094038171636686}
        };

        double[][] estimatedWeightInsHiddenLayer = new double[][] {
                {0, 0, 0}, // BIAS Neuron has no inputs
                {0.2919152244942841, 0.7939286873560839, 0.4899017616324842},
                {-0.19417607788110997, -0.5977559729258883, 0.7094038171636686}
        };

        double[][] estimatedWeightOutsHiddenLayer = new double[][] {
                {0.11959226882646905, 0.1392353717483391},
                {0.3186424521579064, -0.36494076586584906},
                {0.24110464420360836, 0.9296154433374304}
        };

        double[][] estimatedWeightInsOutputLayer = new double[][] {
                {0.11959226882646905, 0.3186424521579064, 0.24110464420360836},
                {0.1392353717483391, -0.36494076586584906, 0.9296154433374304}
        };
        for (int i = 0; i < trainedNet.getInputLayer().getNeurons().size(); i++) {
            for (int j = 0; j < trainedNet.getInputLayer().getNeurons().get(i).getWeightOut().size(); j++) {
                if (trainedNet.getInputLayer().getNeurons().get(i).getWeightOut().get(j) != estimatedWeightOutsInputLayer[i][j]) {
                    newWeightsAreOkay = false;
                }
            }
        }

        for (int i = 0; i < trainedNet.getHiddenLayers().get(0).getNeurons().size(); i++) {
            for (int j = 0; j < trainedNet.getHiddenLayers().get(0).getNeurons().get(i).getWeightIn().size(); j++) {
                if (trainedNet.getHiddenLayers().get(0).getNeurons().get(i).getWeightIn().get(j) != estimatedWeightInsHiddenLayer[i][j]) {
                    newWeightsAreOkay = false;
                }
            }
        }

        for (int i = 0; i < trainedNet.getHiddenLayers().get(0).getNeurons().size(); i++) {
            for (int j = 0; j < trainedNet.getHiddenLayers().get(0).getNeurons().get(i).getWeightOut().size(); j++) {
                if (trainedNet.getHiddenLayers().get(0).getNeurons().get(i).getWeightOut().get(j) != estimatedWeightOutsHiddenLayer[i][j]) {
                    newWeightsAreOkay = false;
                }
            }
        }

        for (int i = 0; i < trainedNet.getOutputLayer().getNeurons().size(); i++) {
            for (int j = 0; j < trainedNet.getOutputLayer().getNeurons().get(i).getWeightIn().size(); j++) {
                if (trainedNet.getOutputLayer().getNeurons().get(i).getWeightIn().get(j) != estimatedWeightInsOutputLayer[i][j]) {
                    newWeightsAreOkay = false;
                }
            }
        }

        Assert.assertEquals(true, newWeightsAreOkay);

    }

}
