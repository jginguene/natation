package fr.natation.view.eleve;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
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
import fr.natation.view.IRefreshListener;

public class EleveInfoEditPanel extends JPanel implements IEleveSelectListener, IRefreshListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(EleveAddPanel.class.getName());

    private final JLabel labelNom = new JLabel("Nom:");
    private final JLabel labelPrenom = new JLabel("Prénom:");
    private final JLabel labelGroupe = new JLabel("Groupe");

    private final JTextField inputNom = new JTextField(20);
    private final JTextField inputPrenom = new JTextField(20);
    private final JComboBox<Groupe> inputGroupe = new JComboBox<Groupe>();

    private final JButton updateButton = ButtonFactory.createUpdateButton();
    private final JButton pdfButton = ButtonFactory.createPdfButton();

    public EleveInfoEditPanel() throws Exception {

        this.setBorder(BorderFactory.createTitledBorder("Informations sur l'élève"));

        JPanel panel = new JPanel();
        this.setLayout(new BorderLayout());

        this.add(panel, BorderLayout.PAGE_START);
        this.refresh();

        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();
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

        constraint.gridx = 0;
        constraint.gridy++;
        constraint.gridwidth = 1;
        panel.add(this.updateButton, constraint);

        constraint.gridx = 1;
        panel.add(this.pdfButton, constraint);

        this.updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                EleveInfoEditPanel.this.onUpdateButton();
            }
        });

        this.pdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                EleveInfoEditPanel.this.onPdfButton();
            }
        });

        this.setVisible(true);

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

    @Override
    public void refresh() throws Exception {
        CustomComboBoxModel<Groupe> modelGroupe = new CustomComboBoxModel<Groupe>(GroupeService.getAll());
        this.inputGroupe.setModel(modelGroupe);

    }

    @Override
    public void onChange(Eleve newEleve) {
        this.inputNom.setText(newEleve.getNom());
        this.inputPrenom.setText(newEleve.getPrenom());
        try {
            this.inputGroupe.setSelectedItem(newEleve.getGroupe());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
