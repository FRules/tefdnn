package de.nitschmann.tefdnn.application;

import de.nitschmann.tefdnn.application.io.ImageLoader;
import de.nitschmann.tefdnn.application.io.TrainingData;
import de.nitschmann.tefdnn.application.training.ActivationFunctionType;
import de.nitschmann.tefdnn.application.training.TrainingType;

import java.io.IOException;
import java.nio.file.Paths;


public class SensitivityAnalysis {

    private String busPathTraining;
    private String pkwPathTraining;
    private String minibusPathTraining;

    private void initializePaths() {
        busPathTraining = Paths.get("data/bus/Training").toAbsolutePath().toString();
        pkwPathTraining = Paths.get("data/pkw/Training").toAbsolutePath().toString();
        minibusPathTraining = Paths.get("data/minibus/Training").toAbsolutePath().toString();
    }

    private NeuralNetwork getNeuralNetwork() {
        initializePaths();

        NeuralNetwork nn = new NeuralNetwork();
        nn.initNetwork(784, 40, 3, 1);

        ImageLoader imageLoader = new ImageLoader(nn);
        imageLoader.addTrainingSet(busPathTraining, 0);
        imageLoader.addTrainingSet(pkwPathTraining, 1);
        imageLoader.addTrainingSet(minibusPathTraining, 2);

        TrainingData trainingData = imageLoader.getTrainingData();

        nn.setTrainSet(trainingData.getImages());
        nn.setEstimatedResults(trainingData.getEstimatedResults());
        nn.setTrainingType(TrainingType.BACKPROPAGATION);
        // Set target loss to 0 because it can never be reached
        nn.setTargetLoss(0);
        // Maximum epochs is 2500 for tests with R
        nn.setMaxEpoch(2500);

        return nn;
    }

    public void startAllScenarios() {
        scenario_01();
        scenario_02();
        scenario_03();
        scenario_04();
        scenario_05();
        scenario_06();
        scenario_07();
        scenario_08();
    }

    public void scenario_01() {
        NeuralNetwork nn = getNeuralNetwork();
        nn.setActivationFunction(ActivationFunctionType.SIGMOID);
        nn.setLearningRate(0.01);
        nn.setMomentum(0.95);
        try {
            nn.sensitivityAnalysis(nn, "scenario_01.csv");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void scenario_02() {
        NeuralNetwork nn = getNeuralNetwork();
        nn.setActivationFunction(ActivationFunctionType.SIGMOID);
        nn.setLearningRate(0.1);
        nn.setMomentum(0.5);
        try {
            nn.sensitivityAnalysis(nn, "scenario_02.csv");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void scenario_03() {
        NeuralNetwork nn = getNeuralNetwork();
        nn.setActivationFunction(ActivationFunctionType.SIGMOID);
        nn.setLearningRate(0.5);
        nn.setMomentum(0.1);
        try {
            nn.sensitivityAnalysis(nn, "scenario_03.csv");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void scenario_04() {
        NeuralNetwork nn = getNeuralNetwork();
        nn.setActivationFunction(ActivationFunctionType.SIGMOID);
        nn.setLearningRate(1);
        nn.setMomentum(0);
        try {
            nn.sensitivityAnalysis(nn, "scenario_04.csv");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void scenario_05() {
        NeuralNetwork nn = getNeuralNetwork();
        nn.setActivationFunction(ActivationFunctionType.TANH);
        nn.setLearningRate(0.01);
        nn.setMomentum(0.95);
        try {
            nn.sensitivityAnalysis(nn, "scenario_05.csv");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void scenario_06() {
        NeuralNetwork nn = getNeuralNetwork();
        nn.setActivationFunction(ActivationFunctionType.TANH);
        nn.setLearningRate(0.1);
        nn.setMomentum(0.5);
        try {
            nn.sensitivityAnalysis(nn, "scenario_06.csv");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void scenario_07() {
        NeuralNetwork nn = getNeuralNetwork();
        nn.setActivationFunction(ActivationFunctionType.TANH);
        nn.setLearningRate(0.5);
        nn.setMomentum(0.1);
        try {
            nn.sensitivityAnalysis(nn, "scenario_07.csv");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void scenario_08() {
        NeuralNetwork nn = getNeuralNetwork();
        nn.setActivationFunction(ActivationFunctionType.TANH);
        nn.setLearningRate(1);
        nn.setMomentum(0);
        try {
            nn.sensitivityAnalysis(nn, "scenario_08.csv");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
