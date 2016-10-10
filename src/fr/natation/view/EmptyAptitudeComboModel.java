package fr.natation.view;

public class EmptyAptitudeComboModel extends AptitudeComboModel {

    public EmptyAptitudeComboModel() {
        super(null);
    }

    @Override
    public String toString() {
        return "------------------------------------------";
    }
}
