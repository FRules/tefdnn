package de.nitschmann.tefdnn.presentation.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.nitschmann.tefdnn.presentation.json.mapper.Configuration;
import de.nitschmann.tefdnn.presentation.json.mapper.Initialization;
import de.nitschmann.tefdnn.presentation.json.mapper.JsonConfig;
import de.nitschmann.tefdnn.presentation.json.mapper.TrainingData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JsonValidator {

    public static boolean isValid(String filename) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            byte[] jsonData = Files.readAllBytes(Paths.get(filename));

            try {
                JsonConfig config = objectMapper.readValue(jsonData, JsonConfig.class);

                return isJsonConfigValid(config);
            } catch (JsonMappingException | JsonParseException e) {
                System.out.println("JSON not in valid format");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isJsonConfigValid(JsonConfig config) {
        return isInitializationValid(config) && isConfigurationValid(config) && isTrainingDataValid(config);
    }

    private static boolean isInitializationValid(JsonConfig config) {
        Initialization initialization = config.getInitialization();

        if (initialization == null) {
            System.out.println("Initialization section missing");
            return false;
        }

        if (initialization.getName() == null) {
            System.out.println("Name property in initialization section missing");
            return false;
        }

        if (initialization.getCountOfInputNeurons() <= 0 ||
                initialization.getCountOfHiddenNeurons() <= 0 ||
                initialization.getCountOfOutputNeurons() <= 0 ||
                initialization.getCountOfHiddenLayers() <= 0) {
            System.out.println("Zero or less input/hidden/output neurons or hidden layers specified");
            return false;
        }

        return true;
    }

    private static boolean isConfigurationValid(JsonConfig config) {
        Configuration configuration = config.getConfiguration();

        if (configuration == null) {
            System.out.println("Configuration section missing");
            return false;
        }

        if (configuration.getActivationFunction() <= 0 || configuration.getActivationFunction() >= 4) {
            System.out.println("Activation function must be specified and has to be either 1 (RELU), 2 (SIGMOID) or 3 (TANH)");
            return false;
        }

        if (configuration.getLearningRate() <= 0) {
            System.out.println("Learning rate must be specified and has to be larger than 0");
            return false;
        }

        if (configuration.getMomentum() <= 0) {
            System.out.println("Momentum must be specified and has to be larger than 0");
            return false;
        }

        if (configuration.getTargetLoss() <= 0) {
            System.out.println("Target loss must be specified and has to be larger than 0");
            return false;
        }

        if (configuration.getMaximumNumberOfEpochs() <= 0) {
            System.out.println("Max epoch has to be specified and has to be larger than 0");
            return false;
        }

        if (configuration.getTrainingType() != 1) {
            System.out.println("Training type must be specified and has to be 1 (BACKPROPAGATION)");
            return false;
        }

        return true;
    }

    private static boolean isTrainingDataValid(JsonConfig config) {
        List<TrainingData> trainingData = config.getTrainingData();
        if (trainingData == null) {
            return false;
        }

        Set<Integer> targetNeurons = new HashSet<>();

        for(TrainingData td : trainingData) {
            if (td.getName() == null) {
                System.out.println("Name of class has to be specified");
                return false;
            }

            if (td.getPathToDirectory() == null) {
                System.out.println("Directory path of class has to be specified");
                return false;
            }

            if (td.getTargetNeuron() < 0) {
                System.out.println("Target neuron must be specified and has to be larger than 0");
                return false;
            }

            if (!targetNeurons.add(td.getTargetNeuron())) {
                System.out.println("Every target neuron has to be unique");
                return false;
            }
        }
        return true;
    }
}
