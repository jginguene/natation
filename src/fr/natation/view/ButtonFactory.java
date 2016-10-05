package fr.natation.view;

import javax.swing.JButton;

public class ButtonFactory {

    public static JButton createCreateButton() {
        return new JButton("Enregistrer", Icon.Create.getImage());
    }

}
