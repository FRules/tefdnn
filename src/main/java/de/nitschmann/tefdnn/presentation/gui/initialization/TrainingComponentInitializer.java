package de.nitschmann.tefdnn.presentation.gui.initialization;

import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.presentation.gui.*;
import de.nitschmann.tefdnn.presentation.gui.wrapper.CustomGridBagConstraints;
import de.nitschmann.tefdnn.presentation.gui.wrapper.DynamicTrainingComponent;
import de.nitschmann.tefdnn.presentation.gui.wrapper.DynamicTrainingComponentContent;
import de.nitschmann.tefdnn.presentation.gui.wrapper.TrainingComponentType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TrainingComponentInitializer implements IInitializer, ITrainingFinishedEvent {

    private StartView view;

    @Override
    public void initialize(StartView view) {
        this.view = view;
        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 0, 6,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(view.labelTrainingClasses, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 1, 6,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(view.comboTrainingClasses, view.c);
        view.comboTrainingClasses.addActionListener(e -> {
            JComboBox cb = (JComboBox) e.getSource();
            generateTrainingComponents((int)cb.getSelectedItem());
        });
    }

    private void generateTrainingComponents(int amountOfTrainingComponents) {
        ArrayList<DynamicTrainingComponentContent> cachedContent = cacheContentOfDynamicComponents();
        removeDynamicComponents();

        int trainingComponentPositionY = 7;
        for(int i = 0; i < amountOfTrainingComponents; i++) {
            JLabel labelClassName = new JLabel("Class name");
            JLabel labelTargetNeuron = new JLabel("Target neuron");
            JLabel labelDirectoryPath = new JLabel("Path to training data directory");
            JTextField textClassName = new JTextField();
            JComboBox<Integer> comboTargetNeuron = new JComboBox(view.classes);
            comboTargetNeuron.setSelectedItem(i);
            JTextField textDirectoryPath = new JTextField();

            if (i < cachedContent.size()) {
                textClassName.setText(cachedContent.get(i).getClassName());
                textDirectoryPath.setText(cachedContent.get(i).getDirectoryPath());
            }

            ArrayList<DynamicTrainingComponent> list = new ArrayList<>();

            list.add(new DynamicTrainingComponent(labelClassName, TrainingComponentType.LABEL));
            list.add(new DynamicTrainingComponent(labelTargetNeuron, TrainingComponentType.LABEL));
            list.add(new DynamicTrainingComponent(labelDirectoryPath, TrainingComponentType.LABEL));
            list.add(new DynamicTrainingComponent(textClassName, TrainingComponentType.CLASSNAME));
            list.add(new DynamicTrainingComponent(comboTargetNeuron, TrainingComponentType.TARGETNEURON));
            list.add(new DynamicTrainingComponent(textDirectoryPath, TrainingComponentType.DIRECTORYPATH));

            view.dynamicTrainingComponents.add(list);

            if (i % 2 == 0) {
                placeTrainingComponent(labelClassName, labelTargetNeuron, labelDirectoryPath, textClassName, comboTargetNeuron, textDirectoryPath, 0, trainingComponentPositionY);
            } else {
                placeTrainingComponent(labelClassName, labelTargetNeuron, labelDirectoryPath, textClassName, comboTargetNeuron, textDirectoryPath, 4, trainingComponentPositionY);
                trainingComponentPositionY = trainingComponentPositionY + 3;
            }
        }

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 0, trainingComponentPositionY + 4,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 7);
        view.c.fill = GridBagConstraints.HORIZONTAL;
        view.panelMain.add(view.buttonTrain, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 0, trainingComponentPositionY + 5,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 7);
        view.c.ipady = 150;
        view.scrollPaneConsole = new JScrollPane(view.consoleArea);
        view.panelMain.add(view.scrollPaneConsole, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 0, trainingComponentPositionY + 6,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 7);

        view.panelMain.add(view.buttonTest, view.c);
        view.buttonTest.setEnabled(false);

        view.pack();
    }

    private void placeTrainingComponent(JLabel labelClassName, JLabel labelTargetNeuron, JLabel labelDirectoryPath,
                                        JTextField textClassName, JComboBox comboTargetNeuron, JTextField textDirectoryPath, int x, int y) {
        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, x, y,
                new Insets(view.insetsTop +20, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(labelClassName, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, x, y+1,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(labelTargetNeuron, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, x, y+2,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(labelDirectoryPath, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, x+1, y,
                new Insets(view.insetsTop +20, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(textClassName, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, x+1, y+1,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(comboTargetNeuron, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, x+1, y+2,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(textDirectoryPath, view.c);
    }


    private void removeDynamicComponents() {
        for(ArrayList<DynamicTrainingComponent> list : view.dynamicTrainingComponents) {
            for(DynamicTrainingComponent c : list) {
                view.panelMain.remove(c.getComponent());
            }
        }
        if (view.scrollPaneConsole != null) {
            view.panelMain.remove(view.scrollPaneConsole);
        }
        view.panelMain.remove(view.buttonTrain);
        view.panelMain.remove(view.buttonTest);
        view.dynamicTrainingComponents = new ArrayList<>();
    }

    private ArrayList<DynamicTrainingComponentContent> cacheContentOfDynamicComponents() {
        ArrayList<DynamicTrainingComponentContent> result = new ArrayList<>();
        for(ArrayList<DynamicTrainingComponent> list : view.dynamicTrainingComponents) {
            DynamicTrainingComponentContent content = new DynamicTrainingComponentContent();
            for(DynamicTrainingComponent component : list) {
                if (component.getComponentType() == TrainingComponentType.CLASSNAME) {
                    content.setClassName(((JTextField)component.getComponent()).getText());
                } else if(component.getComponentType() == TrainingComponentType.DIRECTORYPATH) {
                    content.setDirectoryPath(((JTextField)component.getComponent()).getText());
                }
            }
            result.add(content);
        }
        return result;
    }

    @Override
    public void trainingFinished(NeuralNetwork neuralNetwork) {
        view.neuralNetwork = neuralNetwork;
        view.buttonTrain.setEnabled(true);
        view.buttonTest.setEnabled(true);
    }
}
