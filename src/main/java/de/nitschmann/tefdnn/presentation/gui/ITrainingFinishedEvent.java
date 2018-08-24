package de.nitschmann.tefdnn.presentation.gui;

import de.nitschmann.tefdnn.application.NeuralNetwork;

public interface ITrainingFinishedEvent {
    public void trainingFinished(NeuralNetwork neuralNetwork);
}
