package de.nitschmann.tefdnn.presentation.gui.console;

import de.nitschmann.tefdnn.presentation.gui.wrapper.DynamicTrainingComponent;
import de.nitschmann.tefdnn.presentation.gui.wrapper.TrainingComponentType;

import javax.swing.*;
import java.util.ArrayList;

public class GuiToConsoleConverter {

    public static String getInitializationPart(JTextField name, JTextField inputNeurons, JTextField hiddenNeurons, JTextField outputNeurons, JTextField hiddenLayers) {
        return "init -n: " +
                name.getText() +
                " -cin: " +
                inputNeurons.getText() +
                " -chn: " +
                hiddenNeurons.getText() +
                " -con: " +
                outputNeurons.getText() +
                " -chl: " +
                hiddenLayers.getText();
    }

    public static String getConfigurationPart(JTextField learningRate, JTextField momentum, JTextField maxEpoch,
                                              JTextField targetLoss, JComboBox<String> trainingType, JComboBox<String> activationFunction) {
        StringBuilder sb = new StringBuilder();
        sb.append("conf");
        sb.append(" -tt: ");
        if ((trainingType.getSelectedItem()) == "Backpropagation")
        sb.append("1");
        sb.append(" -lr: ");
        sb.append(learningRate.getText());
        sb.append(" -mom: ");
        sb.append(momentum.getText());
        sb.append(" -af: ");
        if ((activationFunction.getSelectedItem()) == "ReLU") {
            sb.append(1);
        } else if ((activationFunction.getSelectedItem()) == "Sigmoid") {
            sb.append(2);
        } else if ((activationFunction.getSelectedItem()) == "TanH") {
            sb.append(3);
        }
        sb.append(" -me: ");
        sb.append(maxEpoch.getText());
        sb.append(" -tl: ");
        sb.append(targetLoss.getText());
        return sb.toString();
    }

    public static String[] getTrainingPart(ArrayList<ArrayList<DynamicTrainingComponent>> dynamicComponents) {
        String[] trainStrings = new String[dynamicComponents.size()];
        for (int i = 0; i < dynamicComponents.size(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("train ");
            for(DynamicTrainingComponent component : dynamicComponents.get(i)) {
                if (component.getComponentType() == TrainingComponentType.CLASSNAME) {
                    sb.append(" -n: ");
                    sb.append(((JTextField)component.getComponent()).getText());
                } else if (component.getComponentType() == TrainingComponentType.TARGETNEURON) {
                    sb.append(" -tn: ");
                    sb.append(((JComboBox)component.getComponent()).getSelectedItem());
                } else if (component.getComponentType() == TrainingComponentType.DIRECTORYPATH) {
                    sb.append(" -ptd: ");
                    sb.append(((JTextField)component.getComponent()).getText());
                }
            }
            trainStrings[i] = sb.toString();
        }
        return trainStrings;
    }

}
