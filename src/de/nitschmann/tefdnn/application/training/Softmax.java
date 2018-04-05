package de.nitschmann.tefdnn.application.training;

import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.application.Neuron;

public class Softmax {
    public static double getProbability(NeuralNetwork nn, int neuronIndex) {
        double numerator = Math.exp(nn.getOutputLayer().getNeurons().get(neuronIndex).getSumOfInputValues());
        double denominator = 0;

        for(Neuron n : nn.getOutputLayer().getNeurons()) {
            denominator += Math.exp(n.getSumOfInputValues());
        }

        return numerator / denominator;
    }

    public static double derivative(double value) {
        return value * (1 - value);
    }

    public static double calcLoss(NeuralNetwork nn) {
        double loss = 0;
        for(Neuron n : nn.getOutputLayer().getNeurons()) {
            loss += n.getEstimatedValue() * Math.log(n.getOutputValue());
        }
        return -loss;
    }
}
