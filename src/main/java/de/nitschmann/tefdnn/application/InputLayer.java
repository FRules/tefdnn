package de.nitschmann.tefdnn.application;

import java.util.ArrayList;
import java.util.List;

public class InputLayer extends Layer {

    /**
     * Since we have a copy constructor we need this to initialize an empty layer
     */
    public InputLayer() {

    }

    /**
     * Copy constructor
     * @param inputLayer
     * input layer which should be copied
     */
    public InputLayer(InputLayer inputLayer) {
        super(inputLayer);
    }

    /**
     * Initializes the Input layer with random weights
     * @param inputLayer
     * Input layer which should be initialized
     */
    public void initLayer(InputLayer inputLayer) {
        ArrayList<Neuron> neurons = new ArrayList<>();

        // Initialize a BIAS neuron and add it to the list of neurons
        Neuron bias = new Neuron();
        bias.setOutputValue(1);
        bias.setSumOfInputValues(1);
        neurons.add(bias);

        // We start at one because we already added one bias neuron.
        for (int i = 1; i < inputLayer.getCountOfNeurons(); i++) {
            // Create a neuron. Since we're on the input layer,
            // it has no input weights. Output weights get added later if we
            // initialized the input weights of the first hidden layer, then they'll
            // get connected
            Neuron neuron = new Neuron();
            neurons.add(neuron);
        }

        inputLayer.setNeurons(neurons);
    }

    /**
     * Initializes the input layer with pre-defined outputs
     * @param inputLayer
     * Input layer which should be initialized
     * @param outputs
     * Pre-defined outputs of each neuron in this layer
     */
    public void initLayer(InputLayer inputLayer, List<Double> outputs) {
        ArrayList<Neuron> neurons = new ArrayList<>();

        // Bias
        Neuron bias = new Neuron();
        bias.setOutputValue(1);
        bias.setSumOfInputValues(1);
        neurons.add(bias);

        for (double output : outputs) {
            Neuron neuron = new Neuron();
            neuron.setOutputValue(output);
            neuron.setSumOfInputValues(output);
            neurons.add(neuron);
        }

        inputLayer.setNeurons(neurons);
    }

    // Add 1 BIAS Neuron
    public void setCountOfNeurons(int countOfNeurons) {
        super.setCountOfNeurons(countOfNeurons + 1);
    }
}
