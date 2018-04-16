package de.nitschmann.tefdnn.application.training;

public class Tanh implements IActivationFunction {
    /**
     * Activation function - in this case, it's the tanh function.
     * @param value
     * Value which should be processed
     * @return
     * Value after processing the tanh function
     */
    public double activate(double value) {
        return Math.tanh(value);
    }

    /**
     * Derivative of the tanh function
     * @param value
     * Value which should be processed
     * @return
     * Value after processing the derivative function
     */
    public double derivative(double value) {
        return 1 - Math.pow(activate(value), 2);
    }
}
