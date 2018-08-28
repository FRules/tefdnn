package de.nitschmann.tefdnn.presentation;

import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.persistence.Database;

import static de.nitschmann.tefdnn.presentation.Parser.parseString;

public class Saver {

    /**
     * Saves the neural network by the system name
     * @param neuralNetworkName desired name of the neural network
     * @param neuralNetwork specified neural network
     * @param database database connection which should be used
     * @return true if storing was successful
     */
    private boolean save(String neuralNetworkName, NeuralNetwork neuralNetwork, Database database) {
        neuralNetwork.setName(neuralNetworkName);
        return database.save(neuralNetwork);
    }

    /**
     * parses the input string and calls the required save methods
     * @param database database connection which should be used
     * @param input input string which will be parsed
     * @param neuralNetwork specified neural network
     * @return true if storing was successful
     */
    public boolean saveNeuralNetwork(Database database, String input, NeuralNetwork neuralNetwork) {
        if (input.contains("-nff:")) {
            String nameFF = parseString(input, "-nff:");
            return save(nameFF, neuralNetwork, database);
        }
        printHelp();
        return false;
    }

    /**
     * saves the result of a trained network to the database
     * @param database database connection which should be used
     * @param neuralNetwork specified neural network
     * @param pathToImage path to image which was tested
     * @param result result string of the output layer
     * @return true if storing the result was successful
     */
    public boolean saveResult(Database database, NeuralNetwork neuralNetwork, String pathToImage, String result) {
        return database.saveResult(neuralNetwork, pathToImage, result);
    }

    /**
     * prints the help of the save command
     */
    private void printHelp() {
        System.out.println("Not all parameters were specified. Save takes the following arguments.");
        System.out.println("   -nFF:\tName of feedforward network");
        System.out.println("At least nFF needs to be specified.");
    }


}
