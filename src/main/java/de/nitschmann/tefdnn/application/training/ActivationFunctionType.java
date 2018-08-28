package de.nitschmann.tefdnn.application.training;

public enum ActivationFunctionType {
    SIGMOID(2), RELU(1), TANH(3);

    private final int value;
    ActivationFunctionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
