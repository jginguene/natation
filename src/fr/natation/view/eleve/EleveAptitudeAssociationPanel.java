package fr.natation.view.eleve;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import fr.natation.model.Eleve;

public class EleveAptitudeAssociationPanel extends JPanel implements IEleveSelectListener {

    private static final long serialVersionUID = 1L;

    public EleveAptitudeAssociationPanel(Eleve eleve) {

        this.setBorder(BorderFactory.createTitledBorder("Aptitudes l'élève"));

    }

    @Override
    public void onChange(Eleve newEleve) {
        // TODO Auto-generated method stub

    }

}
