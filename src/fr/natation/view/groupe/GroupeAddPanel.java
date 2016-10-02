package fr.natation.view.groupe;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import fr.natation.Utils;
import fr.natation.model.Groupe;
import fr.natation.service.GroupeService;
import fr.natation.view.IRefreshListener;

public class GroupeAddPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(GroupeAddPanel.class.getName());

    private final JLabel labelNom = new JLabel("Nom:");
    private final JLabel labelDescription = new JLabel("Description:");

    private final JTextField inputNom = new JTextField(20);
    private final JTextField inputDescription = new JTextField(40);

    private final JButton addButton = new JButton("Ajouter");

    private IRefreshListener listener;

    public GroupeAddPanel() throws Exception {

        this.setBorder(BorderFactory.createTitledBorder("Ajouter un groupe"));

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
        this.add(this.labelDescription, constraint);

        constraint.gridx = 1;
        this.add(this.inputDescription, constraint);

        constraint.gridx = 0;
        constraint.gridy = 3;
        constraint.gridwidth = 2;
        this.add(this.addButton, constraint);

        this.addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                GroupeAddPanel.this.onAddButton();
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

                if (Utils.isBlank(this.inputDescription.getText())) {
                    error += "La saisie du description est obligatoire.\n";
                }

                if (!Utils.isBlank(error)) {
                    JOptionPane.showMessageDialog(null, error, "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Groupe groupe = new Groupe();
                groupe.setNom(this.inputNom.getText());
                groupe.setDescription(this.inputDescription.getText());

                GroupeService.create(groupe);

                this.listener.refresh();

                this.inputNom.setText("");
                this.inputDescription.setText("");

                this.refreshGroupes();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "L'ajout a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
                LOGGER.error("L'ajout a échoué", e);
            }
        }
    }

    private void refreshGroupes() throws Exception {
        // List<Groupe> groupes = GroupeService.getAll();
        // GroupeComboboxModel model = new GroupeComboboxModel(groupes);
        // this.inputGroupe.setModel(model);
    }

    public void addListener(IRefreshListener listener) {
        this.listener = listener;
    }

    @Override
    public void refresh() throws Exception {
        // TODO Auto-generated method stub

    }

}
