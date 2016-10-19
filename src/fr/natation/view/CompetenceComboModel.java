package fr.natation.view;

import fr.natation.Utils;
import fr.natation.model.Competence;

public class CompetenceComboModel {

    private final Competence competence;

    public CompetenceComboModel(Competence competence) {
        this.competence = competence;
    }

    @Override
    public String toString() {
        String str = this.competence.getNum() + ") " + this.competence.getDescription();
        int lineLength = 35;
        return Utils.cutStringHtml(str, lineLength);

    }

    public Competence getCompetence() {
        return this.competence;
    }

}
