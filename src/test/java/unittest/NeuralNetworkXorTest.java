package unittest;

import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.application.training.ActivationFunctionType;
import de.nitschmann.tefdnn.application.training.TrainingType;
import init.Initialization;
import init.Weights;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class NeuralNetworkXorTest {

    private ArrayList<double[][]> getTrainingDataForXor() {
        ArrayList<double[][]> trainingDataAndEstimatedResults = new ArrayList<>();

        double[][] trainingData = new double[4][];
        double[][] estimatedValues = new double[4][];

        trainingData[0] = new double[]{0.0, 0.0};
        trainingData[1] = new double[]{1.0, 0.0};
        trainingData[2] = new double[]{0.0, 1.0};
        trainingData[3] = new double[]{1.0, 1.0};

        estimatedValues[0] = new double[]{0.0, 1.0};
        estimatedValues[1] = new double[]{1.0, 0.0};
        estimatedValues[2] = new double[]{1.0, 0.0};
        estimatedValues[3] = new double[]{0.0, 1.0};

        trainingDataAndEstimatedResults.add(trainingData);
        trainingDataAndEstimatedResults.add(estimatedValues);

        return trainingDataAndEstimatedResults;
    }

    /**
     * Test is okay if a neural network with 2 input neurons, 10 hidden layer neurons and 1 output layer neuron can solve the xor problem.
     * We can solve it if we reach at least 99% Accuracy. Sometimes we can stuck in a local minimum. If thats the case, we try it again up
     * to 5 times.
     */
    @Test
    public void xorRandomNetworkOneLayer() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.initNetwork(2, 10, 2, 1);

        ArrayList<double[][]> trainingData = getTrainingDataForXor();

        nn.setTrainingType(TrainingType.BACKPROPAGATION);
        nn.setActivationFunction(ActivationFunctionType.SIGMOID);
        nn.setTrainSet(trainingData.get(0));
        nn.setMaxEpoch(20000);
        nn.setTargetLoss(0.005);
        nn.setLearningRate(1);
        nn.setMomentum(0.95);
        nn.setEstimatedResults(trainingData.get(1));

        NeuralNetwork trainedNet = nn.train(nn);

        trainedNet.setInput(nn, new double[]{1.0, 1.0});
        ArrayList<Double> res1 = trainedNet.test(nn);

        trainedNet.setInput(nn, new double[]{0.0, 0.0});
        ArrayList<Double> res2 = trainedNet.test(nn);

        trainedNet.setInput(nn, new double[]{0.0, 1.0});
        ArrayList<Double> res3 = trainedNet.test(nn);

        trainedNet.setInput(nn, new double[]{1.0, 0.0});
        ArrayList<Double> res4 = trainedNet.test(nn);

        Assert.assertTrue(res1.get(0) < 0.2);
        Assert.assertTrue(res1.get(1) > 0.8);

        Assert.assertTrue(res2.get(0) < 0.2);
        Assert.assertTrue(res2.get(1) > 0.8);

        Assert.assertTrue(res3.get(0) > 0.8);
        Assert.assertTrue(res3.get(1) < 0.2);

        Assert.assertTrue(res4.get(0) > 0.8);
        Assert.assertTrue(res4.get(1) < 0.2);
    }

    /**
     * Test is okay if a neural network with 2 input neurons, 2 hidden layer neurons and 1 output layer neuron can solve the xor problem.
     * We can solve it if we reach at least 99% Accuracy. Sometimes we can stuck in a local minimum. If thats the case, we try it again up
     * to 5 times.
     */
    @Test
    public void xorRandomNetworkTwoLayer() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.initNetwork(2, 10, 2, 2);

        ArrayList<double[][]> trainingData = getTrainingDataForXor();

        nn.setTrainingType(TrainingType.BACKPROPAGATION);
        nn.setActivationFunction(ActivationFunctionType.SIGMOID);
        nn.setTrainSet(trainingData.get(0));
        nn.setLearningRate(1);
        nn.setTargetLoss(0.005);
        nn.setMomentum(0.95);
        nn.setMaxEpoch(200000);
        nn.setEstimatedResults(trainingData.get(1));

        NeuralNetwork trainedNet = nn.train(nn);

        trainedNet.setInput(nn, new double[]{1.0, 1.0});
        ArrayList<Double> res1 = trainedNet.test(nn);

        trainedNet.setInput(nn, new double[]{0.0, 0.0});
        ArrayList<Double> res2 = trainedNet.test(nn);

        trainedNet.setInput(nn, new double[]{0.0, 1.0});
        ArrayList<Double> res3 = trainedNet.test(nn);

        trainedNet.setInput(nn, new double[]{1.0, 0.0});
        ArrayList<Double> res4 = trainedNet.test(nn);

        Assert.assertTrue(res1.get(0) < 0.2);
        Assert.assertTrue(res1.get(1) > 0.8);

        Assert.assertTrue(res2.get(0) < 0.2);
        Assert.assertTrue(res2.get(1) > 0.8);

        Assert.assertTrue(res3.get(0) > 0.8);
        Assert.assertTrue(res3.get(1) < 0.2);

        Assert.assertTrue(res4.get(0) > 0.8);
        Assert.assertTrue(res4.get(1) < 0.2);
    }

    @Test
    public void xorPreInitializedNetworkOneLayer() {
        Weights weights = Initialization.initPreInitializedTwoInputTwoHiddenTwoOutputNeurons();

        NeuralNetwork nn = new NeuralNetwork();
        nn.initNetwork(weights.getOutputsInInput(), weights.getWeightInHidden(), weights.getWeightInOutput());

        ArrayList<double[][]> trainingData = getTrainingDataForXor();

        nn.setTrainingType(TrainingType.BACKPROPAGATION);
        nn.setActivationFunction(ActivationFunctionType.SIGMOID);
        nn.setTrainSet(trainingData.get(0));
        nn.setMaxEpoch(20000);
        nn.setTargetLoss(0.005);
        nn.setMomentum(1);
        nn.setLearningRate(1);
        nn.setEstimatedResults(trainingData.get(1));

        NeuralNetwork trainedNet = nn.train(nn);

        trainedNet.setInput(nn, new double[]{1.0, 1.0});
        ArrayList<Double> res1 = trainedNet.test(nn);

        trainedNet.setInput(nn, new double[]{0.0, 0.0});
        ArrayList<Double> res2 = trainedNet.test(nn);

        trainedNet.setInput(nn, new double[]{0.0, 1.0});
        ArrayList<Double> res3 = trainedNet.test(nn);

        trainedNet.setInput(nn, new double[]{1.0, 0.0});
        ArrayList<Double> res4 = trainedNet.test(nn);

        System.out.println(res1);
        System.out.println(res2);
        System.out.println(res3);
        System.out.println(res4);

        Assert.assertTrue(res1.get(0) < 0.2);
        Assert.assertTrue(res1.get(1) > 0.8);

        Assert.assertTrue(res2.get(0) < 0.2);
        Assert.assertTrue(res2.get(1) > 0.8);

        Assert.assertTrue(res3.get(0) > 0.8);
        Assert.assertTrue(res3.get(1) < 0.2);

        Assert.assertTrue(res4.get(0) > 0.8);
        Assert.assertTrue(res4.get(1) < 0.2);
    }
}

