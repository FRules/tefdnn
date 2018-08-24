package de.nitschmann.tefdnn.presentation.gui.initialization;

import de.nitschmann.tefdnn.presentation.gui.wrapper.CustomGridBagConstraints;
import de.nitschmann.tefdnn.presentation.gui.StartView;

import java.awt.*;

public class InitializationComponentInitializer implements IInitializer {
    @Override
    public void initialize(StartView view) {
        /* Initialization elements code */
        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 0, 1,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(view.labelName, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 0, 2,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(view.labelInputNeurons, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 0, 3,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(view.labelHiddenNeurons, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 0, 4,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(view.labelOutputNeurons, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 0, 5,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(view.labelHiddenLayers, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 1, 1,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(view.textName, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 1, 2,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(view.textInputNeurons, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 1, 3,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(view.textHiddenNeurons, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 1, 4,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(view.textOutputNeurons, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 1, 5,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(view.textHiddenLayers, view.c);
    }
}
