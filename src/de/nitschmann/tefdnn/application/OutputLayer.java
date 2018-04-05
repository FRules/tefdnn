package de.nitschmann.tefdnn.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutputLayer extends Layer {

    /**
     * Since we have a copy constructor we need this to initialize an empty layer
     */
    public OutputLayer() {

    }

    /**
     * Copy constructor
     * @param outputLayer
     * output layer which should be copied
     */
    public OutputLayer(OutputLayer outputLayer) {
        super(outputLayer);
    }

    /**
     * Initializes the Output layer with random weights
     * @param outputLayer
     * Output layer which should be initialized
     * @param numberOfNeuronsInHiddenLayer
     * Amount of neurons that are in hidden layer. The amount of neurons equal the number of input weights for each output neuron.
     */
    public void initLayer(OutputLayer outputLayer, int numberOfNeuronsInHiddenLayer) {
        ArrayList<Neuron> neurons = new ArrayList<>();

        for(int i = 0; i < outputLayer.getCountOfNeurons(); i++) {
            // Create a neuron
            Neuron neuron = new Neuron();
            ArrayList<Double> weightsIn = new ArrayList<>();

            for (int j = 0; j < numberOfNeuronsInHiddenLayer; j++) {
                // Initialize the weight ins of the neuron with a random value
                //weightsIn.add( neuron.initNeuron() );
                weightsIn.add( neuron.initNeuron(numberOfNeuronsInHiddenLayer) );
            }

            // Set the weights of the neuron and add the neuron to the list of neurons in output layer
            neuron.setWeightIn(weightsIn);
            neurons.add(neuron);
        }

        // Add all neurons to the output layer
        outputLayer.setNeurons(neurons);
    }


    /**
     * Initializes the output layer with pre-defined weights
     * @param outputLayer
     * Output layer which should be initialized
     * @param weights
     * Pre-defined weights of each neuron in this layer
     */
    public void initLayer(OutputLayer outputLayer, Map<Integer, List<Double>> weights) {
        // Output-Layer has no BIAS
        super.initLayer(outputLayer, weights, false);
    }
}
