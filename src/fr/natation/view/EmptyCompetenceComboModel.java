package fr.natation.view;

public class EmptyCompetenceComboModel extends CompetenceComboModel {

    public EmptyCompetenceComboModel() {
        super(null);
    }

    @Override
    public String toString() {
        return "------------------------------------------";
    }
}
