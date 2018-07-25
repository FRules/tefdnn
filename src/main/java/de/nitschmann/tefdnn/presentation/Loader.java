package de.nitschmann.tefdnn.presentation;

import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.application.NeuralNetworkType;
import de.nitschmann.tefdnn.application.TrainingEnvironment;
import de.nitschmann.tefdnn.persistence.Database;

import static de.nitschmann.tefdnn.presentation.Parser.parseInt;
import static de.nitschmann.tefdnn.presentation.Parser.parseString;

public class Loader {

    /**
     * Initializes a complete new training environment
     * @param name name of the training environment
     * @param numberOfInputNeurons
     * @param numberOfHiddenNeurons
     * @param numberOfOutputNeurons
     * @param numberOfHiddenLayers
     * @param numberOfHiddenNeuronsAutoEncoder
     * @return
     */
    private TrainingEnvironment init(String name, int numberOfInputNeurons, int numberOfHiddenNeurons, int numberOfOutputNeurons, int numberOfHiddenLayers, int numberOfHiddenNeuronsAutoEncoder) {
        NeuralNetwork ae = new NeuralNetwork();
        ae.setNeuralNetworkType(NeuralNetworkType.AUTOENCODER);
        ae.initNetwork(numberOfInputNeurons, numberOfHiddenNeuronsAutoEncoder, numberOfInputNeurons, 1);
        NeuralNetwork ff = new NeuralNetwork();
        ff.setNeuralNetworkType(NeuralNetworkType.FEEDFORWARD);
        ff.initNetwork(numberOfInputNeurons, numberOfHiddenNeurons, numberOfOutputNeurons, numberOfHiddenLayers);
        return new TrainingEnvironment(name, ff, ae);
    }

    /**
     * Initializes a complete new training environment which does not include an autoencoder
     * @param name name of the training environment
     * @param numberOfInputNeurons
     * @param numberOfHiddenNeurons
     * @param numberOfOutputNeurons
     * @param numberOfHiddenLayers
     * @return
     */
    private TrainingEnvironment init(String name, int numberOfInputNeurons, int numberOfHiddenNeurons, int numberOfOutputNeurons, int numberOfHiddenLayers) {
        NeuralNetwork ff = new NeuralNetwork();
        ff.setNeuralNetworkType(NeuralNetworkType.FEEDFORWARD);
        ff.initNetwork(numberOfInputNeurons, numberOfHiddenNeurons, numberOfOutputNeurons, numberOfHiddenLayers);
        return new TrainingEnvironment(name, ff);
    }

    /**
     * Initializes a complete new training environment without specifying the name
     * @param numberOfInputNeurons
     * @param numberOfHiddenNeurons
     * @param numberOfOutputNeurons
     * @param numberOfHiddenLayers
     * @param numberOfHiddenNeuronsAutoEncoder
     * @return
     */
    private TrainingEnvironment init(int numberOfInputNeurons, int numberOfHiddenNeurons, int numberOfOutputNeurons, int numberOfHiddenLayers, int numberOfHiddenNeuronsAutoEncoder) {
        NeuralNetwork ae = new NeuralNetwork();
        ae.setNeuralNetworkType(NeuralNetworkType.AUTOENCODER);
        ae.initNetwork(numberOfInputNeurons, numberOfHiddenNeuronsAutoEncoder, numberOfInputNeurons, 1);
        NeuralNetwork ff = new NeuralNetwork();
        ff.setNeuralNetworkType(NeuralNetworkType.FEEDFORWARD);
        ff.initNetwork(numberOfInputNeurons, numberOfHiddenNeurons, numberOfOutputNeurons, numberOfHiddenLayers);
        return new TrainingEnvironment(ff, ae);
    }

    /**
     * Initializes a complete new training environment without specifying the name and without the autoencoder
     * @param numberOfInputNeurons
     * @param numberOfHiddenNeurons
     * @param numberOfOutputNeurons
     * @param numberOfHiddenLayers
     * @return
     */
    private TrainingEnvironment init(int numberOfInputNeurons, int numberOfHiddenNeurons, int numberOfOutputNeurons, int numberOfHiddenLayers) {
        NeuralNetwork ff = new NeuralNetwork();
        ff.setNeuralNetworkType(NeuralNetworkType.FEEDFORWARD);
        ff.initNetwork(numberOfInputNeurons, numberOfHiddenNeurons, numberOfOutputNeurons, numberOfHiddenLayers);
        return new TrainingEnvironment(ff);
    }

    /**
     * loads a training environment from the database by specifying the training environment name
     * @param nameOfSystem
     * @param database
     * @return
     */
    private TrainingEnvironment init(String nameOfSystem, Database database) {
        return database.initTrainingEnvironment(nameOfSystem);
    }

    /**
     * loads just the feedforward network in a training environment by specifying the name of the feedforward network
     * @param nameFeedforward
     * @param database
     * @return
     */
    private TrainingEnvironment initJustFeedforward(String nameFeedforward, Database database) {
        return database.initTrainingEnvironmentJustFeedforward(nameFeedforward);
    }

    /**
     * loads a training environment by specifying the names of the feedforward and autoencoder network
     * @param nameOfFeedForwardNetwork
     * @param nameOfAutoEncoderNetwork
     * @param database
     * @return
     */
    private TrainingEnvironment init(String nameOfFeedForwardNetwork, String nameOfAutoEncoderNetwork, Database database) {
        return database.initTrainingEnvironment(nameOfFeedForwardNetwork, nameOfAutoEncoderNetwork);
    }

    /**
     * parses the input string and calls the required init methods
     * @param database
     * @param input
     * @return
     */
    public TrainingEnvironment initEnvironment(Database database, String input) {
        if (input.contains("-n:") || input.contains("-cin:") || input.contains("-chn:") || input.contains("-con:") || input.contains("-chl:") || input.contains("-chnae:")) {
            /* User wants to initialize a completely empty training environment. That means, we will generate a new one.
               In order to guarantee success, we need to check if user did specify all the information. */
            if (input.contains("-cin:") && input.contains("-chn:") && input.contains("-con:") && input.contains("-chl:") && input.contains("-chnae:")) {
                String name = parseString(input, "-n:");
                int numberOfInputNeurons = parseInt(input, "-cin:");
                int numberOfHiddenNeurons = parseInt(input, "-chn:");
                int numberOfOutputNeurons = parseInt(input, "-con:");
                int numberOfHiddenLayers = parseInt(input, "-chl:");
                int numberOfHiddenNeuronsAutoEncoder = parseInt(input, "-chnae:");

                if (name == null) {
                    return init(numberOfInputNeurons, numberOfHiddenNeurons, numberOfOutputNeurons, numberOfHiddenLayers, numberOfHiddenNeuronsAutoEncoder);
                } else {
                    return init(name, numberOfInputNeurons, numberOfHiddenNeurons, numberOfOutputNeurons, numberOfHiddenLayers, numberOfHiddenNeuronsAutoEncoder);
                }
            } if (input.contains("-cin:") && input.contains("-chn:") && input.contains("-con:") && input.contains("-chl:")) {
                String name = parseString(input, "-n:");
                int numberOfInputNeurons = parseInt(input, "-cin:");
                int numberOfHiddenNeurons = parseInt(input, "-chn:");
                int numberOfOutputNeurons = parseInt(input, "-con:");
                int numberOfHiddenLayers = parseInt(input, "-chl:");

                if (name == null) {
                    return init(numberOfInputNeurons, numberOfHiddenNeurons, numberOfOutputNeurons, numberOfHiddenLayers);
                } else {
                    return init(name, numberOfInputNeurons, numberOfHiddenNeurons, numberOfOutputNeurons, numberOfHiddenLayers);
                }
            } else {
                printHelp();
                return null;
            }
        } else if (input.contains("-ns:")) {
            String name = parseString(input, "-ns:");
            return init(name, database);
        } else if (input.contains("-nff") && input.contains("-nae:")) {
            String nameFeedForward = parseString(input, "-nff:");
            String nameAutoEncoder = parseString(input, "-nae:");
            return init(nameFeedForward, nameAutoEncoder, database);
        } else if (input.contains("-nff:")) {
            String name = parseString(input, "-nff:");
            return initJustFeedforward(name, database);
        }

        printHelp();
        return null;
    }

    /**
     * prints the help of the init command
     */
    private void printHelp() {
        System.out.println("Not all parameters were specified. Init takes the following arguments.");
        System.out.println("   For empty initialization");
        System.out.println("      -n:\tName of training environment, optional");
        System.out.println("      -cIN:\tNumber of input layer neurons, required");
        System.out.println("      -cHN:\tNumber of hidden layer neurons, required");
        System.out.println("      -cON:\tNumber of output layer neurons, required");
        System.out.println("      -cHL:\tNumber of hidden layers, required");
        System.out.println("      -cHNAE:\tNumber of hidden layer neurons on autoencoder, required");
        System.out.println("   For loading a neural network by training environment name");
        System.out.println("      -nS:\tName of training environment");
        System.out.println("   For loading a neural network by feedforward and autoencoder name");
        System.out.println("      -nFF:\tName of feedforward network");
        System.out.println("      -nAE:\tName of autoencoder network");
        System.out.println("   For loading a neural network via json file");
        System.out.println("      -json:\tPath to json file");
    }
}
