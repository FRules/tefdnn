package de.nitschmann.tefdnn.presentation;

import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.persistence.Database;

import static de.nitschmann.tefdnn.presentation.Parser.parseInt;
import static de.nitschmann.tefdnn.presentation.Parser.parseString;

public class Loader {

    /**
     * Initializes a complete new neural network
     * @param name name of the neural network
     * @param numberOfInputNeurons
     * @param numberOfHiddenNeurons
     * @param numberOfOutputNeurons
     * @param numberOfHiddenLayers
     * @return
     */
    private NeuralNetwork init(String name, int numberOfInputNeurons, int numberOfHiddenNeurons, int numberOfOutputNeurons, int numberOfHiddenLayers) {
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        neuralNetwork.initNetwork(numberOfInputNeurons, numberOfHiddenNeurons, numberOfOutputNeurons, numberOfHiddenLayers);
        neuralNetwork.setName(name);
        return neuralNetwork;
    }

    /**
     * Initializes a complete new neural network without specifying the name
     * @param numberOfInputNeurons
     * @param numberOfHiddenNeurons
     * @param numberOfOutputNeurons
     * @param numberOfHiddenLayers
     * @return
     */
    private NeuralNetwork init(int numberOfInputNeurons, int numberOfHiddenNeurons, int numberOfOutputNeurons, int numberOfHiddenLayers) {
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        neuralNetwork.initNetwork(numberOfInputNeurons, numberOfHiddenNeurons, numberOfOutputNeurons, numberOfHiddenLayers);
        return neuralNetwork;
    }

    /**
     * loads the neural network by specifying the name of the feedforward network
     * @param nameFeedforward
     * @param database
     * @return
     */
    private NeuralNetwork init(String nameFeedforward, Database database) {
        return database.initNeuralNetwork(nameFeedforward);
    }

    /**
     * parses the input string and calls the required init methods
     * @param database
     * @param input
     * @return
     */
    public NeuralNetwork initNeuralNetwork(Database database, String input) {
        if (input.contains("-n:") || input.contains("-cin:") || input.contains("-chn:") || input.contains("-con:") || input.contains("-chl:")) {
            /* User wants to initialize a completely empty neural network. That means, we will generate a new one.
               In order to guarantee success, we need to check if user did specify all the information. */
            if (input.contains("-cin:") && input.contains("-chn:") && input.contains("-con:") && input.contains("-chl:")) {
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
        } else if (input.contains("-nff:")) {
            String nameFeedForward = parseString(input, "-nff:");
            return init(nameFeedForward, database);
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
        System.out.println("      -n:\tName of neural network, optional");
        System.out.println("      -cIN:\tNumber of input layer neurons, required");
        System.out.println("      -cHN:\tNumber of hidden layer neurons, required");
        System.out.println("      -cON:\tNumber of output layer neurons, required");
        System.out.println("      -cHL:\tNumber of hidden layers, required");
        System.out.println("   For loading a neural network by name");
        System.out.println("      -nFF:\tName of feedforward network");
        System.out.println("   For loading a neural network via json file");
        System.out.println("      -json:\tPath to json file");
    }
}
