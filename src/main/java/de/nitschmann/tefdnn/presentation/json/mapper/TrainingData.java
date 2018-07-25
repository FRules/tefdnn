package de.nitschmann.tefdnn.presentation.json.mapper;

public class TrainingData {

    private String name;
    private String pathToDirectory;
    private int targetNeuron;


    public String getPathToDirectory() {
        return pathToDirectory;
    }

    public void setPathToDirectory(String pathToDirectory) {
        this.pathToDirectory = pathToDirectory;
    }

    public int getTargetNeuron() {
        return targetNeuron;
    }

    public void setTargetNeuron(int targetNeuron) {
        this.targetNeuron = targetNeuron;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
