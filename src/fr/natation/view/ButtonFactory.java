package fr.natation.view;

import javax.swing.JButton;

public class ButtonFactory {

    public static JButton createCreateButton() {
        return new JButton("Enregistrer", Icon.Create.getImage());
    }

    public static JButton createPdfButton() {
        return createPdfButton("Export pdf");
    }

    public static JButton createCancelButton() {
        return new JButton("Annuler", Icon.Cancel.getImage());
    }

    public static JButton createPdfButton(String text) {
        return new JButton(text, Icon.Pdf.getImage());
    }

    public static JButton createUpdateButton() {
        return new JButton("Mettre à jour", Icon.Update.getImage());
    }

}
