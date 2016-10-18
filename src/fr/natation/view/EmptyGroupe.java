package fr.natation.view;

import fr.natation.model.Groupe;

public class EmptyGroupe extends Groupe {

    public EmptyGroupe(String text) {
        this.setId(-1);
        this.setNom(text);
        this.setDescription(text);
    }

}
