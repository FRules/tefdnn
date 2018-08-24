package de.nitschmann.tefdnn.presentation.gui.initialization;


import de.nitschmann.tefdnn.presentation.gui.wrapper.CustomGridBagConstraints;
import de.nitschmann.tefdnn.presentation.gui.StartView;

import java.awt.*;

public class ConfigurationComponentInitializer implements IInitializer {
    @Override
    public void initialize(StartView view) {
        /* Configuration elements code */
        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 4, 1,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(view.labelLearningRate, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 4, 2,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(view.labelMomentum, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 4, 3,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(view.labelMaxEpoch, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 4, 4,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(view.labelTargetLoss, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 4, 5,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(view.labelTrainingType, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 4, 6,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(view.labelActivationFunction, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 5, 1,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(view.textLearningRate, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 5, 2,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(view.textMomentum, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 5, 3,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(view.textMaxEpoch, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 5, 4,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(view.textTargetLoss, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 5, 5,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(view.comboTrainingType, view.c);


        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 5, 6,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(view.comboActivationFunction, view.c);
    }
}
