package de.nitschmann.tefdnn.application.training;

public class Relu implements IActivationFunction {

    public double activate(double value) {
        return value > 0 ? value : 0;
    }

    public double derivative(double value) {
        return value > 0 ? 1 : 0;
    }
}
