package fr.natation.view.eleve;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import fr.natation.Utils;
import fr.natation.model.Classe;
import fr.natation.model.Eleve;
import fr.natation.model.Groupe;
import fr.natation.service.ClasseService;
import fr.natation.service.EleveService;
import fr.natation.service.GroupeService;
import fr.natation.view.ButtonFactory;
import fr.natation.view.CustomComboBoxModel;
import fr.natation.view.GridBagConstraintsFactory;
import fr.natation.view.IRefreshListener;

public class EleveAddPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(EleveAddPanel.class.getName());

    private final JLabel labelNom = new JLabel("Nom:");
    private final JLabel labelPrenom = new JLabel("Prénom: ");
    private final JLabel labelGroupe = new JLabel("Groupe: ");
    private final JLabel labelClasse = new JLabel("Classe: ");

    private final JTextField inputNom = new JTextField(20);
    private final JTextField inputPrenom = new JTextField(20);
    private final JComboBox<Groupe> inputGroupe = new JComboBox<Groupe>();
    private final JComboBox<Classe> inputClasse = new JComboBox<Classe>();

    private final JButton addButton = ButtonFactory.createCreateButton();
    private final JButton cancelButton = ButtonFactory.createCancelButton();

    private IRefreshListener listener;
    private JDialog dialog;

    public EleveAddPanel() throws Exception {

        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        this.refresh();

        JPanel panel = new JPanel();
        this.setLayout(new BorderLayout());

        this.add(panel, BorderLayout.WEST);

        panel.setLayout(new GridBagLayout());

        int y = 0;
        panel.add(this.labelNom, GridBagConstraintsFactory.create(0, y, 1, 1));
        panel.add(this.inputNom, GridBagConstraintsFactory.create(1, y, 2, 1));
        y++;

        panel.add(this.labelPrenom, GridBagConstraintsFactory.create(0, y, 1, 1));
        panel.add(this.inputPrenom, GridBagConstraintsFactory.create(1, y, 2, 1));
        y++;

        panel.add(this.labelGroupe, GridBagConstraintsFactory.create(0, y, 1, 1));
        panel.add(this.inputGroupe, GridBagConstraintsFactory.create(1, y, 1, 1));
        y++;

        panel.add(this.labelClasse, GridBagConstraintsFactory.create(0, y, 1, 1));
        panel.add(this.inputClasse, GridBagConstraintsFactory.create(1, y, 1, 1));
        y++;

        panel.add(this.cancelButton, GridBagConstraintsFactory.create(0, y, 1, 1));
        panel.add(this.addButton, GridBagConstraintsFactory.create(1, y, 1, 1));

        this.addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                EleveAddPanel.this.onAddButton();
            }
        });

        this.cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                EleveAddPanel.this.onCancelButton();
            }
        });

        this.setVisible(true);

    }

    public void onCancelButton() {
        if (this.dialog != null) {
            this.dialog.dispose();
        }
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

                Classe selectedClasse = (Classe) this.inputClasse.getSelectedItem();

                if (selectedClasse != null) {
                    eleve.setClasseId(selectedClasse.getId());
                }
                EleveService.create(eleve);

                this.listener.refresh();

                this.inputNom.setText("");
                this.inputPrenom.setText("");
                this.inputGroupe.setSelectedIndex(0);

                this.refresh();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "L'ajout a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
                LOGGER.error("L'ajout a échoué", e);
            } finally {
                if (this.dialog != null) {
                    this.dialog.dispose();
                }
            }
        }
    }

    public void addListener(IRefreshListener listener) {
        this.listener = listener;
    }

    @Override
    public void refresh() throws Exception {
        CustomComboBoxModel<Groupe> modelGroupe = new CustomComboBoxModel<Groupe>(GroupeService.getAll());
        this.inputGroupe.setModel(modelGroupe);

        CustomComboBoxModel<Classe> modelClasse = new CustomComboBoxModel<Classe>(ClasseService.getAll());
        this.inputClasse.setModel(modelClasse);

    }

    public void setDialog(JDialog dialog) {
        this.dialog = dialog;
    }

}
