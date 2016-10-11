package fr.natation.view;

import javax.swing.ImageIcon;

import fr.natation.Utils;
import fr.natation.model.Capacite;

public class ViewUtils {

    public static ImageIcon getCapaciteIcon(Capacite capacite, int height) {
        try {
            return Utils.getExternalImage("img/" + capacite.getNom() + ".png", height);
        } catch (Exception e) {
            return Utils.getExternalImage("img/app.png", height);

        }
    }

}
