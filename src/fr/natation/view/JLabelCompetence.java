package fr.natation.view;

import javax.swing.JLabel;

import fr.natation.model.Competence;

public class JLabelCompetence extends JLabel {

    private static final long serialVersionUID = 1L;

    private Competence competence;

    public JLabelCompetence(String title) {
        super(title);
    }

    public Competence getCompetence() {
        return this.competence;
    }

    public void setCompetence(Competence competence) {
        this.competence = competence;
    }

}
