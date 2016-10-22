package fr.natation.view;

import fr.natation.model.Niveau;

public class EmptyNiveau extends Niveau {

    public EmptyNiveau(String text) {
        this.setId(-1);
        this.setNom(text);
    }

}
