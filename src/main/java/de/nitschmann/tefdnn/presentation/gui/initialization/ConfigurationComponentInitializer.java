package de.nitschmann.tefdnn.presentation.gui.initialization;


import de.nitschmann.tefdnn.presentation.gui.wrapper.CustomGridBagConstraints;
import de.nitschmann.tefdnn.presentation.gui.StartView;

import java.awt.*;

public class ConfigurationComponentInitializer implements IInitializer {
    @Override
    public void initialize(StartView view) {
        /* Configuration elements code */
        int startY = view.configurationStartY;

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 4, startY ,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(view.labelLearningRate, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 4, startY+ 1,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(view.labelMomentum, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 4, startY + 2,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(view.labelMaxEpoch, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 4, startY + 3,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(view.labelTargetLoss, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 4, startY + 4,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(view.labelTrainingType, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 4, startY + 5,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight));
        view.panelMain.add(view.labelActivationFunction, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 5, startY,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(view.textLearningRate, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 5, startY + 1,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(view.textMomentum, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 5, startY + 2,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(view.textMaxEpoch, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 5, startY + 3,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(view.textTargetLoss, view.c);

        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 5, startY + 4,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(view.comboTrainingType, view.c);


        view.c = CustomGridBagConstraints.getCustomGridBagConstraints(GridBagConstraints.HORIZONTAL, 5, startY+ 5,
                new Insets(view.insetsTop, view.insetsLeft, view.insetsBottom, view.insetsRight), 150, 2);
        view.panelMain.add(view.comboActivationFunction, view.c);
    }
}
