package fr.natation.view;

import java.awt.GridBagConstraints;

public class GridBagConstraintsFactory {

    public static GridBagConstraints create(int x, int y, int width, int height) {
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.gridx = x;
        constraint.gridy = y;
        constraint.gridwidth = width;
        constraint.gridheight = height;

        return constraint;

    }

}
