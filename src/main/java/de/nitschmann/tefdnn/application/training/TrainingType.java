package de.nitschmann.tefdnn.application.training;

public enum TrainingType {
    BACKPROPAGATION(1);

    private final int value;
    TrainingType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
