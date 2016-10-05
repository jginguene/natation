package fr.natation.view;

import javax.swing.ImageIcon;

import fr.natation.Utils;

public enum Icon {

    Update(Utils.getImage("save.png")),
    Create(Utils.getImage("save.png")),
    Delete(Utils.getImage("delete.png")),
    Print(Utils.getImage("print.png")),
    Application(Utils.getImage("app.png")),
    Pdf(Utils.getImage("pdf.png"));

    private ImageIcon image;

    private Icon(ImageIcon image) {
        this.image = image;
    }

    public ImageIcon getImage() {
        return this.image;
    }
}
