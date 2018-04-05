package de.nitschmann.tefdnn.test.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Weights {
    private List<Double> outputsInInput;
    private Map<Integer, List<Double>> weightInOutput;
    private List<Map<Integer, List<Double>>> weightInHidden;

    public Weights(List<Double> outputsInInput, Map<Integer, List<Double>> weightInOutput, List<Map<Integer, List<Double>>> weightInHidden) {
        this.outputsInInput = outputsInInput;
        this.weightInOutput = weightInOutput;
        this.weightInHidden = weightInHidden;
    }

    public List<Double> getOutputsInInput() {
        return outputsInInput;
    }

    public Map<Integer, List<Double>> getWeightInOutput() {
        return weightInOutput;
    }

    public List<Map<Integer, List<Double>>> getWeightInHidden() {
        return weightInHidden;
    }
}
