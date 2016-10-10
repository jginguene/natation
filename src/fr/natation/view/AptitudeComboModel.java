package fr.natation.view;

import fr.natation.model.Aptitude;

public class AptitudeComboModel {

    private final Aptitude aptitude;

    public AptitudeComboModel(Aptitude aptitude) {
        this.aptitude = aptitude;
    }

    @Override
    public String toString() {
        int lineLength = 35;
        String str = this.aptitude.getScore() + ") " + this.aptitude.getDescription();
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

    public Aptitude getAptitude() {
        return this.aptitude;
    }

}
