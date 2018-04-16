package de.nitschmann.tefdnn.application.training;

public class Sigmoid implements IActivationFunction {

    /**
     * Activation function - in this case, it's the sigmoid function.
     * @param value
     * Value which should be processed
     * @return
     * Value after processing the sigmoid function
     */
    public double activate(double value) {
        return 1 / (1 + Math.exp(-value));
    }

    /**
     * Derivative of the sigmoid function
     * @param value
     * Value which should be processed
     * @return
     * Value after processing the derivative function
     */
    public double derivative(double value) {
        return activate(value) * (1 - activate(value));
    }
}
