package de.nitschmann.tefdnn.presentation;

import de.nitschmann.tefdnn.application.TrainingEnvironment;
import de.nitschmann.tefdnn.persistence.Database;

import static de.nitschmann.tefdnn.presentation.Parser.parseString;

public class Saver {

    /**
     * Saves the training environment by the system name
     * @param systemName desired name of the training environment
     * @param trainingEnvironment specified trainingEnvironment
     * @param database database connection which should be used
     * @return
     */
    private boolean save(String systemName, TrainingEnvironment trainingEnvironment, Database database) {
        trainingEnvironment.setName(systemName);
        return database.save(trainingEnvironment);
    }

    /**
     * Saves just the feedforward network of the training environment
     * @param feedforwardName desired name of the feedforward network
     * @param trainingEnvironment specified training environment
     * @param database database connection which should be used
     * @return
     */
    private boolean saveJustFeedforward(String feedforwardName, TrainingEnvironment trainingEnvironment, Database database) {
        trainingEnvironment.getFeedForwardNetwork().setName(feedforwardName);
        return database.saveJustFeedforward(trainingEnvironment);
    }

    /**
     * saves the training environment by the system-, feedforward- and autoencoder name
     * @param systemName desired name of the training environment
     * @param feedForwardName desired name of the feedforward network
     * @param autoEncoderName desired name of the autoencoder network
     * @param trainingEnvironment specified training environment
     * @param database database connection which should be used
     * @return
     */
    private boolean save(String systemName, String feedForwardName, String autoEncoderName,  TrainingEnvironment trainingEnvironment, Database database) {
        trainingEnvironment.setName(systemName);
        trainingEnvironment.getAutoEncoderNetwork().setName(autoEncoderName);
        trainingEnvironment.getFeedForwardNetwork().setName(feedForwardName);
        return database.save(trainingEnvironment);
    }

    /**
     * Saves the training environment by the feedforward- and autoencoder name
     * @param feedForwardName desired name of the feedforward network
     * @param autoEncoderName desired name of the autoencoder network
     * @param trainingEnvironment specified training environment
     * @param database database connection which should be used
     * @return
     */
    private boolean save(String feedForwardName, String autoEncoderName, TrainingEnvironment trainingEnvironment, Database database) {
        trainingEnvironment.getFeedForwardNetwork().setName(feedForwardName);
        trainingEnvironment.getAutoEncoderNetwork().setName(autoEncoderName);
        return database.save(trainingEnvironment);
    }

    /**
     * parses the input string and calls the required save methods
     * @param database database connection which should be used
     * @param input input string which will be parsed
     * @param env specified training environment
     * @return
     */
    public boolean saveEnvironment(Database database, String input, TrainingEnvironment env) {
        if (input.contains("-nff:") && input.contains("-nae:") && input.contains("-ns:")) {
            /* All parameters were specified */
            String nameSystem = parseString(input, "-ns:");
            String nameFF = parseString(input, "-nff:");
            String nameAE = parseString(input, "-nae:");
            return save(nameSystem, nameFF, nameAE, env, database);
        } else if (input.contains("-nff:") && input.contains("-nae:")) {
            /* feedforward and autoencoder name were specified */
            String nameFF = parseString(input, "-nff:");
            String nameAE = parseString(input, "-nae:");
            return save(nameFF, nameAE, env, database);
        } else if (input.contains("-nff:")) {
            String nameFF = parseString(input, "-nff:");
            return saveJustFeedforward(nameFF, env, database);
        } else if (input.contains("-ns:")) {
            /* environment name was specified */
            String nameSystem = parseString(input, "-ns:");
            return save(nameSystem, env, database);
        } else if (env.getName() != null) {
            return save(env.getName(), env, database);
        }
        printHelp();
        return false;
    }

    /**
     * saves the result of a trained network to the database
     * @param database database connection which should be used
     * @param env specified training environment
     * @param pathToImage path to image which was tested
     * @param result result string of the output layer
     * @return
     */
    public boolean saveResult(Database database, TrainingEnvironment env, String pathToImage, String result) {
        return database.saveResult(env, pathToImage, result);
    }

    /**
     * prints the help of the save command
     */
    private void printHelp() {
        System.out.println("Not all parameters were specified. Save takes the following arguments.");
        System.out.println("   -nFF:\tName of feedforward network");
        System.out.println("   -nAE:\tName of autoencoder network");
        System.out.println("   -nS:\tName of training environment");
        System.out.println("At least nFF or nS need to be specified.");
    }


}
