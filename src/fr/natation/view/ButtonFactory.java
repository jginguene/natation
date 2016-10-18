package fr.natation.view;

import javax.swing.JButton;

public class ButtonFactory {

    public static JButton createButton(String text, Icon icon) {
        return new JButton(text, icon.getImage());
    }

    public static JButton createCreateButton() {
        return new JButton("Enregistrer", Icon.Create.getImage());
    }

    public static JButton createCreateButton(String text) {
        return new JButton(text, Icon.Create.getImage());
    }

    public static JButton createCheckButton(String text) {
        return new JButton(text, Icon.Check.getImage());
    }

    public static JButton createPdfButton() {
        return createPdfButton("Export pdf");
    }

    public static JButton createExcelButton() {
        return new JButton("Export", Icon.Excel.getImage());
    }

    public static JButton createCancelButton() {
        return new JButton("Annuler", Icon.Cancel.getImage());
    }

    public static JButton createDeleteButton() {
        return new JButton("Supprimer", Icon.Delete.getImage());
    }

    public static JButton createCancelButton(String text) {
        return new JButton(text, Icon.Cancel.getImage());
    }

    public static JButton createPdfButton(String text) {
        return new JButton(text, Icon.Pdf.getImage());
    }

    public static JButton createAddButton(String text) {
        return new JButton(text, Icon.Add.getImage());
    }

    public static JButton createUpdateButton() {
        return new JButton("Mettre à jour", Icon.Update.getImage());
    }

}
