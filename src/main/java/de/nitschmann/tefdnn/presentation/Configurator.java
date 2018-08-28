package de.nitschmann.tefdnn.presentation;

import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.application.training.ActivationFunctionType;
import de.nitschmann.tefdnn.application.training.TrainingType;
import static de.nitschmann.tefdnn.presentation.Parser.parseDouble;
import static de.nitschmann.tefdnn.presentation.Parser.parseInt;

public class Configurator {

    /**
     * sets the learning rate of the neural network
     * @param learningRate learningRate
     * @param neuralNetwork neuralNetwork
     */
    private void setLearningRate(double learningRate, NeuralNetwork neuralNetwork) {
        neuralNetwork.setLearningRate(learningRate);
        System.out.printf("Successfully set learning rate for neural network to %f\n", learningRate);
    }

    /**
     * sets the training type of the neural network
     * @param trainingType trainingType
     * @param neuralNetwork neuralNetwork
     */
    private void setTrainingType(TrainingType trainingType, NeuralNetwork neuralNetwork) {
        neuralNetwork.setTrainingType(trainingType);
        System.out.printf("Successfully set training type for neural network to %s\n", trainingType);
    }

    /**
     * sets the activationFunctionType of the neural network
     * @param activationFunctionType activation function type
     * @param neuralNetwork neuralNetwork
     */
    private void setActivationFunction(ActivationFunctionType activationFunctionType, NeuralNetwork neuralNetwork) {
        neuralNetwork.setActivationFunction(activationFunctionType);
        System.out.printf("Successfully set activation function for neural network to %s\n", activationFunctionType);
    }

    /**
     * sets the maxEpoch of the neural network
     * @param maxEpoch max amount of epochs
     * @param neuralNetwork neuralNetwork
     */
    private void setMaxEpoch(int maxEpoch, NeuralNetwork neuralNetwork) {
        neuralNetwork.setMaxEpoch(maxEpoch);
        System.out.printf("Successfully set max epoch for neural network to %d\n", maxEpoch);
    }

    /**
     * sets the targetLoss of the neural network
     * @param targetLoss target loss
     * @param neuralNetwork neuralNetwork
     */
    private void setTargetLoss(double targetLoss, NeuralNetwork neuralNetwork) {
        neuralNetwork.setTargetLoss(targetLoss);
        System.out.printf("Successfully set target loss for neural network to %f\n", targetLoss);
    }

    /**
     * sets the momentum of the neural network
     * @param momentum momentum
     * @param neuralNetwork neuralNetwork
     */
    private void setMomentum(double momentum, NeuralNetwork neuralNetwork) {
        neuralNetwork.setMomentum(momentum);
        System.out.printf("Successfully set momentum for neural network to %f\n", momentum);
    }

    /**
     * parses the configuration string and calls the required methods
     * @param input input string which looks like conf -lr: ...
     * @param neuralNetwork neuralNetwork
     * @return true, if some property changed, otherwise false
     */
    public boolean configureNeuralNetwork(String input, NeuralNetwork neuralNetwork) {
        boolean changed = false;
        input = input.toLowerCase().trim();
        if (input.contains("-lr:")) {
            double learningRate = parseDouble(input, "-lr:");
            if (learningRate != -1) {
                setLearningRate(learningRate, neuralNetwork);
                changed = true;
            }
        }
        if (input.contains("-tt:")) {
            int trainingType = parseInt(input, "-tt:");
            if (trainingType != -1) {
                switch (trainingType) {
                    case 1:
                        setTrainingType(TrainingType.BACKPROPAGATION, neuralNetwork);
                        break;
                    default:
                        setTrainingType(TrainingType.BACKPROPAGATION, neuralNetwork);
                        System.out.println("Training type not found. Using default: Backpropagation.");
                }
                changed = true;
            }
        }
        if (input.contains("-af:")) {
            int activationFunction = parseInt(input, "-af:");
            if (activationFunction != -1) {
                switch (activationFunction) {
                    case 1:
                        setActivationFunction(ActivationFunctionType.RELU, neuralNetwork);
                        break;
                    case 2:
                        setActivationFunction(ActivationFunctionType.SIGMOID, neuralNetwork);
                        break;
                    case 3:
                        setActivationFunction(ActivationFunctionType.TANH, neuralNetwork);
                        break;
                    default:
                        setActivationFunction(ActivationFunctionType.RELU, neuralNetwork);
                        System.out.println("Training type not found. Using default.");
                }
                changed = true;
            }
        }
        if (input.contains("-me:")) {
            int maxEpoch = parseInt(input, "-me:");
            if (maxEpoch != -1) {
                setMaxEpoch(maxEpoch, neuralNetwork);
                changed = true;
            }
        }
        if (input.contains("-tl:")) {
            double targetLoss = parseDouble(input, "-tl:");
            if (targetLoss != -1) {
                setTargetLoss(targetLoss, neuralNetwork);
                changed = true;
            }
        }
        if (input.contains("-mom:")) {
            double momentum = parseDouble(input, "-mom:");
            if (momentum != -1) {
                setMomentum(momentum, neuralNetwork);
                changed = true;
            }
        }

        if (!changed) {
            printHelp();
        }

        return changed;
    }

    /**
     * Checks if the neural network is properly configured and now
     * needed parameters are missing
     * @param neuralNetwork neuralNetwork
     * @return true if it is properly configured, otherwise false
     */
    public boolean isProperlyConfigured(NeuralNetwork neuralNetwork) {
        boolean isProperlyConfigured = true;

        if (neuralNetwork.getLearningRate() == 0) {
            System.out.println("Learning rate must be set");
            isProperlyConfigured = false;
        }
        if (neuralNetwork.getTrainingType() == null) {
            System.out.println("Training type must be set");
            isProperlyConfigured = false;
        }

        if (neuralNetwork.getActivationFunctionType() == null) {
            System.out.println("Activation function must be set");
            isProperlyConfigured = false;
        }

        if (neuralNetwork.getMomentum() == 0) {
            System.out.println("Momentum must be set");
            isProperlyConfigured = false;
        }

        if (neuralNetwork.getMaxEpoch() == 0) {
            System.out.println("Max Epochs must be set");
            isProperlyConfigured = false;
        }

        if (neuralNetwork.getTargetLoss() == 0) {
            System.out.println("Target loss must be set");
            isProperlyConfigured = false;
        }

        return isProperlyConfigured;
    }

    /**
     * prints the help of the conf command
     */
    private void printHelp() {
        System.out.println("Parameters were wrong. Conf takes the following arguments which are all optional.");
        System.out.println("   -lr:\tLearning rate");
        System.out.println("   -tt:\tTraining Type");
        System.out.println("        Options: 1 = Backpropagation");
        System.out.println("   -af:\tActivation function");
        System.out.println("        Options: 1 = ReLU, 2 = Sigmoid");
        System.out.println("   -me:\tMaximum number of epochs");
        System.out.println("   -tl:\tTarget loss");
        System.out.println("   -mom:\tMomentum");
    }
}
