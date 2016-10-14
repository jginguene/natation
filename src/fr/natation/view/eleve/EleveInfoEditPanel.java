package fr.natation.view.eleve;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import fr.natation.model.Eleve;
import fr.natation.model.Groupe;
import fr.natation.service.GroupeService;
import fr.natation.view.CustomComboBoxModel;
import fr.natation.view.IRefreshListener;

public class EleveInfoEditPanel extends JPanel implements IEleveSelectListener, IRefreshListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(EleveInfoEditPanel.class.getName());

    private final JLabel labelNom = new JLabel("Nom:");
    private final JLabel labelPrenom = new JLabel("Prénom:");
    private final JLabel labelGroupe = new JLabel("Groupe");

    private final JTextField inputNom = new JTextField(20);
    private final JTextField inputPrenom = new JTextField(20);
    private final JComboBox<Groupe> inputGroupe = new JComboBox<Groupe>();

    private Eleve eleve;

    public EleveInfoEditPanel() throws Exception {

        this.setBorder(BorderFactory.createTitledBorder("Informations sur l'élève"));

        this.setLayout(new GridBagLayout());

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(100, 50));

        GridBagConstraints constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.NORTHWEST;
        constraint.weighty = 1;
        constraint.weightx = 1;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        this.add(panel, constraint);
        this.refresh();

        panel.setLayout(new GridBagLayout());
        constraint.fill = GridBagConstraints.HORIZONTAL;

        constraint.gridy = 0;
        constraint.gridx = 0;
        constraint.gridwidth = 1;
        panel.add(this.labelNom, constraint);

        constraint.gridx = 1;
        panel.add(this.inputNom, constraint);

        constraint.gridy++;
        constraint.gridx = 0;
        panel.add(this.labelPrenom, constraint);

        constraint.gridx = 1;
        panel.add(this.inputPrenom, constraint);

        constraint.gridy++;
        constraint.gridx = 0;
        panel.add(this.labelGroupe, constraint);

        constraint.gridx = 1;
        panel.add(this.inputGroupe, constraint);

        this.setVisible(true);

    }

    @Override
    public void refresh() throws Exception {
        CustomComboBoxModel<Groupe> modelGroupe = new CustomComboBoxModel<Groupe>(GroupeService.getAll());
        this.inputGroupe.setModel(modelGroupe);

        if (this.eleve != null) {
            try {
                this.inputGroupe.setSelectedItem(this.eleve.getGroupe());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onChange(Eleve newEleve, Object source) {
        if (newEleve != null) {

            this.eleve = newEleve;
            this.inputNom.setText(newEleve.getNom());
            this.inputPrenom.setText(newEleve.getPrenom());

            try {
                this.inputGroupe.setSelectedItem(newEleve.getGroupe());
                this.inputGroupe.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void updateEleve(Eleve eleve) {
        eleve.setNom(this.inputNom.getText());
        eleve.setPrenom(this.inputPrenom.getText());
        Groupe groupe = ((Groupe) this.inputGroupe.getSelectedItem());
        if (groupe != null) {
            eleve.setGroupeId(groupe.getId());
        }
    }

}
