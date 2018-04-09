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

    private String busPathTraining;
    private String busPathTest;
    private String pkwPathTraining;
    private String pkwPathTest;
    private String minibusPathTraining;
    private String minibusPathTest;

    @Before
    public void initializePaths() {
        /*
        geometricsPathToCircles = Paths.get("data/geometrics/Training/0").toAbsolutePath().toString();
        geometricsPathToTriangles = Paths.get("data/geometrics/Training/1").toAbsolutePath().toString();
        geometricsPathToTests = Paths.get("data/geometrics/Test").toAbsolutePath().toString();
        */

        busPathTraining = Paths.get("data/bus/Training").toAbsolutePath().toString();
        pkwPathTraining = Paths.get("data/pkw/Training").toAbsolutePath().toString();
        minibusPathTraining = Paths.get("data/minibus/Training").toAbsolutePath().toString();
        busPathTest = Paths.get("data/bus/Test").toAbsolutePath().toString();
        pkwPathTest = Paths.get("data/pkw/Test").toAbsolutePath().toString();
        minibusPathTest = Paths.get("data/minibus/Test").toAbsolutePath().toString();

        /*
        mnistPathToZeroes = Paths.get("data/mnist/training/0").toAbsolutePath().toString();
        mnistPathToOnes = Paths.get("data/mnist/training/1").toAbsolutePath().toString();
        mnistPathToTwos = Paths.get("data/mnist/training/2").toAbsolutePath().toString();
        mnistPathToThrees = Paths.get("data/mnist/training/3").toAbsolutePath().toString();
        mnistPathToFours = Paths.get("data/mnist/training/4").toAbsolutePath().toString();
        mnistPathToFives = Paths.get("data/mnist/training/5").toAbsolutePath().toString();
        mnistPathToSixs = Paths.get("data/mnist/training/6").toAbsolutePath().toString();
        mnistPathToSevens = Paths.get("data/mnist/training/7").toAbsolutePath().toString();
        mnistPathToEights = Paths.get("data/mnist/training/8").toAbsolutePath().toString();
        mnistPathToNines = Paths.get("data/mnist/training/9").toAbsolutePath().toString();
        mnistPathTest = Paths.get("data/mnist/test/test2").toAbsolutePath().toString();
        */
    }

    /*
    @Test
    public void imageGeometricsTestRandomNetworkOneHiddenLayer() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.initNetwork(784, 40, 2, 1);

        ImageLoader imageLoader = new ImageLoader(nn);
        imageLoader.addTrainingSet(geometricsPathToCircles, 0);
        imageLoader.addTrainingSet(geometricsPathToTriangles, 1);
        imageLoader.addTestPath(geometricsPathToTests);

        TrainingData trainingData = imageLoader.getTrainingData();
        Map<String, double[]> testData = imageLoader.getTestImages(trainingData.getMeanImage());

        nn.setTrainSet(trainingData.getImages());
        nn.setEstimatedResults(trainingData.getEstimatedResults());
        nn.setTrainingType(TrainingType.BACKPROPAGATION);
        nn.setLearningRate(0.001);
        nn.setMomentum(0.95);
        nn.setTargetLoss(0.00005);
        nn.setMaxEpoch(10000);
        nn.setActivationFunction(ActivationFunctionType.SIGMOID);

        NeuralNetwork trained = nn.train(nn);

        Iterator it = testData.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            trained.setInput(trained, (double[]) pair.getValue());
            if (pair.getKey().toString().contains("img1.jpg") || pair.getKey().toString().contains("img2.jpg")) {
                // should be recognized as a circle --> neuron 0 should fire
                List<Double> result = trained.test(trained);
                System.out.println(result);
                Assert.assertTrue(result.get(0) > 0.6 || result.get(0) > result.get(1));
                Assert.assertTrue(result.get(1) < 0.4 || result.get(1) < result.get(0));
            } else {
                // should be recognized as a triangle --> neuron 1 should fire
                List<Double> result = trained.test(trained);
                System.out.println(result);
                Assert.assertTrue(result.get(0) < 0.4 || result.get(0) < result.get(1));
                Assert.assertTrue(result.get(1) > 0.6 || result.get(1) > result.get(0));
            }
        }
    }


    @Test
    public void imageMnistTestRandomNetworkTwoHiddenLayer() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.initNetwork(784, 20, 10, 2);

        ImageLoader imageLoader = new ImageLoader(nn);
        imageLoader.addTrainingSet(mnistPathToZeroes, 0);
        imageLoader.addTrainingSet(mnistPathToOnes, 1);
        imageLoader.addTrainingSet(mnistPathToTwos, 2);
        imageLoader.addTrainingSet(mnistPathToThrees, 3);
        imageLoader.addTrainingSet(mnistPathToFours, 4);
        imageLoader.addTrainingSet(mnistPathToFives, 5);
        imageLoader.addTrainingSet(mnistPathToSixs, 6);
        imageLoader.addTrainingSet(mnistPathToSevens, 7);
        imageLoader.addTrainingSet(mnistPathToEights, 8);
        imageLoader.addTrainingSet(mnistPathToNines, 9);
        imageLoader.addTestPath(mnistPathTest);

        TrainingData trainingData = imageLoader.getTrainingData();
        Map<String, double[]> testData = imageLoader.getTestImages(trainingData.getMeanImage());

        nn.setTrainSet(trainingData.getImages());
        nn.setEstimatedResults(trainingData.getEstimatedResults());
        nn.setTrainingType(TrainingType.BACKPROPAGATION);
        nn.setLearningRate(0.01);
        nn.setMomentum(0.95);
        nn.setTargetLoss(0.005);
        nn.setMaxEpoch(40);
        nn.setActivationFunction(ActivationFunctionType.SIGMOID);

        NeuralNetwork trained = nn.train(nn);

        double quote = 0;
        Iterator it = testData.entrySet().iterator();
        int j = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String imagePath = pair.getKey().toString();
            trained.setInput(trained, (double[]) pair.getValue());
            List<Double> result = trained.test(trained);

            double highestValue = 0;
            int recognizedNumber = -1;
            for (int i = 0; i < result.size(); i++) {
                if (result.get(i) > highestValue) {
                    highestValue = result.get(i);
                    recognizedNumber = i;
                }
            }
            System.out.println(pair.getKey().toString() + " - Recognized Number: " + recognizedNumber);
            int indexOfPoint = imagePath.indexOf('.');
            int number = Integer.parseInt(imagePath.substring(indexOfPoint - 1, indexOfPoint));
            quote += (number == recognizedNumber) ? 1 : 0;
            j++;
        }
        Assert.assertTrue(quote / j >= 0.8);
    }


    @Test
    public void imageMnistTestRandomNetworkOneHiddenLayer() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.initNetwork(784, 40, 10, 1);

        ImageLoader imageLoader = new ImageLoader(nn);
        imageLoader.addTrainingSet(mnistPathToZeroes, 0);
        imageLoader.addTrainingSet(mnistPathToOnes, 1);
        imageLoader.addTrainingSet(mnistPathToTwos, 2);
        imageLoader.addTrainingSet(mnistPathToThrees, 3);
        imageLoader.addTrainingSet(mnistPathToFours, 4);
        imageLoader.addTrainingSet(mnistPathToFives, 5);
        imageLoader.addTrainingSet(mnistPathToSixs, 6);
        imageLoader.addTrainingSet(mnistPathToSevens, 7);
        imageLoader.addTrainingSet(mnistPathToEights, 8);
        imageLoader.addTrainingSet(mnistPathToNines, 9);
        imageLoader.addTestPath(mnistPathTest);

        TrainingData trainingData = imageLoader.getTrainingData();
        Map<String, double[]> testData = imageLoader.getTestImages(trainingData.getMeanImage());

        nn.setTrainSet(trainingData.getImages());
        nn.setEstimatedResults(trainingData.getEstimatedResults());
        nn.setTrainingType(TrainingType.BACKPROPAGATION);
        nn.setLearningRate(0.001);
        nn.setMomentum(0.95);
        nn.setTargetLoss(0.005);
        nn.setMaxEpoch(40);
        nn.setActivationFunction(ActivationFunctionType.SIGMOID);

        NeuralNetwork trained = nn.train(nn);

        double quote = 0;
        Iterator it = testData.entrySet().iterator();
        int j = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String imagePath = pair.getKey().toString();
            trained.setInput(trained, (double[]) pair.getValue());
            List<Double> result = trained.test(trained);

            double highestValue = 0;
            int recognizedNumber = -1;
            for (int i = 0; i < result.size(); i++) {
                if (result.get(i) > highestValue) {
                    highestValue = result.get(i);
                    recognizedNumber = i;
                }
            }
            System.out.println(pair.getKey().toString() + " - Recognized Number: " + recognizedNumber);
            int indexOfPoint = imagePath.indexOf('.');
            int number = Integer.parseInt(imagePath.substring(indexOfPoint - 1, indexOfPoint));
            quote += (number == recognizedNumber) ? 1 : 0;
            j++;
        }
        Assert.assertTrue(quote / j >= 0.8);
    }
*/

    /*
    @Test
    public void imageBusTestRandomNetworkOneHiddenLayer() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.initNetwork(784, 40, 3, 1);

        ImageLoader imageLoader = new ImageLoader(nn);
        imageLoader.addTrainingSet(busPathTraining, 0);
        imageLoader.addTrainingSet(catPathTraining, 1);
        imageLoader.addTrainingSet(woodPathTraining, 2);
        imageLoader.addTestPath(busPathTest);
        imageLoader.addTestPath(catPathTest);
        imageLoader.addTestPath(woodPathTest);

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

            if (imagePath.endsWith("0.jpg")) {
                // Neuron 1 was trained, so we check if the neuron with index 1 has the highest value
                Assert.assertTrue(result.get(1) > result.get(2) && result.get(1) > result.get(0));
            } else if (imagePath.endsWith("bus.jpg")) {
                // Neuron 0 was trained, so we check if the neuron with index 0 has the highest vlaue
                Assert.assertTrue(result.get(0) > result.get(1) && result.get(0) > result.get(2));
            } else if (imagePath.endsWith("holz.jpg")) {
                // Neuron 2 was trained, so we check if the neuron with index 2 has the highest value
                Assert.assertTrue(result.get(2) > result.get(0) && result.get(2) > result.get(1));
            }
            //Assert.assertTrue(result.get(0) > 0.6 || result.get(0) > result.get(1));
            //Assert.assertTrue(result.get(1) < 0.4 || result.get(1) < result.get(0));
        }
    }
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

