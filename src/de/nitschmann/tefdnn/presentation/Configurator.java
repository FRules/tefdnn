package de.nitschmann.tefdnn.presentation;

import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.application.TrainingEnvironment;
import de.nitschmann.tefdnn.application.training.ActivationFunctionType;
import de.nitschmann.tefdnn.application.training.TrainingType;
import static de.nitschmann.tefdnn.presentation.Parser.parseDouble;
import static de.nitschmann.tefdnn.presentation.Parser.parseInt;

public class Configurator {

    /**
     * sets the learning rate of the training environment or just the feedforward network
     * @param learningRate
     * @param trainingEnvironment
     * @param onlyFF
     */
    private void setLearningRate(double learningRate, TrainingEnvironment trainingEnvironment, boolean onlyFF) {
        trainingEnvironment.getFeedForwardNetwork().setLearningRate(learningRate);
        if (!onlyFF) {
            trainingEnvironment.getAutoEncoderNetwork().setLearningRate(learningRate);
        }
        System.out.printf("Successfully set learning rate for training environment to %f\n", learningRate);
    }

    /**
     * sets the training type of the training environment or just the feedforward network
     * @param trainingType
     * @param trainingEnvironment
     * @param onlyFF
     */
    private void setTrainingType(TrainingType trainingType, TrainingEnvironment trainingEnvironment, boolean onlyFF) {
        trainingEnvironment.getFeedForwardNetwork().setTrainingType(trainingType);
        if (!onlyFF) {
            trainingEnvironment.getAutoEncoderNetwork().setTrainingType(trainingType);
        }
        System.out.printf("Successfully set training type for training environment to %s\n", trainingType);
    }

    /**
     * sets the activationFunctionType of the training environment or just the feedforward network
     * @param activationFunctionType
     * @param trainingEnvironment
     * @param onlyFF
     */
    private void setActivationFunction(ActivationFunctionType activationFunctionType, TrainingEnvironment trainingEnvironment, boolean onlyFF) {
        trainingEnvironment.getFeedForwardNetwork().setActivationFunction(activationFunctionType);
        if (!onlyFF) {
            trainingEnvironment.getAutoEncoderNetwork().setActivationFunction(activationFunctionType);
        }
        System.out.printf("Successfully set activation function for training environment to %s\n", activationFunctionType);
    }

    /**
     * sets the maxEpoch of the training environment or just the feedforward network
     * @param maxEpoch
     * @param trainingEnvironment
     * @param onlyFF
     */
    private void setMaxEpoch(int maxEpoch, TrainingEnvironment trainingEnvironment, boolean onlyFF) {
        trainingEnvironment.getFeedForwardNetwork().setMaxEpoch(maxEpoch);
        if (!onlyFF) {
            trainingEnvironment.getAutoEncoderNetwork().setMaxEpoch(maxEpoch);
        }
        System.out.printf("Successfully set max epoch for training environment to %d\n", maxEpoch);
    }

    /**
     * sets the targetLoss of the training environment or just the feedforward network
     * @param targetLoss
     * @param trainingEnvironment
     * @param onlyFF
     */
    private void setTargetLoss(double targetLoss, TrainingEnvironment trainingEnvironment, boolean onlyFF) {
        trainingEnvironment.getFeedForwardNetwork().setTargetLoss(targetLoss);
        if (!onlyFF) {
            trainingEnvironment.getAutoEncoderNetwork().setTargetLoss(targetLoss);
        }
        System.out.printf("Successfully set target loss for training environment to %f\n", targetLoss);
    }

    /**
     * sets the momentum of the training environment or just the feedforward network
     * @param momentum
     * @param trainingEnvironment
     * @param onlyFF
     */
    private void setMomentum(double momentum, TrainingEnvironment trainingEnvironment, boolean onlyFF) {
        trainingEnvironment.getFeedForwardNetwork().setMomentum(momentum);

        if (!onlyFF) {
            trainingEnvironment.getAutoEncoderNetwork().setMomentum(momentum);
        }
        System.out.printf("Successfully set momentum for training environment to %f\n", momentum);
    }

    /**
     * parses the configuration string and calls the required methods
     * @param input
     * @param trainingEnvironment
     * @return
     */
    public boolean configureEnvironment(String input, TrainingEnvironment trainingEnvironment) {
        boolean configOnlyFF = false;

        if (input.contains("-nff")) {
            configOnlyFF = true;
        }

        boolean changed = false;
        input = input.toLowerCase().trim();
        if (input.contains("-lr:")) {
            double learningRate = parseDouble(input, "-lr:");
            if (learningRate != -1) {
                setLearningRate(learningRate, trainingEnvironment, configOnlyFF);
                changed = true;
            }
        }
        if (input.contains("-tt:")) {
            int trainingType = parseInt(input, "-tt:");
            if (trainingType != -1) {
                switch (trainingType) {
                    case 1:
                        setTrainingType(TrainingType.BACKPROPAGATION, trainingEnvironment, configOnlyFF);
                        break;
                    default:
                        setTrainingType(TrainingType.BACKPROPAGATION, trainingEnvironment, configOnlyFF);
                        System.out.println("Training type not found. Using default.");
                }
                changed = true;
            }
        }
        if (input.contains("-af:")) {
            int activationFunction = parseInt(input, "-af:");
            if (activationFunction != -1) {
                switch (activationFunction) {
                    case 1:
                        setActivationFunction(ActivationFunctionType.RELU, trainingEnvironment, configOnlyFF);
                        break;
                    case 2:
                        setActivationFunction(ActivationFunctionType.SIGMOID, trainingEnvironment, configOnlyFF);
                        break;
                    default:
                        setActivationFunction(ActivationFunctionType.RELU, trainingEnvironment, configOnlyFF);
                        System.out.println("Training type not found. Using default.");
                }
                changed = true;
            }
        }
        if (input.contains("-me:")) {
            int maxEpoch = parseInt(input, "-me:");
            if (maxEpoch != -1) {
                setMaxEpoch(maxEpoch, trainingEnvironment, configOnlyFF);
                changed = true;
            }
        }
        if (input.contains("-tl:")) {
            double targetLoss = parseDouble(input, "-tl:");
            if (targetLoss != -1) {
                setTargetLoss(targetLoss, trainingEnvironment, configOnlyFF);
                changed = true;
            }
        }
        if (input.contains("-mom:")) {
            double momentum = parseDouble(input, "-mom:");
            if (momentum != -1) {
                setMomentum(momentum, trainingEnvironment, configOnlyFF);
                changed = true;
            }
        }

        if (!changed) {
            printHelp();
        }

        return changed;
    }

    public boolean isProperlyConfigured(TrainingEnvironment env) {
        boolean isProperlyConfigured = true;
        if (env.getAutoEncoderNetwork() != null) {
            NeuralNetwork nn = env.getFeedForwardNetwork();
            NeuralNetwork ae = env.getAutoEncoderNetwork();
            if (nn.getLearningRate() == 0 || ae.getLearningRate() == 0) {
                System.out.println("Learning rate must be set");
                isProperlyConfigured = false;
            }
            if (nn.getTrainingType() == null || ae.getTrainingType() == null) {
                System.out.println("Training type must be set");
                isProperlyConfigured = false;
            }

            if (nn.getActivationFunctionType() == null || ae.getActivationFunctionType() == null) {
                System.out.println("Activation function must be set");
                isProperlyConfigured = false;
            }

            if (nn.getMomentum() == 0 || ae.getMomentum() == 0) {
                System.out.println("Momentum must be set");
                isProperlyConfigured = false;
            }

            if (nn.getMaxEpoch() == 0 || ae.getMaxEpoch() == 0) {
                System.out.println("Max Epochs must be set");
                isProperlyConfigured = false;
            }

            if (nn.getTargetLoss() == 0 || ae.getTargetLoss() == 0) {
                System.out.println("Target loss must be set");
                isProperlyConfigured = false;
            }
        } else {
            /* just the feedforward network has to be configured */
            NeuralNetwork nn = env.getFeedForwardNetwork();
            if (nn.getLearningRate() == 0) {
                System.out.println("Learning rate must be set");
                isProperlyConfigured = false;
            }
            if (nn.getTrainingType() == null) {
                System.out.println("Training type must be set");
                isProperlyConfigured = false;
            }

            if (nn.getActivationFunctionType() == null) {
                System.out.println("Activation function must be set");
                isProperlyConfigured = false;
            }

            if (nn.getMomentum() == 0) {
                System.out.println("Momentum must be set");
                isProperlyConfigured = false;
            }

            if (nn.getMaxEpoch() == 0) {
                System.out.println("Max Epochs must be set");
                isProperlyConfigured = false;
            }

            if (nn.getTargetLoss() == 0) {
                System.out.println("Target loss must be set");
                isProperlyConfigured = false;
            }
        }
        return isProperlyConfigured;
    }

    /**
     * prints the help of the conf command
     */
    private void printHelp() {
        System.out.println("Parameters were wrong. Conf takes the following arguments which are all optional.");
        System.out.println("   -nff\tconfigures only feedforward network");
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
