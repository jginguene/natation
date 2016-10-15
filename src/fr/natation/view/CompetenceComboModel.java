package fr.natation.view;

import fr.natation.model.Competence;

public class CompetenceComboModel {

    private final Competence competence;

    public CompetenceComboModel(Competence competence) {
        this.competence = competence;
    }

    @Override
    public String toString() {
        int lineLength = 35;
        String str = this.competence.getNum() + ") " + this.competence.getDescription();
        if (str.length() < lineLength) {
            return str;
        } else {
            String line = "";
            String ret = "";
            for (String word : str.split(" ")) {
                String newLine = line + word + " ";
                if (newLine.length() < lineLength) {
                    line = newLine;
                } else {
                    ret = ret + " " + line + "<br/>";
                    line = word;
                }
            }
            ret = ret + line;
            return "<html>" + ret;
        }
    }

    public Competence getCompetence() {
        return this.competence;
    }

}
