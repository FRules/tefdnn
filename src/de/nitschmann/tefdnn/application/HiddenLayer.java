package de.nitschmann.tefdnn.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HiddenLayer extends Layer {

    /**
     * Since we have a copy constructor we need this to initialize an empty layer
     */
    public HiddenLayer() {

    }

    /**
     * Copy constructor
     * @param hiddenLayer
     * Hidden layer which should be copied
     */
    public HiddenLayer(HiddenLayer hiddenLayer) {
        super(hiddenLayer);
    }

    /**
     * Initializes the hidden layers with pre-defined weights
     * @param hiddenLayers
     * Output hidden layers which should be initialized
     * @param weightsList
     * Pre-defined weights of each neuron in every hidden layer
     */
    public void initLayer(List<HiddenLayer> hiddenLayers, List<Map<Integer, List<Double>>> weightsList) {
        if (hiddenLayers.size() != weightsList.size()) {
            return;
        }

        for (int i = 0; i < weightsList.size(); i++) {
            Map<Integer, List<Double>> weights = weightsList.get(i);
            super.initLayer(hiddenLayers.get(i), weights, true);
        }
    }

    /***
     * Initializes the hidden layers with random data
     * @param hiddenLayers
     * hidden layers which should be initialized
     * @param inputLayer
     * input layer of the network
     */
    public void initLayer(List<HiddenLayer> hiddenLayers, InputLayer inputLayer) {
        for(int i = 0; i < hiddenLayers.size(); i++) {
            // Initialize a list of neurons for each hidden layer
            ArrayList<Neuron> neurons = new ArrayList<>();

            // Add one bias neuron
            Neuron bias = new Neuron();
            bias.setOutputValue(1);
            bias.setSumOfInputValues(1);
            neurons.add(bias);

            // Start at 1, because we already added one bias neuron
            for (int j = 1; j < hiddenLayers.get(i).getCountOfNeurons(); j++) {
                ArrayList<Double> weightsIn = new ArrayList<>();

                int numberOfInputWeights;

                if (i == 0) {
                    // We're in the first loop which means we're on the first hidden layer.
                    // That means the amount of input weights equals the amount of neurons on the input layer.
                    numberOfInputWeights = inputLayer.getCountOfNeurons();

                } else {
                    // We're not in the first loop. This means the amount of input weights equals the
                    // amount of neurons in the previous hidden layer
                    numberOfInputWeights = hiddenLayers.get(i - 1).getCountOfNeurons();
                }

                Neuron neuron = new Neuron();

                for (int p = 0; p < numberOfInputWeights; p++) {
                    //weightsIn.add( neuron.initNeuron() );
                    weightsIn.add( neuron.initNeuron(numberOfInputWeights) );

                }

                neuron.setWeightIn(weightsIn);

                neurons.add(neuron);
            }

            hiddenLayers.get(i).setNeurons(neurons);
        }

    }

    /**
     * Sets the counts of neurons but adds one extra neuron as bias
     * @param countOfNeurons amount of neurons
     */
    public void setCountOfNeurons(int countOfNeurons) {
        super.setCountOfNeurons(countOfNeurons + 1);
    }
}
