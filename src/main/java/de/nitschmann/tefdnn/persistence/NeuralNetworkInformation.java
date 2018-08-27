package de.nitschmann.tefdnn.persistence;

public class NeuralNetworkInformation {

    private String name;
    private int numberOfInputNeurons;
    private int numberOfHiddenNeurons;
    private int numberOfOutputNeurons;
    private int numberOfHiddenLayers;
    private double learningRate;
    private double momentum;
    private double targetLoss;
    private int maxEpoch;
    private int activationFunction;
    private int trainingType;
    private int neuralNetworkId;
    private int inputLayerId;
    private int idOfOneHiddenLayer;
    private int outputLayerId;

    public NeuralNetworkInformation(int neuralNetworkId, String name, double learningRate, double momentum, double targetLoss, int maxEpoch, int activationFunction, int trainingType) {
        this.neuralNetworkId = neuralNetworkId;
        this.name = name;
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.targetLoss = targetLoss;
        this.maxEpoch = maxEpoch;
        this.activationFunction = activationFunction;
        this.trainingType = trainingType;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfInputNeurons() {
        return numberOfInputNeurons;
    }

    public int getNumberOfHiddenNeurons() {
        return numberOfHiddenNeurons;
    }

    public int getNumberOfOutputNeurons() {
        return numberOfOutputNeurons;
    }

    public int getNumberOfHiddenLayers() {
        return numberOfHiddenLayers;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public double getMomentum() {
        return momentum;
    }

    public double getTargetLoss() {
        return targetLoss;
    }

    public int getMaxEpoch() {
        return maxEpoch;
    }

    public int getNeuralNetworkId() {
        return this.neuralNetworkId;
    }

    public int getActivationFunction() {
        return activationFunction;
    }

    public int getTrainingType() {
        return trainingType;
    }

    public void setNumberOfInputNeurons(int numberOfInputNeurons) {
        this.numberOfInputNeurons = numberOfInputNeurons;
    }

    public void setNumberOfHiddenNeurons(int numberOfHiddenNeurons) {
        this.numberOfHiddenNeurons = numberOfHiddenNeurons;
    }

    public void setNumberOfOutputNeurons(int numberOfOutputNeurons) {
        this.numberOfOutputNeurons = numberOfOutputNeurons;
    }

    public void setNumberOfHiddenLayers(int numberOfHiddenLayers) {
        this.numberOfHiddenLayers = numberOfHiddenLayers;
    }

    public int getInputLayerId() {
        return inputLayerId;
    }

    public void setInputLayerId(int inputLayerId) {
        this.inputLayerId = inputLayerId;
    }

    public int getIdOfOneHiddenLayer() {
        return idOfOneHiddenLayer;
    }

    public void setIdOfOneHiddenLayer(int idOfOneHiddenLayer) {
        this.idOfOneHiddenLayer = idOfOneHiddenLayer;
    }

    public int getOutputLayerId() {
        return outputLayerId;
    }

    public void setOutputLayerId(int outputLayerId) {
        this.outputLayerId = outputLayerId;
    }
}
