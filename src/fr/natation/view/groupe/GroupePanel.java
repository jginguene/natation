package fr.natation.view.groupe;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.natation.model.Groupe;
import fr.natation.service.GroupeService;

public class GroupePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public GroupePanel() throws Exception {
        this.setBorder(BorderFactory.createTitledBorder("Groupes"));

        this.setLayout(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.gridwidth = 1;
        constraint.gridy = 0;

        List<Groupe> groupes = GroupeService.getAll();

        for (Groupe groupe : groupes) {
            constraint.fill = GridBagConstraints.HORIZONTAL;
            constraint.gridx = 0;
            JLabel titre = new JLabel("Groupe " + groupe.getNom() + ": ");
            titre.setFont(titre.getFont().deriveFont(Font.BOLD));
            this.add(titre, constraint);

            constraint.gridx = 1;
            this.add(new JLabel(groupe.getDescription()), constraint);

            constraint.gridy = constraint.gridy + 1;
        }
    }

}
