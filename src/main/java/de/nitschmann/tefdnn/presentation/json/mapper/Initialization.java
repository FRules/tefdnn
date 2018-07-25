package de.nitschmann.tefdnn.presentation.json.mapper;

public class Initialization {

    private String name;
    private int countOfInputNeurons;
    private int countOfHiddenNeurons;
    private int countOfOutputNeurons;
    private int countOfHiddenLayers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountOfInputNeurons() {
        return countOfInputNeurons;
    }

    public void setCountOfInputNeurons(int countOfInputNeurons) {
        this.countOfInputNeurons = countOfInputNeurons;
    }

    public int getCountOfHiddenNeurons() {
        return countOfHiddenNeurons;
    }

    public void setCountOfHiddenNeurons(int countOfHiddenNeurons) {
        this.countOfHiddenNeurons = countOfHiddenNeurons;
    }

    public int getCountOfOutputNeurons() {
        return countOfOutputNeurons;
    }

    public void setCountOfOutputNeurons(int countOfOutputNeurons) {
        this.countOfOutputNeurons = countOfOutputNeurons;
    }

    public int getCountOfHiddenLayers() {
        return countOfHiddenLayers;
    }

    public void setCountOfHiddenLayers(int countOfHiddenLayers) {
        this.countOfHiddenLayers = countOfHiddenLayers;
    }
}
