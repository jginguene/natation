package fr.natation.view.eleve;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import fr.natation.PdfGenerator;
import fr.natation.model.Eleve;
import fr.natation.model.Groupe;
import fr.natation.service.EleveService;
import fr.natation.service.GroupeService;
import fr.natation.view.ButtonFactory;
import fr.natation.view.CustomComboBoxModel;

public class ElevePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(EleveAddPanel.class.getName());

    private final JComboBox<Eleve> inputEleve = new JComboBox<Eleve>();

    private final JLabel labelNom = new JLabel("Nom:");
    private final JLabel labelPrenom = new JLabel("Prénom:");
    private final JLabel labelGroupe = new JLabel("Groupe");

    private final JTextField inputNom = new JTextField(20);
    private final JTextField inputPrenom = new JTextField(20);
    private final JComboBox<Groupe> inputGroupe = new JComboBox<Groupe>();

    private final JButton updateButton = ButtonFactory.createUpdateButton();
    private final JButton pdfButton = ButtonFactory.createPdfButton();

    public ElevePanel() throws Exception {
        JPanel panel = new JPanel();
        this.setLayout(new BorderLayout());

        this.add(panel, BorderLayout.PAGE_START);
        this.refresh();

        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridwidth = 2;
        panel.add(this.inputEleve, constraint);

        constraint.gridx = 0;
        constraint.gridy++;
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

        constraint.gridx = 0;
        constraint.gridy++;
        constraint.gridwidth = 1;
        panel.add(this.updateButton, constraint);

        constraint.gridx = 1;
        panel.add(this.pdfButton, constraint);

        this.updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ElevePanel.this.onUpdateButton();
            }
        });

        this.pdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ElevePanel.this.onPdfButton();
            }
        });

        this.setVisible(true);

        this.inputEleve.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                ElevePanel.this.onSelectEleve((Eleve) event.getItem());
            }
        });
        this.inputEleve.setSelectedIndex(0);

    }

    private void onSelectEleve(Eleve eleve) {
        this.inputNom.setText(eleve.getNom());
        this.inputPrenom.setText(eleve.getPrenom());
        try {
            this.inputGroupe.setSelectedItem(eleve.getGroupe());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onUpdateButton() {
        try {

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "L'ajout a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("L'ajout a échoué", e);
        }

    }

    private void onPdfButton() {
        try {

            LOGGER.info("onPdfButton");
            PdfGenerator generator = new PdfGenerator();
            for (Eleve eleve : EleveService.getAll()) {
                generator.addPage(eleve);
                LOGGER.info("add " + eleve);
            }
            generator.generate("diplomes.pdf");

            JOptionPane.showMessageDialog(null, "Le fichier diplome.pdf a été créé", "Information", JOptionPane.INFORMATION_MESSAGE);

            Desktop.getDesktop().open(new File("diplomes.pdf"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "La génération des diplomes a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("La génération des diplomes a échoué", e);
        }
    }

    private void refresh() throws Exception {
        CustomComboBoxModel<Groupe> modelGroupe = new CustomComboBoxModel<Groupe>(GroupeService.getAll());
        this.inputGroupe.setModel(modelGroupe);

        CustomComboBoxModel<Eleve> modelEleve = new CustomComboBoxModel<Eleve>(EleveService.getAll());
        this.inputEleve.setModel(modelEleve);
    }

}
