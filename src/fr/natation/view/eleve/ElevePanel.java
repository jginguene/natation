package fr.natation.view.eleve;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import fr.natation.Printer;
import fr.natation.Utils;
import fr.natation.model.Eleve;
import fr.natation.model.Groupe;
import fr.natation.service.EleveService;
import fr.natation.service.GroupeService;
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

    private final JButton updateButton = new JButton("Mettre à jour", Utils.getImage("update.png"));
    private final JButton printButton = new JButton("Imprimer", Utils.getImage("print.png"));

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
        panel.add(this.printButton, constraint);

        this.updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ElevePanel.this.onUpdateButton();
            }
        });

        this.printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ElevePanel.this.onPrintButton();
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

    private void onPrintButton() {
        PrinterJob pjob = PrinterJob.getPrinterJob();
        PageFormat preformat = pjob.defaultPage();
        preformat.setOrientation(PageFormat.LANDSCAPE);
        PageFormat postformat = pjob.pageDialog(preformat);
        // If user does not hit cancel then print.
        if (preformat != postformat) {
            // Set print component
            pjob.setPrintable(new Printer(this), postformat);
            if (pjob.printDialog()) {
                try {
                    pjob.print();
                } catch (PrinterException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private void refresh() throws Exception {
        CustomComboBoxModel<Groupe> modelGroupe = new CustomComboBoxModel<Groupe>(GroupeService.getAll());
        this.inputGroupe.setModel(modelGroupe);

        CustomComboBoxModel<Eleve> modelEleve = new CustomComboBoxModel<Eleve>(EleveService.getAll());
        this.inputEleve.setModel(modelEleve);
    }

}
