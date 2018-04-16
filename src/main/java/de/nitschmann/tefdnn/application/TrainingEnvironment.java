package de.nitschmann.tefdnn.application;

public class TrainingEnvironment {

    private NeuralNetwork feedForwardNetwork;
    private NeuralNetwork autoEncoderNetwork;
    private String name;

    public TrainingEnvironment() {

    }

    public TrainingEnvironment(NeuralNetwork ff, NeuralNetwork ae) {
        this.feedForwardNetwork = ff;
        this.autoEncoderNetwork = ae;
    }

    public TrainingEnvironment(String name, NeuralNetwork ff) {
        this.feedForwardNetwork = ff;
        this.name = name;
    }

    public TrainingEnvironment(NeuralNetwork ff) {
        this.feedForwardNetwork = ff;
    }

    public TrainingEnvironment(String name, NeuralNetwork ff, NeuralNetwork ae) {
        this.name = name;
        this.feedForwardNetwork = ff;
        this.autoEncoderNetwork = ae;
    }

    public void setFeedForwardNetwork(NeuralNetwork nn) {
        this.feedForwardNetwork = nn;
    }

    public void setAutoEncoderNetwork(NeuralNetwork nn) {
        this.autoEncoderNetwork = nn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NeuralNetwork getAutoEncoderNetwork() {
        return this.autoEncoderNetwork;
    }

    public NeuralNetwork getFeedForwardNetwork() {
        return this.feedForwardNetwork;
    }

    public String getName() {
        return this.name;
    }
}
