package fr.natation.view;

import javax.swing.ImageIcon;

import fr.natation.Utils;

public enum Icon {

    Update(Utils.getExternalImage("img/save.png")),
    Create(Utils.getExternalImage("img/save.png")),
    Delete(Utils.getExternalImage("img/delete.png")),
    Print(Utils.getExternalImage("img/print.png")),
    Application(Utils.getExternalImage("img/app.png")),
    Pdf(Utils.getExternalImage("img/pdf.png")),
    Cancel(Utils.getExternalImage("img/cancel.png")),
    Capacite(Utils.getExternalImage("img/capacite.png")),
    Aptitude(Utils.getExternalImage("img/aptitude.png")),
    Eleve(Utils.getExternalImage("img/eleve.png")),
    Liste(Utils.getExternalImage("img/liste.png"));
    ;

    private ImageIcon image;

    private Icon(ImageIcon image) {
        this.image = image;
    }

    public ImageIcon getImage() {
        return this.image;
    }
}
