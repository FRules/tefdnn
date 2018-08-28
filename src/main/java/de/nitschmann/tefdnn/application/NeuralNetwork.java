package de.nitschmann.tefdnn.application;

import de.nitschmann.tefdnn.application.training.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NeuralNetwork {

    private String name;
    private InputLayer inputLayer;
    private HiddenLayer hiddenLayer;
    private List<HiddenLayer> hiddenLayers;
    private OutputLayer outputLayer;
    private double[][] trainSet;
    private double[][] estimatedResults;
    private TrainingType trainingType;
    private ActivationFunctionType activationFunctionType;
    private IActivationFunction activationFunction;
    private int maxEpoch;
    private double targetLoss;
    private double learningRate;
    private double momentum;
    private double meanImage;

    /**
     * Since we have a copy constructor we need this to initialize an empty neural network
     */
    public NeuralNetwork() {

    }

    /**
     * Copy Constructor.
     * @param nn
     * Neural network which should be copied
     * @see NeuralNetwork
     */
    public NeuralNetwork(NeuralNetwork nn) {
        this.learningRate = nn.getLearningRate();
        this.maxEpoch = nn.getMaxEpoch();
        this.targetLoss = nn.getTargetLoss();
        this.activationFunction = nn.activationFunction;
        this.trainingType = nn.getTrainingType();
        this.meanImage = nn.getMeanImage();
        this.inputLayer = new InputLayer(nn.getInputLayer());
        this.outputLayer = new OutputLayer(nn.getOutputLayer());
        ArrayList<HiddenLayer> copiedHiddenLayers = new ArrayList<>();
        for (HiddenLayer hiddenLayer : nn.getHiddenLayers()) {
            copiedHiddenLayers.add(new HiddenLayer(hiddenLayer));
        }
        this.hiddenLayers = copiedHiddenLayers;
        try {
            this.setTrainSet(nn.getTrainSet().clone());

        } catch (NullPointerException ignored) { }

        try {
            this.setEstimatedResults(nn.getEstimatedResults().clone());

        } catch (NullPointerException ignored) { }

    }

    /**
     * This method calls the initTwoInputTwoHiddenTwoOutputNeurons function of the layers. The weights of the layers will be random initialized.
     * @param countOfHiddenLayers
     * Count of hidden layers in neural network
     * @param countOfNeuronsInHiddenLayer
     * Count of neurons in each hidden layer. Every hidden layer has the same amount of neurons.
     */
    private void initLayers(int countOfHiddenLayers, int countOfNeuronsInHiddenLayer) {
        inputLayer.initLayer(inputLayer);

        if (countOfHiddenLayers > 0) {
            hiddenLayer.initLayer(hiddenLayers, inputLayer);
        }

        outputLayer.initLayer(outputLayer, countOfNeuronsInHiddenLayer+1);
    }

    /**
     * This method calls the initTwoInputTwoHiddenTwoOutputNeurons function of the layers but with the addition of given input weights. This method can initialize the whole
     * neural network with pre-set weights.
     * @param outputsInputLayer
     * ArrayList which defines the output values of each neuron in the hidden layer. If this ArrayList contains 5 doubles, the input layer will have
     * a size of 6 (5 + 1 BIAS Neuron)
     * @param weightsHiddenLayer
     * Defines the weights of each hidden Layer. It's an Arraylist which contains a map which contains the weights of each neuron in another arraylist. The
     * structure is like this:
     * ArrayList which defines Hidden Layers
     *  [0] - Hidden Layer 1
     *   |  [0] - Map 1
     *   |   |  [0] - ArrayList for Neuron 1 which contains Input weights
     *   |   |   |  [0] - 0.2
     *   |   |   |  [1] - 0.5
     *   |   |   |  [2] - 0.1
     *   |  [1] - Map 2
     *   |   |  [0] - ArrayList for Neuron 2 which contains Input weights
     *   |   |   |  [0] - 0.1
     *   |   |   |  [1] - 0.2
     *   |   |   |  [2] - 0.3
     *  [1] - Hidden Layer 2
     *  ...
     * @param weightsOutputLayer
     * Defines the weights of each output Layer. Its a map which contains the weights of each neuron in another ArrayList. The structure is like this:
     * [0] - Map 1
     *   |   |  [0] - ArrayList for Neuron 1 which contains Input weights
     *   |   |   |  [0] - 0.1
     *   |   |   |  [1] - 0.2
     *   |   |   |  [2] - 0.6
     *   |  [1] - Map 2
     *   |   |  [0] - ArrayList for Neuron 2 which contains Input weights
     *   |   |   |  [0] - 0.9
     *   |   |   |  [1] - 0.1
     *   |   |   |  [2] - 0.5
     *   ...
     */
    private void initLayers(List<Double> outputsInputLayer, List<Map<Integer, List<Double>>> weightsHiddenLayer, Map<Integer, List<Double>> weightsOutputLayer) {
        inputLayer.initLayer(inputLayer, outputsInputLayer);

        if (weightsHiddenLayer.size() > 0) {
            hiddenLayer.initLayer(hiddenLayers, weightsHiddenLayer);
        }

        outputLayer.initLayer(outputLayer, weightsOutputLayer);
    }

    /**
     * We have to connect all the layers. Until this method gets called, we only initialized the layers itself but not
     * the connection between them. Also, we initialized only the input weights of each layer. With that information, we can
     * calculate the output weights of each neuron in each layer. The output of each layer equals the input of the following layer.
     * Output of 1st Hidden Layer = Input of 2nd Hidden Layer
     * Output of 2nd Hidden Layer = Input of 3rd Hidden Layer
     * ...
     * Output of nth Hidden Layer = Input of Output Layer
     * @param countOfHiddenLayers
     * Amount of hidden layers in the neural network
     */
    private void connectLayers(int countOfHiddenLayers) {
        // First, we have to connect the input layer to the first hidden layer.
        // We loop through each neuron in input layer
        for(int i = 0; i < inputLayer.getCountOfNeurons(); i++) {
            ArrayList<Double> weightOut = new ArrayList<>();
            // Now, we loop through each neuron in the first hidden Layer except the BIAS
            // The BIAS has no weight in values. We take these weight in values and add them
            // to the weight out list
            for(int j = 1; j < hiddenLayers.get(0).getCountOfNeurons(); j++) {
                weightOut.add(hiddenLayers.get(0).getNeurons().get(j).getWeightIn().get(i));
            }
            // Add the weight outs to the input neuron
            inputLayer.getNeurons().get(i).setWeightOut(weightOut);
        }

        if (countOfHiddenLayers > 1) {
            // If there's more than one hidden layer, the output weights of the hidden layers must be connected to the input weights of the following hidden layer.
            // Exception: The output weights of the last hidden layer must be connected to the input weights of the output layer.
            for(int hiddenLayer_i = 0; hiddenLayer_i < countOfHiddenLayers; hiddenLayer_i++) {
                if (hiddenLayer_i != countOfHiddenLayers - 1) {
                    for(int i = 0; i < hiddenLayers.get(hiddenLayer_i).getCountOfNeurons(); i++) {
                        ArrayList<Double> weightOut = new ArrayList<>();
                        // Once again, we start at 1 in order to skip the BIAS neuron since it has no weight ins.
                        for (int j = 1; j < hiddenLayers.get(hiddenLayer_i + 1).getCountOfNeurons(); j++) {
                            weightOut.add(hiddenLayers.get(hiddenLayer_i + 1).getNeurons().get(j).getWeightIn().get(i));
                        }
                        hiddenLayers.get(hiddenLayer_i).getNeurons().get(i).setWeightOut(weightOut);
                    }
                } else {
                    // Last layer --> we have to connect the output weights of this layer with the weight ins of the output layer
                    for(int i = 0; i < hiddenLayers.get(hiddenLayer_i).getCountOfNeurons(); i++) {
                        ArrayList<Double> weightOut = new ArrayList<>();
                        for(int j = 0; j < outputLayer.getCountOfNeurons(); j++) {
                            weightOut.add(outputLayer.getNeurons().get(j).getWeightIn().get(i));
                        }
                        hiddenLayers.get(hiddenLayer_i).getNeurons().get(i).setWeightOut(weightOut);
                    }
                }
            }
        } else {
            // If there's only one hidden layer the output weights of the hidden layer must be connected with the input weights of the output layer.
            for (int i = 0; i < hiddenLayers.get(0).getCountOfNeurons(); i++) {
                ArrayList<Double> weightOut = new ArrayList<>();
                for (int j = 0; j < outputLayer.getCountOfNeurons(); j++) {
                    weightOut.add(outputLayer.getNeurons().get(j).getWeightIn().get(i));
                }
                hiddenLayers.get(0).getNeurons().get(i).setWeightOut(weightOut);
            }
        }
    }

    /**
     * This method initializes the whole neural network but with the addition of given input weights.
     * @param outputsInputLayer
     * ArrayList which defines the output values of each neuron in the hidden layer. If this ArrayList contains 5 doubles, the input layer will have
     * a size of 6 (5 + 1 BIAS Neuron)
     * @param weightsHiddenLayer
     * Defines the weights of each hidden Layer. It's an Arraylist which contains a map which contains the weights of each neuron in another arraylist. The
     * structure is like this:
     * ArrayList which defines Hidden Layers
     *  [0] - Hidden Layer 1
     *   |  [0] - Map 1
     *   |   |  [0] - ArrayList for Neuron 1 which contains Input weights
     *   |   |   |  [0] - 0.2
     *   |   |   |  [1] - 0.5
     *   |   |   |  [2] - 0.1
     *   |  [1] - Map 2
     *   |   |  [0] - ArrayList for Neuron 2 which contains Input weights
     *   |   |   |  [0] - 0.1
     *   |   |   |  [1] - 0.2
     *   |   |   |  [2] - 0.3
     *  [1] - Hidden Layer 2
     *  ...
     * @param weightsOutputLayer
     * Defines the weights of each output Layer. Its a map which contains the weights of each neuron in another ArrayList. The structure is like this:
     * [0] - Map 1
     *   |   |  [0] - ArrayList for Neuron 1 which contains Input weights
     *   |   |   |  [0] - 0.1
     *   |   |   |  [1] - 0.2
     *   |   |   |  [2] - 0.6
     *   |  [1] - Map 2
     *   |   |  [0] - ArrayList for Neuron 2 which contains Input weights
     *   |   |   |  [0] - 0.9
     *   |   |   |  [1] - 0.1
     *   |   |   |  [2] - 0.5
     *   ...
     */
    public void initNetwork(List<Double> outputsInputLayer, List<Map<Integer, List<Double>>> weightsHiddenLayer, Map<Integer, List<Double>> weightsOutputLayer) {
        inputLayer = new InputLayer();
        inputLayer.setCountOfNeurons(outputsInputLayer.size());

        outputLayer = new OutputLayer();
        outputLayer.setCountOfNeurons(weightsOutputLayer.size());

        hiddenLayers = new ArrayList<>();

        for (Map<Integer, List<Double>> aWeightsHiddenLayer : weightsHiddenLayer) {
            hiddenLayer = new HiddenLayer();
            hiddenLayer.setCountOfNeurons(aWeightsHiddenLayer.size());
            hiddenLayers.add(hiddenLayer);
        }

        initLayers(outputsInputLayer, weightsHiddenLayer, weightsOutputLayer);

        connectLayers(weightsHiddenLayer.size());
    }

    /**
     * This method initializes the whole neural network with random weights.
     * @param countOfNeuronsInInputLayer
     * Amount of neurons in input layer.
     * @param countOfNeuronsInHiddenLayer
     * Amount of neurons in each hidden layer.
     * @param countOfNeuronsInOutputLayer
     * Amount of neurons in output layer.
     * @param countOfHiddenLayers
     * Amount of hidden layers.
     */
    public void initNetwork(int countOfNeuronsInInputLayer, int countOfNeuronsInHiddenLayer, int countOfNeuronsInOutputLayer, int countOfHiddenLayers) {
        inputLayer = new InputLayer();
        inputLayer.setCountOfNeurons(countOfNeuronsInInputLayer);

        outputLayer = new OutputLayer();
        outputLayer.setCountOfNeurons(countOfNeuronsInOutputLayer);

        hiddenLayers = new ArrayList<>();

        for(int i = 0; i < countOfHiddenLayers; i++) {
            hiddenLayer = new HiddenLayer();
            hiddenLayer.setCountOfNeurons(countOfNeuronsInHiddenLayer);
            hiddenLayers.add(hiddenLayer);
        }

        initLayers(countOfHiddenLayers, countOfNeuronsInHiddenLayer);

        connectLayers(countOfHiddenLayers);
    }

    /**
     * @return The input layer in the neural network
     * @see InputLayer
     */
    public InputLayer getInputLayer() {
        return this.inputLayer;
    }

    /**
     * sets the activation function
     * @param activationFunctionType activation function type
     */
    public void setActivationFunction(ActivationFunctionType activationFunctionType) {
        this.activationFunctionType = activationFunctionType;
        switch(activationFunctionType) {
            case RELU:
                this.activationFunction = new Relu();
                break;
            case SIGMOID:
                this.activationFunction = new Sigmoid();
                break;
            case TANH:
                this.activationFunction = new Tanh();
                break;
            default:
                this.activationFunction = new Relu();
                break;
        }
    }

    public ActivationFunctionType getActivationFunctionType() {
        return this.activationFunctionType;
    }

    /**
     * @return All hidden layerse in the neural network
     * @see ArrayList<HiddenLayer>s
     */
    public List<HiddenLayer> getHiddenLayers() {
        return this.hiddenLayers;
    }

    /**
     * @return The output layer in the neural network
     * @see OutputLayer
     */
    public OutputLayer getOutputLayer() {
        return this.outputLayer;
    }

    /**
     * @return The train set
     */
    public double[][] getTrainSet() {
        return trainSet;
    }

    /**
     * sets the mean image
     * @param meanImage mean image
     */
    public void setMeanImage(double meanImage) {
        this.meanImage = meanImage;
    }

    /**
     * returns the mean image
     * @return mean image
     */
    public double getMeanImage() {
        return this.meanImage;
    }

    /**
     * @param trainSet sets the train set
     */
    public void setTrainSet(double[][] trainSet) {
        this.trainSet = trainSet;
    }

    /**
     * @return The estimated results of the train set
     */
    public double[][] getEstimatedResults() {
        return this.estimatedResults;
    }

    /**
     * @param estimatedResults sets the estimated results of the train set
     */
    public void setEstimatedResults(double[][] estimatedResults) {
        this.estimatedResults = estimatedResults;
    }

    /**
     * @param trainingType sets the training type which should be used
     */
    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    /**
     * @return the train type which is used
     */
    public TrainingType getTrainingType() {
        return this.trainingType;
    }

    /**
     * Initial call of the training of the network
     * @param nn
     * The Neural network which should learn
     * @return
     * The trained neural network
     */
    public NeuralNetwork train(NeuralNetwork nn) {
        if (nn.getTrainingType() == TrainingType.BACKPROPAGATION) {
            Backpropagation backpropagation = new Backpropagation(this.activationFunction);
            return new NeuralNetwork(backpropagation.train(nn));
        }

        return null;
    }

    /**
     * Does basically the same as train but logs the information for sensitivity analysis
     * @param nn
     * The neural network which should learn / which we need to fetch sensitivity data from
     * @param filename
     * The name of the generated csv file (scenario_01.csv for example)
     */
    public void sensitivityAnalysis(NeuralNetwork nn, String filename) throws IOException {
        if (nn.getTrainingType() == TrainingType.BACKPROPAGATION) {
            Backpropagation backpropagation = new Backpropagation(this.activationFunction);
            new NeuralNetwork(backpropagation.sensitivityAnalysis(nn, filename));
        }

    }

    /**
     * @param learningRate sets the learning rate
     */
    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }


    /**
     * @return the learning rate of the network
     */
    public double getLearningRate() {
        return this.learningRate;
    }

    /**
     * Sets the input of the neural network. The purpose of this method is to
     * change the inputs of the input neurons in order to test the network.
     * @param nn
     * The neural network which should be tested
     * @param input
     * Input data which should have the same length as the number of input neurons
     * minus 1 (BIAS)
     */
    public void setInput(NeuralNetwork nn, double[] input) {
        if (input.length != nn.getInputLayer().getCountOfNeurons() -1) {
            return;
        }

        for(int i = 1; i < nn.getInputLayer().getCountOfNeurons(); i++) {
            Neuron neuron = nn.getInputLayer().getNeurons().get(i);
            neuron.setSumOfInputValues(input[i-1]);
            neuron.setOutputValue(input[i-1]);
        }
    }

    public int getMaxEpoch() {
        return maxEpoch;
    }

    public void setMaxEpoch(int maxEpoch) {
        this.maxEpoch = maxEpoch;
    }

    public double getTargetLoss() {
        return targetLoss;
    }

    public void setTargetLoss(double targetLoss) {
        this.targetLoss = targetLoss;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    public double getMomentum() {
        return this.momentum;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Tests the forward pass of the neural network.
     * @param nn
     * The neural network which should be tested
     * @return
     * The output values of the neurons in the output layer
     */
    public ArrayList<Double> test(NeuralNetwork nn) {
        // We copy the network first because we dont want to change sum of input values or outputs in the original
        // network
        NeuralNetwork testNetwork = new NeuralNetwork(nn);

        if (testNetwork.getTrainingType() == TrainingType.BACKPROPAGATION) {
            Backpropagation b = new Backpropagation(this.activationFunction);
            testNetwork = b.forward(testNetwork);
        }

        ArrayList<Double> outputs = new ArrayList<>();
        for(Neuron neuron : testNetwork.getOutputLayer().getNeurons()) {
            outputs.add(neuron.getOutputValue());
        }
        return outputs;
    }

    public boolean equalsStructureAndConfiguration(Object o) {
        NeuralNetwork nn;
        try {
            nn = (NeuralNetwork) o;
        } catch (ClassCastException c) {
            return false;
        }

        if (nn.getHiddenLayers().size() != this.hiddenLayers.size()) {
            return false;
        }

        if (nn.getInputLayer().getCountOfNeurons() != this.inputLayer.getCountOfNeurons()) {
            return false;
        }

        for (int i = 0; i < nn.getHiddenLayers().size(); i++) {
            if (nn.getHiddenLayers().get(i).getCountOfNeurons() != this.getHiddenLayers().get(i).getCountOfNeurons()) {
                return false;
            }
        }

        if (nn.getOutputLayer().getCountOfNeurons() != this.outputLayer.getCountOfNeurons()) {
            return false;
        }

        if (nn.getActivationFunctionType() != null && this.activationFunctionType != null) {
            if (nn.getActivationFunctionType() != this.activationFunctionType) {
                return false;
            }
        } else if (nn.getActivationFunctionType() == null && this.activationFunctionType != null) {
            return false;
        }

        if (nn.getTrainingType() != null && this.trainingType != null) {
            if (!Objects.equals(nn.getTrainingType().toString(), this.trainingType.toString())) {
                return false;
            }
        } else if (nn.getTrainingType() == null && this.trainingType != null) {
            return false;
        }

        return !(nn.getLearningRate() != this.learningRate) &&
                nn.getMaxEpoch() == this.maxEpoch &&
                !(nn.getMomentum() != this.momentum) &&
                !(nn.getTargetLoss() != this.targetLoss);

    }

    @Override
    public boolean equals(Object o) {
        if (getClass() != o.getClass()) {
            return false;
        }

        NeuralNetwork nn;
        try {
            nn = (NeuralNetwork) o;
        } catch (ClassCastException c) {
            return false;
        }

        if (nn.getHiddenLayers().size() != this.hiddenLayers.size()) {
            return false;
        }

        if (!nn.getInputLayer().equals(this.inputLayer)) {
            return false;
        }

        for (int i = 0; i < nn.getHiddenLayers().size(); i++) {
            if (!nn.getHiddenLayers().get(i).equals(this.getHiddenLayers().get(i))) {
                return false;
            }
        }

        if (!nn.getOutputLayer().equals(this.outputLayer)) {
            return false;
        }

        if (nn.getActivationFunctionType() != null && this.activationFunctionType != null) {
            if (nn.getActivationFunctionType() != this.activationFunctionType) {
                return false;
            }
        } else if (nn.getActivationFunctionType() == null && this.activationFunctionType != null) {
            return false;
        }

        if (nn.getTrainingType() != null && this.trainingType != null) {
            if (!Objects.equals(nn.getTrainingType().toString(), this.trainingType.toString())) {
                return false;
            }
        } else if (nn.getTrainingType() == null && this.trainingType != null) {
            return false;
        }

        return !(nn.getLearningRate() != this.learningRate) &&
                nn.getMaxEpoch() == this.maxEpoch &&
                !(nn.getMomentum() != this.momentum) &&
                !(nn.getTargetLoss() != this.targetLoss);
    }
}
