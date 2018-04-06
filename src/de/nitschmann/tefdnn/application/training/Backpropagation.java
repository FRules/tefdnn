package de.nitschmann.tefdnn.application.training;

import de.nitschmann.tefdnn.application.*;

public class Backpropagation {

    private IActivationFunction activationFunction;

    public Backpropagation(IActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    /**
     * Trains the neural network with backpropagation algorithm.
     *
     * @param nn Neural Network which should be trained
     * @return trained Neural Network
     * @see NeuralNetwork
     */
    public NeuralNetwork train(NeuralNetwork nn) {
        int epoch = 0;
        double meanLoss = Integer.MAX_VALUE;

        while (meanLoss > nn.getTargetLoss()) {

            double loss = 0;

            if (epoch > nn.getMaxEpoch()) {
                break;
            }

            // We iterate over the whole train set
            for (int i = 0; i < nn.getTrainSet().length; i++) {
                // We initialize the input layer neurons with the train set data.
                // We start at 1 because the first neuron is the bias.
                for (int j = 1; j < nn.getInputLayer().getCountOfNeurons(); j++) {
                    nn.getInputLayer().getNeurons().get(j).setOutputValue(nn.getTrainSet()[i][j - 1]);
                    nn.getInputLayer().getNeurons().get(j).setSumOfInputValues(nn.getTrainSet()[i][j - 1]);
                }

                for (int j = 0; j < nn.getOutputLayer().getCountOfNeurons(); j++) {
                    nn.getOutputLayer().getNeurons().get(j).setEstimatedValue(nn.getEstimatedResults()[i][j]);
                }

                // We need to save the weights because the backpropagation algorithm changes the weights instantly.
                // Because the old weights are still needed for the algorithm for further calculation, its necessary
                // to save them.
                saveWeights(nn);

                // Forward the data through the network
                nn = forward(nn);

                // Set the loss of the output neurons after we've forward propagated through one entry of the trainset.
                //loss += calcLoss(nn);
                loss += calcLoss(nn);

                // Backpropagate the error through the network and adjust weights
                nn = backpropagate(nn);
            }

            meanLoss = loss / nn.getTrainSet().length;
            System.out.println("Epoch: " + epoch + ", Mean Loss: " + meanLoss);
            epoch++;
        }
        return nn;

    }

    public double calcLoss(NeuralNetwork nn) {
        double loss = 0;
        for (Neuron neuron : nn.getOutputLayer().getNeurons()) {
            loss += 0.5 * Math.pow(neuron.getEstimatedValue() - neuron.getOutputValue(), 2);
        }
        return loss;
    }

    /**
     * We need to save the weights. Every neuron has extra temporary lists reserved for that.
     * The backpropagation algorithm first adjusts the weights between output and the last hidden layer.
     * The backpropagation algorithm needs the old weights though later.
     * SUGGESTION: Maybe we can calculate the delta of weights and change them after a desired amount of epochs.
     *
     * @param nn The neural network for which all the weights should be saved
     */
    private void saveWeights(NeuralNetwork nn) {
        for (Neuron neuron : nn.getInputLayer().getNeurons()) {
            neuron.saveActualWeightsInTemporaryVar();
        }

        for (HiddenLayer hiddenLayer : nn.getHiddenLayers()) {
            for (Neuron neuron : hiddenLayer.getNeurons()) {
                neuron.saveActualWeightsInTemporaryVar();
            }
        }

        for (Neuron neuron : nn.getOutputLayer().getNeurons()) {
            neuron.saveActualWeightsInTemporaryVar();
        }
    }

    /**
     * Calculates the input sums of each neuron and also the final output of each neuron (after the activation function)
     *
     * @param nn The neural network which should be used.
     * @return NeuralNetwork
     * @see NeuralNetwork
     */
    public NeuralNetwork forward(NeuralNetwork nn) {
        int hiddenLayer_i = 0;
        // First, we loop through all hidden layers and calculate for each hidden neuron the sums and output
        for (HiddenLayer hiddenLayer : nn.getHiddenLayers()) {
            // We loop through each neuron in the current hidden layer being processed except the BIAS, thats
            // why we start at 1
            for (int i = 1; i < hiddenLayer.getCountOfNeurons(); i++) {
                Neuron neuron = hiddenLayer.getNeurons().get(i);
                double sum = 0.0;
                if (hiddenLayer_i == 0) {
                    // We're in the first hidden layer. That being said, the sums and outputs are being calculated
                    // with the training data --> outputs of input layer.
                    for (int j = 0; j < nn.getInputLayer().getCountOfNeurons(); j++) {
                        // Multiply each weight with the output of the input neuron and add it to the sum
                        sum += neuron.getWeightIn().get(j) * nn.getInputLayer().getNeurons().get(j).getOutputValue();
                    }
                } else {
                    // We're not in the first hidden layer which means the sums and outputs are being calculated with the
                    // outputs of the previous hidden layer
                    for (int j = 0; j < hiddenLayer.getCountOfNeurons(); j++) {
                        double outputValueOfPreviousHiddenLayerNeuron = nn.getHiddenLayers().get(hiddenLayer_i - 1).getNeurons().get(j).getOutputValue();
                        sum += neuron.getWeightIn().get(j) * outputValueOfPreviousHiddenLayerNeuron;
                    }
                }
                // Set the sum and output value for the neuron
                neuron.setSumOfInputValues(sum);
                neuron.setOutputValue(activationFunction.activate(sum));
            }

            hiddenLayer_i++;
        }

        // After we calculated all the sums and outputs of all neurons in all hidden layers
        // we have to continue with calculating the values of each neuron in the output layer.

        // The reference is the last hidden layer, that's why we save it to a variable for easier understanding
        HiddenLayer lastHiddenLayer = nn.getHiddenLayers().get(nn.getHiddenLayers().size() - 1);

        // loop through each neuron in the output layer. Since we have no BIAS in the output neuron, it's fine to
        // start at 0 this time.
        for (int neuron_i = 0; neuron_i < nn.getOutputLayer().getCountOfNeurons(); neuron_i++) {
            Neuron neuron = nn.getOutputLayer().getNeurons().get(neuron_i);
            double sum = 0.0;
            for (int i = 0; i < neuron.getWeightIn().size(); i++) {
                // Multiply each weight with the output of the last hidden layer neurons and add it to the sum
                double outputValueOfLastHiddenLayerNeuron = lastHiddenLayer.getNeurons().get(i).getOutputValue();
                sum += outputValueOfLastHiddenLayerNeuron * neuron.getWeightIn().get(i);
            }

            // Set the sum and output value for the neuron
            neuron.setSumOfInputValues(sum);
            //neuron.setOutputValue(Softmax.getProbability(nn, neuron_i));
            neuron.setOutputValue(activationFunction.activate(sum));

            // We're on the output layer so we can have an error because we have a supervised training method.
            // The error is the difference of the estimated and the actual output.
            neuron.setError(neuron.getEstimatedValue() - neuron.getOutputValue());

        }

        return nn;
    }

    /**
     * This function backpropagates through the whole network and adjusts the weights to get closer to the desired
     * output.
     *
     * @param nn Neural network which should be used
     * @return Improved neural network
     * @see NeuralNetwork
     */
    public NeuralNetwork backpropagate(NeuralNetwork nn) {
        /**
         * The general backpropagation formula is
         * DELTA W_ij = LearningRate * Delta_i * A_j
         *
         * DELTA W_ij is the value, which has to be added to the current weight of the connection between Neuron i and Neuron j
         * Learning Rate is a constant of the neural network
         * Delta_i is the delta of each neuron. The formula is notated below.
         * A_j is the output of the neuron j
         *
         * There are two ways to calculate the delta of a neuron. It's a difference if the neuron is
         * on the output or hidden layer.
         *
         * Output-Formula
         * delta_i = f'(sum of inputs) * Error
         *
         * Hidden-Formula
         * delta_i = f'(sum of inputs) * SUM_L [ delta_L * W_L_i ]
         */

        // First, we loop through each neuron in the output layer.
        for (int neuron_i = 0; neuron_i < nn.getOutputLayer().getCountOfNeurons(); neuron_i++) {
            Neuron neuron = nn.getOutputLayer().getNeurons().get(neuron_i);

            // We calculate the delta of the output neuron
            double delta_i = activationFunction.derivative(neuron.getSumOfInputValues()) * neuron.getError();
            // double delta_i = Softmax.derivative(neuron.getSumOfInputValues()) * neuron.getError();
            // The delta is needed later for calculating weights. That's why we need to save it.
            neuron.setDelta(delta_i);
            // Calculate the weight which has to be adjusted in a separate function. This function also adjusts the weights already.
            calculateDelta_W_ij(nn, nn.getHiddenLayers().get(nn.getHiddenLayers().size() - 1), neuron, neuron_i);
        }

        // Now that we processed the output layer and adjusted the weights between output layer and last hidden layer, we have to
        // loop through each hidden layer adjusting the weights between the hidden layers and the first hidden layer and input layer.

        // We start at the last hidden layer
        for (int hiddenLayer_i = nn.getHiddenLayers().size() - 1; hiddenLayer_i >= 0; hiddenLayer_i--) {
            HiddenLayer hiddenLayer = nn.getHiddenLayers().get(hiddenLayer_i);
            if (hiddenLayer_i == nn.getHiddenLayers().size() - 1 && hiddenLayer_i != 0) {
                // We're in the first loop but not in the last one which means there are multiple hidden layers.
                // --> We have to calculate the delta values of each neuron by the delta values of the output layer.
                // We start at 1 in order to exclude BIAS
                for (int i = 1; i < hiddenLayer.getCountOfNeurons(); i++) {
                    Neuron neuron = hiddenLayer.getNeurons().get(i);
                    // We already can calculate the first part of the delta_i
                    double delta_i = activationFunction.derivative(neuron.getSumOfInputValues());
                    double weightsMultipliedWithDeltaOfOutputLayer = 0.0;

                    // Now we can calculate the next part of the delta_i. Let's say we have two output neurons
                    // whose delta values are 0.05 and 0.07. Then we multiply each delta value with the weight out of
                    // the current neuron and add it all together.
                    for (int j = 0; j < neuron.getWeightOut().size(); j++) {
                        double deltaOfOutputNeuron = nn.getOutputLayer().getNeurons().get(j).getDelta();
                        double weightOutOfCurrentNeuron = neuron.getWeightOutTemp().get(j);
                        weightsMultipliedWithDeltaOfOutputLayer += deltaOfOutputNeuron * weightOutOfCurrentNeuron;
                    }

                    // Calculate the final delta_i and set it to save it.
                    delta_i *= weightsMultipliedWithDeltaOfOutputLayer;
                    neuron.setDelta(delta_i);
                    // We know we're in the first loop. That means, we need to adjust the weights between the
                    // second last hidden layer and the last hidden layer.
                    HiddenLayer secondLastHiddenLayer = nn.getHiddenLayers().get(hiddenLayer_i - 1);
                    calculateDelta_W_ij(nn, secondLastHiddenLayer, neuron, i - 1);
                }
            } else if (hiddenLayer_i == 0) {
                // We're in the last loop which means we're on the first hidden layer after the input layer.
                // We once again start at 1 to skip BIAS
                for (int i = 1; i < hiddenLayer.getCountOfNeurons(); i++) {
                    Neuron neuron = hiddenLayer.getNeurons().get(i);

                    // We can calculate the first part of the delta_i function really easy
                    double delta_i = activationFunction.derivative(neuron.getSumOfInputValues());
                    double weightsMultipliedWithDelta = 0.0;

                    for (int j = 0; j < neuron.getWeightOut().size(); j++) {
                        double delta;
                        // We're on the first hidden layer and need the delta values of the following layer.
                        // We need to check if we have multiple hidden layers. If we have, then we take the delta
                        // of the following hidden layer. Otherwise, we take the delta of the output layer.
                        if (nn.getHiddenLayers().size() == 1) {
                            delta = nn.getOutputLayer().getNeurons().get(j).getDelta();
                        } else {
                            delta = nn.getHiddenLayers().get(hiddenLayer_i + 1).getNeurons().get(j).getDelta();
                        }
                        double weightOutOfCurrentNeuron = neuron.getWeightOutTemp().get(j);
                        weightsMultipliedWithDelta += delta * weightOutOfCurrentNeuron;
                    }

                    // Calculate the final delta_i and set it to save it.
                    delta_i *= weightsMultipliedWithDelta;
                    neuron.setDelta(delta_i);
                    // We know we're in the last loop. That means, we need to adjust the weights between the
                    // input layer and the first hidden layer
                    calculateDelta_W_ij(nn, nn.getInputLayer(), neuron, i - 1);
                }
            } else {
                // We're neither in the first nor the last loop which means we're processing a hidden layer which is surrounded by
                // two hidden layers
                for (int i = 1; i < hiddenLayer.getCountOfNeurons(); i++) {
                    Neuron neuron = hiddenLayer.getNeurons().get(i);

                    // We can calculate the first part of the delta_i function
                    double delta_i = activationFunction.derivative(neuron.getSumOfInputValues());
                    double weightsMultipliedWithDelta = 0.0;

                    for (int j = 0; j < neuron.getWeightOut().size(); j++) {
                        // We're on a hidden layer between two hidden layers. That means we need the delta values of the
                        // layer after this current layer.
                        double delta = nn.getHiddenLayers().get(hiddenLayer_i + 1).getNeurons().get(j).getDelta();
                        double weightOutOfCurrentNeuron = neuron.getWeightOutTemp().get(j);
                        weightsMultipliedWithDelta += delta * weightOutOfCurrentNeuron;
                    }

                    // Calculate the final delta_i and set it to save it
                    delta_i *= weightsMultipliedWithDelta;
                    neuron.setDelta(delta_i);

                    // We know we're between two hidden layers so we need to adjust the weights between the
                    // previous hidden layer and the current layer.
                    calculateDelta_W_ij(nn, nn.getHiddenLayers().get(hiddenLayer_i - 1), neuron, i - 1);
                }
            }
        }
        return nn;
    }

    /**
     * Calculates the delta between two neurons. The result will be added to the weight between those two neurons.
     *
     * @param nn             Neural Network which is being processed
     * @param layer          We adjust a connection between two neurons from layer i and layer j. Layer j is the layer which is closer to the input layer
     *                       or the input layer itself.
     * @param neuron         Neuron is the neuron of the layer i = the layer which is closer to the output layer.
     * @param indexWeightOut Index of the weight out which has to be adjusted
     * @see NeuralNetwork
     */

    private void calculateDelta_W_ij(NeuralNetwork nn, Layer layer, Neuron neuron, int indexWeightOut) {
        // We adjust all weight ins of the given neuron.
        // That means, we have to loop through the previous layer and get all the output values because this is needed
        // for the delta_W_ij formula
        for (int j = 0; j < layer.getCountOfNeurons(); j++) {
            double delta_W_ij = nn.getLearningRate() * neuron.getDelta() * layer.getNeurons().get(j).getOutputValue();
            double oldValue = layer.getNeurons().get(j).getWeightOut().get(indexWeightOut);
            double oldDelta_W_ij = 0;
            if (neuron.getOldDeltaW_ij().get(indexWeightOut) != null) {
                oldDelta_W_ij = neuron.getOldDeltaW_ij().get(indexWeightOut);
            }

            neuron.getOldDeltaW_ij().put(indexWeightOut, delta_W_ij);

            /*double historyGradientSquares = 0.00000001;
            if (neuron.getGradientChanges().get(j) != null) {
                historyGradientSquares = Math.pow(neuron.getGradientChanges().get(j), 2);
            }
            double gradientChange = oldValue + (nn.getLearningRate() / Math.sqrt(historyGradientSquares)) * delta_W_ij;

            neuron.getGradientChanges().put(j, historyGradientSquares + delta_W_ij);*/

            // We need to adjust the weights. First we adjust the weight out of the previous layer neuron to the
            // given layer neuron.
            layer.getNeurons().get(j).getWeightOut().set(indexWeightOut, oldValue + delta_W_ij + (nn.getMomentum() * oldDelta_W_ij));
            // Now we adjust the weight in of the given layer neuron.
            neuron.getWeightIn().set(j, oldValue + delta_W_ij + (nn.getMomentum() * oldDelta_W_ij));
        }
    }

    /**
     * Calculates the delta between two neurons. The result will be added to the weight between those two neurons.
     *
     * @param nn             Neural Network which is being processed
     * @param layer          We adjust a connection between two neurons from layer i and layer j. Layer j is the layer which is closer to the input layer
     *                       or the input layer itself.
     * @param neuron         Neuron is the neuron of the layer i = the layer which is closer to the output layer.
     * @param indexWeightOut Index of the weight out which has to be adjusted
     * @see NeuralNetwork
     */
    /*private void calculateDelta_W_ij(NeuralNetwork nn, Layer layer, Neuron neuron, int indexWeightOut) {
        // We adjust all weight ins of the given neuron.
        // That means, we have to loop through the previous layer and get all the output values because this is needed
        // for the delta_W_ij formula
        for (int j = 0; j < layer.getCountOfNeurons(); j++) {
            double delta_W_ij = nn.getLearningRate() * neuron.getDelta() * layer.getNeurons().get(j).getOutputValue();
            double oldValue = layer.getNeurons().get(j).getWeightOut().get(indexWeightOut);
            // We need to adjust the weights. First we adjust the weight out of the previous layer neuron to the
            // given layer neuron.
            layer.getNeurons().get(j).getWeightOut().set(indexWeightOut, oldValue + delta_W_ij);
            // Now we adjust the weight in of the given layer neuron.
            neuron.getWeightIn().set(j, oldValue + delta_W_ij);
        }
    }*/


}
