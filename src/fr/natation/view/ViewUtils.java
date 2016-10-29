package fr.natation.view;

import java.awt.Color;

import javax.swing.ImageIcon;

import fr.natation.Utils;
import fr.natation.model.Capacite;
import fr.natation.model.Niveau;

public class ViewUtils {

    public static ImageIcon getCapaciteIcon(Capacite capacite, int height) {
        try {
            return Utils.getExternalImage("img/" + capacite.getNom() + ".png", height);
        } catch (Exception e) {
            return Utils.getExternalImage("img/app.png", height);

        }
    }

    public static Color getNiveauColor(Niveau niveau) {

        if (niveau.getNom().equals("1")) {
            return new Color(255, 153, 204);
        }

        if (niveau.getNom().equals("2")) {
            return new Color(153, 204, 255);
        }

        if (niveau.getNom().equals("3")) {
            return new Color(204, 255, 204);
        }

        return Color.GRAY;
    }
}
