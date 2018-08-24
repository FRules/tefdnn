package de.nitschmann.tefdnn.presentation.gui.wrapper;

import java.awt.*;

public class CustomGridBagConstraints {

    public static GridBagConstraints getCustomGridBagConstraints(int fill, int gridx, int gridy, Insets insets) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = fill;
        c.gridx = gridx;
        c.gridy = gridy;
        c.insets = insets;
        return c;
    }

    public static GridBagConstraints getCustomGridBagConstraints(int fill, int gridx, int gridy, Insets insets, int ipadx, int gridwidth) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = fill;
        c.gridx = gridx;
        c.gridy = gridy;
        c.insets = insets;
        c.ipadx = ipadx;
        c.gridwidth = gridwidth;
        return c;
    }

}
