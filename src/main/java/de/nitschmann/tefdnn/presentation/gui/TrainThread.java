package de.nitschmann.tefdnn.presentation.gui;

import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.persistence.Database;
import de.nitschmann.tefdnn.presentation.Console;
import de.nitschmann.tefdnn.presentation.gui.console.GuiToConsoleConverter;
import de.nitschmann.tefdnn.presentation.gui.wrapper.DynamicTrainingComponent;

import javax.swing.*;
import java.util.ArrayList;

public class TrainThread implements Runnable {

    private Database database;
    private JTextField textName;
    private JTextField textInputNeurons;
    private JTextField textHiddenNeurons;
    private JTextField textOutputNeurons;
    private JTextField textHiddenLayers;
    private JTextField textLearningRate;
    private JTextField textMomentum;
    private JTextField textMaxEpoch;
    private JTextField textTargetLoss;
    private JComboBox<String> comboTrainingType;
    private JComboBox<String> comboActivationFunction;
    private ArrayList<ArrayList<DynamicTrainingComponent>> dynamicTrainingComponents;
    private ITrainingFinishedEvent trainingFinishedEvent;

    public TrainThread(StartView view, ITrainingFinishedEvent trainingFinishedEvent) {
        this.database = view.database;
        this.textName = view.textName;
        this.textInputNeurons = view.textInputNeurons;
        this.textHiddenNeurons = view.textHiddenNeurons;
        this.textOutputNeurons = view.textOutputNeurons;
        this.textHiddenLayers = view.textHiddenLayers;
        this.textLearningRate = view.textLearningRate;
        this.textMomentum = view.textMomentum;
        this.textMaxEpoch = view.textMaxEpoch;
        this.textTargetLoss = view.textTargetLoss;
        this.comboTrainingType = view.comboTrainingType;
        this.comboActivationFunction = view.comboActivationFunction;
        this.dynamicTrainingComponents = view.dynamicTrainingComponents;
        this.trainingFinishedEvent = trainingFinishedEvent;
    }

    @Override
    public void run() {
        Console c = new Console(database);

        String init = GuiToConsoleConverter.getInitializationPart(textName, textInputNeurons, textHiddenNeurons, textOutputNeurons, textHiddenLayers);
        NeuralNetwork neuralNetwork = c.init(init);

        String conf = GuiToConsoleConverter.getConfigurationPart(textLearningRate, textMomentum, textMaxEpoch, textTargetLoss, comboTrainingType, comboActivationFunction);
        c.conf(conf, neuralNetwork);

        String[] trains = GuiToConsoleConverter.getTrainingPart(this.dynamicTrainingComponents);
        for(String train : trains) {
            c.train(train, neuralNetwork);
        }

        c.train("train -s", neuralNetwork);
        trainingFinishedEvent.trainingFinished(neuralNetwork);
    }
}
