package de.nitschmann.tefdnn.application;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Neuron {

    /**
     * We save the sum of the input values. This is needed for the backpropagation algorithm.
     * It's basically the sum before the activation function gets called
     */
    private double sumOfInputValues;
    /**
     * The output value is the sumOfInputValues after the activation function.
     */
    private double outputValue;

    /**
     * The estimated value is the value which the neuron should have. Makes only sense for output neurons.
     */
    private double estimatedValue;

    /**
     * The error describes the difference between the output and the estimated result.
     * It's only used in the output neurons.
     */
    private double error;
    /**
     * There are two ways to calculate the delta:
     * Output Neuron delta: f'(netinput) * error
     * Hidden Neuron delta: f'(netinput) * sum of [ weights i multiplied with delta of output neurons ]
     */
    private double delta;

    /**
     * We need to adjust the weights while backpropagating the network.
     * We still need the old weights though for the calculation.
     * Because of this we temporary save the weights before the backpropagation algorithm gets executed.
     */
    private ArrayList<Double> weightOutTemp;
    /**
     * WeightIn and WeightOut of the neurons
     */
    private ArrayList<Double> weightIn;
    private ArrayList<Double> weightOut;

    /**
     * When users set a folder which contains an image class, the user needs to specify which output neuron
     * should be trained for that class. In order to do that, we need to save it. Therefore, this attribute
     * is only relevant for neurons which are on the output layer.
     */
    private int neuronId;

    /**
     * A neuron has a name which need to be specified so we can output it in the graphical user interface
     * or in the console which class was recognized.
     */
    private String name;

    /**
     * We use momentum, therefore we have to save the old delta values of each output weight
     */
    private Map<Integer, Double> oldDeltaW_ij = new HashMap<>();

    /**
     * Since we have a copy constructor we need this to initialize an empty neuron
     */
    public Neuron() {

    }

    /**
     * Copy constructor
     * @param neuron neuron
     * Neuron which should be copied
     */
    public Neuron(Neuron neuron) {
        this.weightIn = new ArrayList<>();
        this.weightOut = new ArrayList<>();

        if (neuron.getWeightIn() != null) {
            weightIn.addAll(neuron.getWeightIn());
        }

        if (neuron.getWeightOut() != null) {
            weightOut.addAll(neuron.getWeightOut());
        }

        this.sumOfInputValues = neuron.sumOfInputValues;
        this.outputValue = neuron.outputValue;
        this.neuronId = neuron.neuronId;
        this.name = neuron.name;
    }

    /**
     * @return A random value between (-1 / number of Input neurons) and (1 / number of Input neurons)
     * @param numberOfInputNeurons number of weights which go into the neuron
     */
    public double initNeuron(int numberOfInputNeurons) {
        double max = 1 / Math.sqrt(numberOfInputNeurons);
        double min = -1 / Math.sqrt(numberOfInputNeurons);
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public ArrayList<Double> getWeightIn() {
        return weightIn;
    }

    public void setWeightIn(ArrayList<Double> weightIn) {
        this.weightIn = weightIn;
    }

    public ArrayList<Double> getWeightOut() {
        return weightOut;
    }

    public void setWeightOut(ArrayList<Double> weightOut) {
        this.weightOut = weightOut;
    }

    public ArrayList<Double> getWeightOutTemp() {
        return weightOutTemp;
    }

    public void setWeightOutTemp(ArrayList<Double> weightOutTemp) {
        this.weightOutTemp = weightOutTemp;
    }

    public double getOutputValue() {
        return outputValue;
    }

    public void setOutputValue(double outputValue) {
        this.outputValue = outputValue;
    }

    public void setSumOfInputValues(double value) {
        this.sumOfInputValues = value;
    }

    public double getSumOfInputValues() {
        return this.sumOfInputValues;
    }

    public void setError(double error) {
        this.error = error;
    }

    public double getError() {
        return this.error;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getDelta() {
        return this.delta;
    }

    public void setEstimatedValue(double estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public double getEstimatedValue() {
        return this.estimatedValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Map<Integer, Double> getOldDeltaW_ij() {
        return this.oldDeltaW_ij;
    }

    /**
     * We immediately change the weights while backpropagating. The algorithm still needs the old weights though.
     * That's why we need to save them before backpropagating through the training set.
     */
    public void saveActualWeightsInTemporaryVar() {
        try {
            ArrayList<Double> tempOut = new ArrayList<>(this.weightOut);
            setWeightOutTemp(tempOut);
        } catch (NullPointerException e) {
            // We just swallow it --> makes no difference.
        }
    }

    @Override
    public boolean equals(Object o) {
        if (getClass() != o.getClass()) {
            return false;
        }

        Neuron n;
        try {
            n = (Neuron) o;
        } catch (ClassCastException e) {
            return false;
        }

        if (n.getWeightIn() != null && this.getWeightIn() != null) {
            if (n.getWeightIn().size() != this.getWeightIn().size()) {
                return false;
            }

            for (int i = 0; i < n.getWeightIn().size(); i++) {
                if (n.getWeightIn().get(i).doubleValue() != this.getWeightIn().get(i).doubleValue()) {
                    return false;
                }
            }
        }


        if (n.getWeightOut() != null && this.getWeightOut() != null) {
            if (n.getWeightOut().size() != this.getWeightOut().size()) {
                return false;
            }

            for (int i = 0; i < n.getWeightOut().size(); i++) {
                if (n.getWeightOut().get(i).doubleValue() != this.getWeightOut().get(i).doubleValue()) {
                    return false;
                }
            }

        }

        return true;
    }
}