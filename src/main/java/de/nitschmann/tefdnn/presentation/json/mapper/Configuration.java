package de.nitschmann.tefdnn.presentation.json.mapper;

public class Configuration {

    private double learningRate;
    private int trainingType;
    private int activationFunction;
    private int maximumNumberOfEpochs;
    private double targetLoss;
    private double momentum;

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public int getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(int trainingType) {
        this.trainingType = trainingType;
    }

    public int getActivationFunction() {
        return activationFunction;
    }

    public void setActivationFunction(int activationFunction) {
        this.activationFunction = activationFunction;
    }

    public int getMaximumNumberOfEpochs() {
        return maximumNumberOfEpochs;
    }

    public double getTargetLoss() {
        return targetLoss;
    }

    public void setTargetLoss(double targetLoss) {
        this.targetLoss = targetLoss;
    }

    public double getMomentum() {
        return momentum;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }
}
