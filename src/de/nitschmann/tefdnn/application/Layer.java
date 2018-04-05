package de.nitschmann.tefdnn.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Layer {

    /**
     * Every Layer has a list of neurons and therefore a count of neurons
     */
    private ArrayList<Neuron> neurons;
    private int countOfNeurons;

    /**
     * Since we have a copy constructor we need this to initialize an empty layer
     */
    public Layer() {

    }

    /**
     * Copy constructor
     * @param layer
     * layer which should be copied
     */
    public Layer(Layer layer) {
        this.neurons = new ArrayList<>();
        for(Neuron neuron : layer.getNeurons()) {
            this.neurons.add(new Neuron(neuron));
        }
        this.countOfNeurons = layer.getCountOfNeurons();
    }

    /**
     * @return the amount of neurons
     */
    public int getCountOfNeurons() {
        return countOfNeurons;
    }

    /**
     * @param countOfNeurons sets the amount of neurons
     */
    public void setCountOfNeurons(int countOfNeurons) {
        this.countOfNeurons = countOfNeurons;
    }

    /**
     * @param neurons sets the neurons
     */
    public void setNeurons(ArrayList<Neuron> neurons) {
        this.neurons = neurons;
    }

    /**
     * @return the list of neurons
     */
    public ArrayList<Neuron> getNeurons() {
        return neurons;
    }

    /**
     * Initializes a layer with predefined weights.
     * @param layer
     * Specified layer. Mostly Hidden or OutputLayer since this method initializes weights and
     * input layers do not have weights.
     * @see InputLayer
     * @see HiddenLayer
     * @see OutputLayer
     * @param weights
     * Map of weights which are needed for the initialization of the weight ins for each neuron
     * @param withBias
     * Output Layer doesn't have a bias but hidden an input layers do. The bias will be created if
     * this is true.
     */
    protected void initLayer(Layer layer, Map<Integer, List<Double>> weights, boolean withBias) {
        ArrayList<Neuron> neurons = new ArrayList<>();

        // Bias has no input weights.
        if (withBias) {
            Neuron bias = new Neuron();
            bias.setOutputValue(1);
            bias.setSumOfInputValues(1);
            neurons.add(bias);
        }

        for(Map.Entry<Integer, List<Double>> entry : weights.entrySet()) {
            Neuron neuron = new Neuron();
            ArrayList<Double> weightInTemp = new ArrayList<>();
            for(double weight : entry.getValue()) {
                weightInTemp.add(weight);
            }
            neuron.setWeightIn(weightInTemp);
            neurons.add(neuron);
        }

        layer.setNeurons(neurons);
    }

    @Override
    public boolean equals(Object o) {
        Layer layer;
        try {
            layer = (Layer) o;
        } catch (ClassCastException e) {
            return false;
        }

        if (layer.getCountOfNeurons() != this.getCountOfNeurons()) {
            return false;
        }

        for (int i = 0; i < layer.getCountOfNeurons(); i++) {
            if (!layer.getNeurons().get(i).equals(this.getNeurons().get(i))) {
                return false;
            }
        }

        return true;
    }
}
