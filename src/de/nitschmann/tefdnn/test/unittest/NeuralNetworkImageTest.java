package de.nitschmann.tefdnn.test.unittest;

import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.application.io.ImageLoader;
import de.nitschmann.tefdnn.application.io.TrainingData;
import de.nitschmann.tefdnn.application.training.ActivationFunctionType;
import de.nitschmann.tefdnn.application.training.TrainingType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.nio.file.Paths;
import java.util.*;

public class NeuralNetworkImageTest {

    /* paths for pkw vs bus vs minibus test */
    private String busPathTraining;
    private String busPathTest;
    private String pkwPathTraining;
    private String pkwPathTest;
    private String minibusPathTraining;
    private String minibusPathTest;

    /* paths for mnist test */
    private String mnistPathToZeroesTraining;
    private String mnistPathToOnesTraining;
    private String mnistPathToTwosTraining;
    private String mnistPathToThreesTraining;
    private String mnistPathToFoursTraining;
    private String mnistPathToFivesTraining;
    private String mnistPathToSixsTraining;
    private String mnistPathToSevensTraining;
    private String mnistPathToEightsTraining;
    private String mnistPathToNinesTraining;

    private String mnistPathToZeroesTest;
    private String mnistPathToOnesTest;
    private String mnistPathToTwosTest;
    private String mnistPathToThreesTest;
    private String mnistPathToFoursTest;
    private String mnistPathToFivesTest;
    private String mnistPathToSixsTest;
    private String mnistPathToSevensTest;
    private String mnistPathToEightsTest;
    private String mnistPathToNinesTest;

    /**
     * Initialize paths for buses, minibuses, PKWs and digits for both training and testing.
     */
    @Before
    public void initializePaths() {
        busPathTraining = Paths.get("data/bus/Training").toAbsolutePath().toString();
        pkwPathTraining = Paths.get("data/pkw/Training").toAbsolutePath().toString();
        minibusPathTraining = Paths.get("data/minibus/Training").toAbsolutePath().toString();
        busPathTest = Paths.get("data/bus/Test").toAbsolutePath().toString();
        pkwPathTest = Paths.get("data/pkw/Test").toAbsolutePath().toString();
        minibusPathTest = Paths.get("data/minibus/Test").toAbsolutePath().toString();

        mnistPathToZeroesTraining = Paths.get("data/mnist/training/0").toAbsolutePath().toString();
        mnistPathToOnesTraining = Paths.get("data/mnist/training/1").toAbsolutePath().toString();
        mnistPathToTwosTraining = Paths.get("data/mnist/training/2").toAbsolutePath().toString();
        mnistPathToThreesTraining = Paths.get("data/mnist/training/3").toAbsolutePath().toString();
        mnistPathToFoursTraining = Paths.get("data/mnist/training/4").toAbsolutePath().toString();
        mnistPathToFivesTraining = Paths.get("data/mnist/training/5").toAbsolutePath().toString();
        mnistPathToSixsTraining = Paths.get("data/mnist/training/6").toAbsolutePath().toString();
        mnistPathToSevensTraining = Paths.get("data/mnist/training/7").toAbsolutePath().toString();
        mnistPathToEightsTraining = Paths.get("data/mnist/training/8").toAbsolutePath().toString();
        mnistPathToNinesTraining = Paths.get("data/mnist/training/9").toAbsolutePath().toString();

        mnistPathToZeroesTest = Paths.get("data/mnist/test/0").toAbsolutePath().toString();
        mnistPathToOnesTest = Paths.get("data/mnist/test/1").toAbsolutePath().toString();
        mnistPathToTwosTest = Paths.get("data/mnist/test/2").toAbsolutePath().toString();
        mnistPathToThreesTest = Paths.get("data/mnist/test/3").toAbsolutePath().toString();
        mnistPathToFoursTest = Paths.get("data/mnist/test/4").toAbsolutePath().toString();
        mnistPathToFivesTest = Paths.get("data/mnist/test/5").toAbsolutePath().toString();
        mnistPathToSixsTest = Paths.get("data/mnist/test/6").toAbsolutePath().toString();
        mnistPathToSevensTest = Paths.get("data/mnist/test/7").toAbsolutePath().toString();
        mnistPathToEightsTest = Paths.get("data/mnist/test/8").toAbsolutePath().toString();
        mnistPathToNinesTest = Paths.get("data/mnist/test/9").toAbsolutePath().toString();
    }

    /**
     * Tests if the correct number was recognized of the mnist dataset.
     * @param actualNumber actual number, which is also the index in the result set
     * @param result result set which shows the percentage of the numbers
     * @return true if the index "actualNumber" in the result set has the highest value
     */
    private boolean isCorrectRecognizedNumber(int actualNumber, List<Double> result) {
        for(int i = 0; i < result.size(); i++) {
            if (i != actualNumber) {
                if (result.get(actualNumber) < result.get(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets the quote of mnist tests. If 5 out of 10 numbers get recognized correctly, quote is 50%.
     * @param trained the trained neural network
     * @param testData the data for testing
     * @return quote
     */
    private double getQuote(NeuralNetwork trained, Map<String, double[]> testData) {
        double quote = 0;

        Iterator it = testData.entrySet().iterator();
        int iterations = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String imagePath = pair.getKey().toString();
            trained.setInput(trained, (double[]) pair.getValue());
            List<Double> result = trained.test(trained);
            System.out.printf("Image path: %s\t\tresult: %s\n", imagePath, result.toString());

            if (imagePath.contains(mnistPathToZeroesTest)) {
                quote += (isCorrectRecognizedNumber(0, result)) ? 1 : 0;
            } else if (imagePath.contains(mnistPathToOnesTest)) {
                quote += (isCorrectRecognizedNumber(1, result)) ? 1 : 0;
            } else if (imagePath.contains(mnistPathToTwosTest)) {
                quote += (isCorrectRecognizedNumber(2, result)) ? 1 : 0;
            } else if (imagePath.contains(mnistPathToThreesTest)) {
                quote += (isCorrectRecognizedNumber(3, result)) ? 1 : 0;
            } else if (imagePath.contains(mnistPathToFoursTest)) {
                quote += (isCorrectRecognizedNumber(4, result)) ? 1 : 0;
            } else if (imagePath.contains(mnistPathToFivesTest)) {
                quote += (isCorrectRecognizedNumber(5, result)) ? 1 : 0;
            } else if (imagePath.contains(mnistPathToSixsTest)) {
                quote += (isCorrectRecognizedNumber(6, result)) ? 1 : 0;
            } else if (imagePath.contains(mnistPathToSevensTest)) {
                quote += (isCorrectRecognizedNumber(7, result)) ? 1 : 0;
            } else if (imagePath.contains(mnistPathToEightsTest)) {
                quote += (isCorrectRecognizedNumber(8, result)) ? 1 : 0;
            } else if (imagePath.contains(mnistPathToNinesTest)) {
                quote += (isCorrectRecognizedNumber(9, result)) ? 1 : 0;
            }
            iterations++;
        }

        System.out.printf("Quote %f\n", (quote / iterations));
        return quote / iterations;
    }

    /**
     * Checks if the mnist dataset gets recognized correctly with 90 % accuracy.
     * We use two hidden layers here with each 20 neurons on it.
     */
    @Test
    public void imageMnistTestRandomNetworkTwoHiddenLayer() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.initNetwork(784, 20, 10, 2);

        ImageLoader imageLoader = new ImageLoader(nn);
        imageLoader.addTrainingSet(mnistPathToZeroesTraining, 0);
        imageLoader.addTrainingSet(mnistPathToOnesTraining, 1);
        imageLoader.addTrainingSet(mnistPathToTwosTraining, 2);
        imageLoader.addTrainingSet(mnistPathToThreesTraining, 3);
        imageLoader.addTrainingSet(mnistPathToFoursTraining, 4);
        imageLoader.addTrainingSet(mnistPathToFivesTraining, 5);
        imageLoader.addTrainingSet(mnistPathToSixsTraining, 6);
        imageLoader.addTrainingSet(mnistPathToSevensTraining, 7);
        imageLoader.addTrainingSet(mnistPathToEightsTraining, 8);
        imageLoader.addTrainingSet(mnistPathToNinesTraining, 9);

        imageLoader.addTestPath(mnistPathToZeroesTest);
        imageLoader.addTestPath(mnistPathToOnesTest);
        imageLoader.addTestPath(mnistPathToTwosTest);
        imageLoader.addTestPath(mnistPathToThreesTest);
        imageLoader.addTestPath(mnistPathToFoursTest);
        imageLoader.addTestPath(mnistPathToFivesTest);
        imageLoader.addTestPath(mnistPathToSixsTest);
        imageLoader.addTestPath(mnistPathToSevensTest);
        imageLoader.addTestPath(mnistPathToEightsTest);
        imageLoader.addTestPath(mnistPathToNinesTest);

        TrainingData trainingData = imageLoader.getTrainingData();
        Map<String, double[]> testData = imageLoader.getTestImages(trainingData.getMeanImage());

        nn.setTrainSet(trainingData.getImages());
        nn.setEstimatedResults(trainingData.getEstimatedResults());
        nn.setTrainingType(TrainingType.BACKPROPAGATION);
        nn.setLearningRate(0.01);
        nn.setMomentum(0.95);
        nn.setTargetLoss(0.05);
        nn.setMaxEpoch(4000);
        nn.setActivationFunction(ActivationFunctionType.SIGMOID);

        NeuralNetwork trained = nn.train(nn);

        Assert.assertTrue(getQuote(trained, testData) >= 0.9);
    }

    /**
     * Checks if the mnist dataset gets recognized correctly with 90 % accuracy.
     * We use one hidden layer here with 20 neurons on it.
     */
    @Test
    public void imageMnistTestRandomNetworkOneHiddenLayer() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.initNetwork(784, 20, 10, 1);

        ImageLoader imageLoader = new ImageLoader(nn);
        imageLoader.addTrainingSet(mnistPathToZeroesTraining, 0);
        imageLoader.addTrainingSet(mnistPathToOnesTraining, 1);
        imageLoader.addTrainingSet(mnistPathToTwosTraining, 2);
        imageLoader.addTrainingSet(mnistPathToThreesTraining, 3);
        imageLoader.addTrainingSet(mnistPathToFoursTraining, 4);
        imageLoader.addTrainingSet(mnistPathToFivesTraining, 5);
        imageLoader.addTrainingSet(mnistPathToSixsTraining, 6);
        imageLoader.addTrainingSet(mnistPathToSevensTraining, 7);
        imageLoader.addTrainingSet(mnistPathToEightsTraining, 8);
        imageLoader.addTrainingSet(mnistPathToNinesTraining, 9);

        imageLoader.addTestPath(mnistPathToZeroesTest);
        imageLoader.addTestPath(mnistPathToOnesTest);
        imageLoader.addTestPath(mnistPathToTwosTest);
        imageLoader.addTestPath(mnistPathToThreesTest);
        imageLoader.addTestPath(mnistPathToFoursTest);
        imageLoader.addTestPath(mnistPathToFivesTest);
        imageLoader.addTestPath(mnistPathToSixsTest);
        imageLoader.addTestPath(mnistPathToSevensTest);
        imageLoader.addTestPath(mnistPathToEightsTest);
        imageLoader.addTestPath(mnistPathToNinesTest);

        TrainingData trainingData = imageLoader.getTrainingData();
        Map<String, double[]> testData = imageLoader.getTestImages(trainingData.getMeanImage());

        nn.setTrainSet(trainingData.getImages());
        nn.setEstimatedResults(trainingData.getEstimatedResults());
        nn.setTrainingType(TrainingType.BACKPROPAGATION);
        nn.setLearningRate(0.01);
        nn.setMomentum(0.95);
        nn.setTargetLoss(0.05);
        nn.setMaxEpoch(4000);
        nn.setActivationFunction(ActivationFunctionType.SIGMOID);

        NeuralNetwork trained = nn.train(nn);

        Assert.assertTrue(getQuote(trained, testData) >= 0.9);
    }

    /**
     * This is the test which is for the POC. We test if we can successfully separate buses from PKWs from minibuses.
     * We train neuron 0 with buses, neuron 1 with PKWs and neuron 2 with minibuses. After training, we check if the
     * correct neurons fire.
     */
    @Test
    public void busVsPkwVsMinibusTest() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.initNetwork(784, 40, 3, 1);

        ImageLoader imageLoader = new ImageLoader(nn);
        imageLoader.addTrainingSet(busPathTraining, 0);
        imageLoader.addTrainingSet(pkwPathTraining, 1);
        imageLoader.addTrainingSet(minibusPathTraining, 2);
        imageLoader.addTestPath(busPathTest);
        imageLoader.addTestPath(pkwPathTest);
        imageLoader.addTestPath(minibusPathTest);

        TrainingData trainingData = imageLoader.getTrainingData();
        Map<String, double[]> testData = imageLoader.getTestImages(trainingData.getMeanImage());

        nn.setTrainSet(trainingData.getImages());
        nn.setEstimatedResults(trainingData.getEstimatedResults());
        nn.setTrainingType(TrainingType.BACKPROPAGATION);
        nn.setLearningRate(0.01);
        nn.setMomentum(0.95);
        nn.setTargetLoss(0.0005);
        nn.setMaxEpoch(4000);
        nn.setActivationFunction(ActivationFunctionType.SIGMOID);

        NeuralNetwork trained = nn.train(nn);

        Iterator it = testData.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String imagePath = pair.getKey().toString();
            trained.setInput(trained, (double[]) pair.getValue());
            List<Double> result = trained.test(trained);
            System.out.printf("Image path: %s\t\tresult: %s\n", imagePath, result.toString());

            if (imagePath.contains(busPathTest)) {
                // Neuron 0 was trained, so we check if the neuron with index 0 has the highest value
                Assert.assertTrue(result.get(0) > result.get(1) && result.get(0) > result.get(2));
            } else if (imagePath.contains(pkwPathTest)) {
                // Neuron 1 was trained, so we check if the neuron with index 1 has the highest value
                Assert.assertTrue(result.get(1) > result.get(0) && result.get(1) > result.get(2));
            } else if (imagePath.contains(minibusPathTest)) {
                // Neuron 2 was trained, so we check if the neuron with index 2 has the highest value
                Assert.assertTrue(result.get(2) > result.get(0) && result.get(2) > result.get(1));
            }
        }
    }
}

