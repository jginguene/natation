package fr.natation.view.eleve;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import fr.natation.Utils;
import fr.natation.model.Eleve;
import fr.natation.model.Groupe;
import fr.natation.service.EleveService;
import fr.natation.service.GroupeService;
import fr.natation.view.IRefreshListener;
import fr.natation.view.groupe.GroupeComboboxModel;

public class EleveAddPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(EleveAddPanel.class.getName());

    private final JLabel labelNom = new JLabel("Nom:");
    private final JLabel labelPrenom = new JLabel("Prénom:");
    private final JLabel labelGroupe = new JLabel("Groupe");

    private final JTextField inputNom = new JTextField(20);
    private final JTextField inputPrenom = new JTextField(20);
    private final JComboBox<Groupe> inputGroupe = new JComboBox<Groupe>();

    private final JButton addButton = new JButton("Ajouter");

    private IRefreshListener listener;

    public EleveAddPanel() throws Exception {

        this.setBorder(BorderFactory.createTitledBorder("Ajouter un élève"));

        this.refreshGroupes();

        this.setLayout(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridwidth = 1;
        this.add(this.labelNom, constraint);

        constraint.gridx = 1;
        this.add(this.inputNom, constraint);

        constraint.gridy = 1;
        constraint.gridx = 0;
        this.add(this.labelPrenom, constraint);

        constraint.gridx = 1;
        this.add(this.inputPrenom, constraint);

        constraint.gridy = 2;
        constraint.gridx = 0;
        this.add(this.labelGroupe, constraint);

        constraint.gridx = 1;
        this.add(this.inputGroupe, constraint);

        constraint.gridx = 0;
        constraint.gridy = 3;
        constraint.gridwidth = 2;
        this.add(this.addButton, constraint);

        this.addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                EleveAddPanel.this.onAddButton();
            }
        });

        this.setVisible(true);

    }

    public void onAddButton() {
        if (this.listener != null) {
            try {

                String error = "";
                if (Utils.isBlank(this.inputNom.getText())) {
                    error += "La saisie du nom est obligatoire.\n";
                }

                if (Utils.isBlank(this.inputPrenom.getText())) {
                    error += "La saisie du prénom est obligatoire.\n";
                }

                if (!Utils.isBlank(error)) {
                    JOptionPane.showMessageDialog(null, error, "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Eleve eleve = new Eleve();

                eleve.setNom(this.inputNom.getText());
                eleve.setPrenom(this.inputPrenom.getText());

                Groupe selectedGroupe = (Groupe) this.inputGroupe.getSelectedItem();

                if (selectedGroupe != null) {
                    eleve.setGroupeId(selectedGroupe.getId());
                }
                EleveService.create(eleve);

                this.listener.refresh();

                this.inputNom.setText("");
                this.inputPrenom.setText("");
                this.inputGroupe.setSelectedIndex(0);

                this.refreshGroupes();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "L'ajout a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
                LOGGER.error("L'ajout a échoué", e);
            }
        }
    }

    private void refreshGroupes() throws Exception {
        List<Groupe> groupes = GroupeService.getAll();
        GroupeComboboxModel model = new GroupeComboboxModel(groupes);
        this.inputGroupe.setModel(model);
    }

    public void addListener(IRefreshListener listener) {
        this.listener = listener;
    }

    @Override
    public void refresh() throws Exception {
        this.refreshGroupes();
    }

}
