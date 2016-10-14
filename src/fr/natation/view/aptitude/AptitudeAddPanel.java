package fr.natation.view.aptitude;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
import fr.natation.model.Aptitude;
import fr.natation.model.Capacite;
import fr.natation.model.Niveau;
import fr.natation.model.TypeAptitude;
import fr.natation.service.AptitudeService;
import fr.natation.service.CapaciteService;
import fr.natation.service.NiveauService;
import fr.natation.service.TypeAptitudeService;
import fr.natation.view.ButtonFactory;
import fr.natation.view.CustomComboBoxModel;
import fr.natation.view.GridBagConstraintsFactory;
import fr.natation.view.IRefreshListener;

public class AptitudeAddPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(AptitudeAddPanel.class.getName());

    private final JLabel labelDescription = new JLabel("Description:");
    private final JLabel labelType = new JLabel("Type:");
    private final JLabel labelNiveau = new JLabel("Niveau:");
    private final JLabel labelScore = new JLabel("Score:");
    private final JLabel labelCapacite = new JLabel("Capacite:");

    private final JTextField inputDescription = new JTextField(80);
    private final JComboBox<Integer> inputScore = new JComboBox<Integer>();
    private final JComboBox<Niveau> inputNiveau = new JComboBox<Niveau>();
    private final JComboBox<TypeAptitude> inputType = new JComboBox<TypeAptitude>();
    private final JComboBox<Capacite> inputCapacite = new JComboBox<Capacite>();

    private final JButton addButton = ButtonFactory.createCreateButton();
    private final JButton cancelButton = ButtonFactory.createCancelButton();

    private JDialog dialog;

    private IRefreshListener listener;

    public AptitudeAddPanel() throws Exception {
        this.refresh();

        this.setBorder(BorderFactory.createTitledBorder("Ajouter une aptitude"));

        JPanel panel = new JPanel();
        this.setLayout(new BorderLayout());

        this.add(panel, BorderLayout.WEST);

        panel.setLayout(new GridBagLayout());

        int y = 0;
        panel.add(this.labelDescription, GridBagConstraintsFactory.create(0, y, 1, 1));
        panel.add(this.inputDescription, GridBagConstraintsFactory.create(1, y, 3, 1));
        y++;

        panel.add(this.labelScore, GridBagConstraintsFactory.create(0, y, 1, 1));
        panel.add(this.inputScore, GridBagConstraintsFactory.create(1, y, 1, 1));
        y++;

        panel.add(this.labelType, GridBagConstraintsFactory.create(0, y, 1, 1));
        panel.add(this.inputType, GridBagConstraintsFactory.create(1, y, 1, 1));
        y++;

        panel.add(this.labelNiveau, GridBagConstraintsFactory.create(0, y, 1, 1));
        panel.add(this.inputNiveau, GridBagConstraintsFactory.create(1, y, 1, 1));
        y++;

        panel.add(this.labelCapacite, GridBagConstraintsFactory.create(0, y, 1, 1));
        panel.add(this.inputCapacite, GridBagConstraintsFactory.create(1, y, 1, 1));
        y++;

        panel.add(this.cancelButton, GridBagConstraintsFactory.create(0, y, 1, 1));
        panel.add(this.addButton, GridBagConstraintsFactory.create(1, y, 1, 1));

        this.addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                AptitudeAddPanel.this.onAddButton();
            }
        });

        this.cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                AptitudeAddPanel.this.onCancelButton();
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

                Niveau selectedNiveau = (Niveau) this.inputNiveau.getSelectedItem();
                if (selectedNiveau != null) {
                    aptitude.setNiveauId(selectedNiveau.getId());
                }

                TypeAptitude selectedType = (TypeAptitude) this.inputType.getSelectedItem();
                if (selectedType != null) {
                    aptitude.setTypeId(selectedType.getId());
                }

                Integer selectScore = (Integer) this.inputScore.getSelectedItem();
                if (selectScore != null) {
                    aptitude.setScore(selectScore);
                }

                int aptitudeId = AptitudeService.create(aptitude);

                if (selectedCapacite != null) {
                    AptitudeService.addCapacite(aptitudeId, selectedCapacite.getId());
                }

                this.listener.refresh();

                this.inputDescription.setText("");
                this.inputType.setSelectedIndex(1);
                this.inputNiveau.setSelectedIndex(1);
                this.inputCapacite.setSelectedIndex(0);

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
        CustomComboBoxModel<TypeAptitude> modelType = new CustomComboBoxModel<TypeAptitude>(TypeAptitudeService.getAll());
        this.inputType.setModel(modelType);
        this.inputType.setSelectedIndex(1);

        CustomComboBoxModel<Niveau> modelNiveau = new CustomComboBoxModel<Niveau>(NiveauService.getAll());
        this.inputNiveau.setModel(modelNiveau);
        this.inputNiveau.setSelectedIndex(1);

        CustomComboBoxModel<Capacite> modelCapacite = new CustomComboBoxModel<Capacite>(CapaciteService.getAll());
        this.inputCapacite.setModel(modelCapacite);

        List<Integer> scores = new ArrayList<Integer>();
        for (int i = 1; i <= 10; i++) {
            scores.add(i);
        }

        CustomComboBoxModel<Integer> modelScore = new CustomComboBoxModel<Integer>(scores);
        this.inputScore.setModel(modelScore);
        this.inputScore.setSelectedIndex(1);
    }

    public void onCancelButton() {
        if (this.dialog != null) {
            this.dialog.dispose();
        }
    }

    public void setDialog(JDialog dialog) {
        this.dialog = dialog;
    }
}
