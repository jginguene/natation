package fr.natation.view;

import javax.swing.ImageIcon;

import fr.natation.Utils;

public enum Icon {

    Splash(Utils.getExternalImage("img/splash.gif")),
    Navette(Utils.getExternalImage("img/navette.png")),
    CheckBoxChecked(Utils.getExternalImage("img/checkbox-checked.png")),
    CheckBoxUnchecked(Utils.getExternalImage("img/checkbox-unchecked.png")),
    Update(Utils.getExternalImage("img/save.png")),
    Create(Utils.getExternalImage("img/save.png")),
    Check(Utils.getExternalImage("img/check.png")),
    Delete(Utils.getExternalImage("img/delete.png")),
    Print(Utils.getExternalImage("img/print.png")),
    Application(Utils.getExternalImage("img/app.png")),
    Pdf(Utils.getExternalImage("img/pdf.png")),
    Excel(Utils.getExternalImage("img/excel.png")),
    Cancel(Utils.getExternalImage("img/cancel.png")),
    Capacite(Utils.getExternalImage("img/capacite.png")),
    Competence(Utils.getExternalImage("img/competence.png")),
    Eleve(Utils.getExternalImage("img/eleve.png")),
    View(Utils.getExternalImage("img/view.png")),
    Liste(Utils.getExternalImage("img/liste.png")),
    Exit(Utils.getExternalImage("img/exit.png")),
    Add(Utils.getExternalImage("img/plus.png")),
    SaveBackup(Utils.getExternalImage("img/save-backup.png")),
    LoadBackup(Utils.getExternalImage("img/load-backup.png"));

    ;

    private ImageIcon image;

    private Icon(ImageIcon image) {
        this.image = image;
    }

    public ImageIcon getImage() {
        return this.image;
    }
}
