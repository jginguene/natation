package fr.natation.view;

public class EmptyAptitudeComboModel extends CompetenceComboModel {

    public EmptyAptitudeComboModel() {
        super(null);
    }

    @Override
    public String toString() {
        return "------------------------------------------";
    }
}
