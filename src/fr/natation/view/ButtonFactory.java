package fr.natation.view;

import javax.swing.JButton;

public class ButtonFactory {

    public static JButton createCreateButton() {
        return new JButton("Enregistrer", Icon.Create.getImage());
    }

    public static JButton createPdfButton() {
        return new JButton("Export pdf", Icon.Pdf.getImage());
    }

    public static JButton createUpdateButton() {
        return new JButton("Mettre à jour", Icon.Update.getImage());
    }

}
