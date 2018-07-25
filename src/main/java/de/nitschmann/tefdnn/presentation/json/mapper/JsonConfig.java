package de.nitschmann.tefdnn.presentation.json.mapper;

import java.util.List;

public class JsonConfig {

    private boolean startTrainingImmediately;
    private boolean openGuiAfterTrainingIsCompleted;
    private Initialization initialization;
    private Configuration configuration;
    private List<TrainingData> trainingData;

    public void setStartTrainingImmediately(boolean startTrainingImmediately) {
        this.startTrainingImmediately = startTrainingImmediately;
    }

    public boolean getStartTrainingImmediately() {
        return this.startTrainingImmediately;
    }

    public void setOpenGuiAfterTrainingIsCompleted(boolean openGuiAfterTrainingIsCompleted) {
        this.openGuiAfterTrainingIsCompleted = openGuiAfterTrainingIsCompleted;
    }

    public boolean getOpenGuiAfterTrainingIsCompleted() {
        return this.openGuiAfterTrainingIsCompleted;
    }

    public void setInitialization(Initialization initialization) {
        this.initialization = initialization;
    }

    public Initialization getInitialization() {
        return this.initialization;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public List<TrainingData> getTrainingData() {
        return trainingData;
    }

    public void setTrainingData(List<TrainingData> trainingData) {
        this.trainingData = trainingData;
    }
}
