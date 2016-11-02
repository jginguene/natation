package fr.natation.view.eleve;

import java.awt.BorderLayout;
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

import fr.natation.model.Ecole;
import fr.natation.service.EcoleService;
import fr.natation.view.ButtonFactory;

public class EcoleInfoEditPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(EcoleInfoEditPanel.class.getName());

    private final JLabel labelNom = new JLabel("Nom: ");
    private final JLabel labelAdresse = new JLabel("Adresse: ");
    private final JLabel labelCodePostal = new JLabel("Code postal: ");
    private final JLabel labelVille = new JLabel("Ville: ");

    private final JTextField inputNom = new JTextField(20);
    private final JTextField inputAdresse = new JTextField(20);
    private final JTextField inputCodePostal = new JTextField(20);
    private final JTextField inputVille = new JTextField(20);

    private final JButton updateButton = ButtonFactory.createUpdateButton();
    private final Ecole ecole;

    public EcoleInfoEditPanel() throws Exception {

        this.ecole = EcoleService.get();

        this.inputNom.setText(this.ecole.getNom());
        this.inputAdresse.setText(this.ecole.getAdresse());
        this.inputCodePostal.setText(this.ecole.getCodePostal());
        this.inputVille.setText(this.ecole.getVille());

        this.setBorder(BorderFactory.createTitledBorder("Informations sur l'école"));

        this.setLayout(new GridBagLayout());

        JPanel panel = new JPanel();

        GridBagConstraints constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.NORTHWEST;
        constraint.weighty = 1;
        constraint.weightx = 1;
        constraint.gridx = 1;
        constraint.gridy = 1;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        this.add(panel, constraint);

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
        panel.add(this.labelAdresse, constraint);

        constraint.gridx = 1;
        panel.add(this.inputAdresse, constraint);

        constraint.gridy++;
        constraint.gridx = 0;
        panel.add(this.labelCodePostal, constraint);

        constraint.gridx = 1;
        panel.add(this.inputCodePostal, constraint);

        constraint.gridy++;
        constraint.gridx = 0;
        panel.add(this.labelVille, constraint);

        constraint.gridx = 1;
        panel.add(this.inputVille, constraint);

        this.setVisible(true);
        JPanel panelButton = new JPanel();
        panelButton.setLayout(new BorderLayout());

        panelButton.add(this.updateButton, BorderLayout.WEST);

        constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.SOUTH;
        constraint.weighty = 2;
        constraint.weightx = 1;
        constraint.gridx = 1;
        constraint.gridy = 1;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        this.add(panelButton, constraint);

        this.updateButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                EcoleInfoEditPanel.this.onUpdateButton();
            }
        });

    }

    public void onUpdateButton() {

        try {
            this.ecole.setNom(this.inputNom.getText());
            this.ecole.setAdresse(this.inputAdresse.getText());
            this.ecole.setCodePostal(this.inputCodePostal.getText());
            this.ecole.setVille(this.inputVille.getText());

            EcoleService.update(this.ecole);

            JOptionPane.showMessageDialog(null, "La mise à jour des informations de l'école est terminée ", "Information", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "La mise à jour des informations de l'école  a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("La mise à jour des informations de l'école  échoué", e);
        }

    }

}
