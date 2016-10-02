package fr.natation.view.aptitude;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import fr.natation.Utils;
import fr.natation.model.Aptitude;
import fr.natation.model.Capacite;
import fr.natation.model.Niveau;
import fr.natation.model.TypeAptitude;
import fr.natation.service.AptitudeService;
import fr.natation.service.CapaciteService;
import fr.natation.service.NiveauService;
import fr.natation.service.TypeAptitudeService;
import fr.natation.view.CustomComboBoxModel;
import fr.natation.view.IRefreshListener;

public class AptitudeAddPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(AptitudeAddPanel.class.getName());

    private final JLabel labelDescription = new JLabel("Description:");
    private final JLabel labelType = new JLabel("Type:");
    private final JLabel labelNiveau = new JLabel("Niveau:");
    private final JLabel labelCapacite = new JLabel("Capacite:");

    private final JTextField inputDescription = new JTextField(80);
    private final JComboBox<Niveau> inputNiveau = new JComboBox<Niveau>();
    private final JComboBox<TypeAptitude> inputType = new JComboBox<TypeAptitude>();
    private final JComboBox<Capacite> inputCapacite = new JComboBox<Capacite>();

    private final JButton addButton = new JButton("Ajouter");

    private IRefreshListener listener;

    public AptitudeAddPanel() throws Exception {
        this.refresh();

        this.setBorder(BorderFactory.createTitledBorder("Ajouter un aptitude"));

        this.setLayout(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridwidth = 1;
        this.add(this.labelDescription, constraint);

        constraint.gridx = 1;
        this.add(this.inputDescription, constraint);

        constraint.gridy = 1;
        constraint.gridx = 0;
        this.add(this.labelNiveau, constraint);

        constraint.gridx = 1;
        this.add(this.inputNiveau, constraint);

        constraint.gridy = 2;
        constraint.gridx = 0;
        this.add(this.labelType, constraint);

        constraint.gridx = 1;
        this.add(this.inputType, constraint);

        constraint.gridy = 3;
        constraint.gridx = 0;
        this.add(this.labelCapacite, constraint);

        constraint.gridx = 1;
        this.add(this.inputCapacite, constraint);

        constraint.gridx = 0;
        constraint.gridy = 4;
        constraint.gridwidth = 2;
        this.add(this.addButton, constraint);

        this.addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                AptitudeAddPanel.this.onAddButton();
            }
        });

        this.setVisible(true);

        this.inputType.setSelectedIndex(0);
        this.inputNiveau.setSelectedIndex(0);

    }

    public void onAddButton() {
        if (this.listener != null) {
            try {

                String error = "";
                if (Utils.isBlank(this.inputDescription.getText())) {
                    error += "La saisie de la description est obligatoire.\n";
                }

                if (!Utils.isBlank(error)) {
                    JOptionPane.showMessageDialog(null, error, "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Aptitude aptitude = new Aptitude();

                aptitude.setDescription(this.inputDescription.getText());

                Capacite selectedCapacite = (Capacite) this.inputCapacite.getSelectedItem();
                if (selectedCapacite != null) {
                    aptitude.setCapaciteId(selectedCapacite.getId());
                }

                Niveau selectedNiveau = (Niveau) this.inputNiveau.getSelectedItem();
                if (selectedNiveau != null) {
                    aptitude.setNiveauId(selectedNiveau.getId());
                }

                TypeAptitude selectedType = (TypeAptitude) this.inputType.getSelectedItem();
                if (selectedType != null) {
                    aptitude.setTypeId(selectedType.getId());
                }

                AptitudeService.create(aptitude);

                this.listener.refresh();

                this.inputDescription.setText("");
                this.inputType.setSelectedIndex(1);
                this.inputNiveau.setSelectedIndex(1);
                this.inputCapacite.setSelectedIndex(0);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "L'ajout a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
                LOGGER.error("L'ajout a échoué", e);
            }
        }
    }

    public void addListener(IRefreshListener listener) {
        this.listener = listener;
    }

    @Override
    public void refresh() throws Exception {
        CustomComboBoxModel<TypeAptitude> modelType = new CustomComboBoxModel<TypeAptitude>(TypeAptitudeService.getAll());
        this.inputType.setModel(modelType);

        CustomComboBoxModel<Niveau> modelNiveau = new CustomComboBoxModel<Niveau>(NiveauService.getAll());
        this.inputNiveau.setModel(modelNiveau);

        CustomComboBoxModel<Capacite> modelCapacite = new CustomComboBoxModel<Capacite>(CapaciteService.getAll());
        this.inputCapacite.setModel(modelCapacite);

    }
}
