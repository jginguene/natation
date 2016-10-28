package fr.natation.view.competence;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import fr.natation.model.Competence;
import fr.natation.model.Domaine;
import fr.natation.model.Eleve;
import fr.natation.model.Groupe;
import fr.natation.model.Niveau;
import fr.natation.service.CompetenceService;
import fr.natation.service.DomaineService;
import fr.natation.service.EleveService;
import fr.natation.service.GroupeService;
import fr.natation.service.NiveauService;
import fr.natation.view.ButtonFactory;
import fr.natation.view.CustomComboBoxModel;
import fr.natation.view.EmptyGroupe;
import fr.natation.view.GridBagConstraintsFactory;
import fr.natation.view.Icon;
import fr.natation.view.eleve.EleveListPanel;

public class SelectionEnLotDeCompetencePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(SelectionEnLotDeCompetencePanel.class.getName());

    private final JComboBox<Groupe> inputGroupe = new JComboBox<Groupe>();
    private final JComboBox<Niveau> inputNiveau = new JComboBox<Niveau>();
    private final JComboBox<Domaine> inputDomaine = new JComboBox<Domaine>();
    private final JComboBox<Competence> inputCompetence = new JComboBox<Competence>();

    private final JButton selectAllButton = ButtonFactory.createButton("Tout sélectionner", Icon.CheckBoxChecked);
    private final JButton unselectAllButton = ButtonFactory.createButton("Tout désélectionner", Icon.CheckBoxUnchecked);
    private final JButton addCompetenceButton = ButtonFactory.createAddButton("Ajouter la compétence aux élèves séléctionnés");

    private final EmptyGroupe tous = new EmptyGroupe("Tous les groupes");

    protected final EleveListPanel listPanel;

    public SelectionEnLotDeCompetencePanel() throws Exception {

        this.listPanel = new EleveListPanel(true) {

            private static final long serialVersionUID = 1L;

            @Override
            protected List<Eleve> getEleveToDisplay() throws Exception {

                Groupe selectedGroupe = (Groupe) SelectionEnLotDeCompetencePanel.this.inputGroupe.getSelectedItem();
                if (selectedGroupe == null) {
                    return Collections.emptyList();
                }

                if (selectedGroupe == SelectionEnLotDeCompetencePanel.this.tous) {
                    return EleveService.getAll();
                } else {
                    return EleveService.getAll(selectedGroupe);
                }
            }
        };

        JPanel panel = new JPanel();

        this.setSize(this.inputCompetence, new Dimension(500, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        panel.setLayout(new GridBagLayout());

        int y = 0;
        panel.add(new JLabel("Groupe: "), GridBagConstraintsFactory.create(0, y, 1, 1));
        panel.add(this.inputGroupe, GridBagConstraintsFactory.create(1, y, 1, 1));
        y++;

        panel.add(new JLabel("Niveau: "), GridBagConstraintsFactory.create(0, y, 1, 1));
        panel.add(this.inputNiveau, GridBagConstraintsFactory.create(1, y, 1, 1));
        y++;

        panel.add(new JLabel("Domaine: "), GridBagConstraintsFactory.create(0, y, 1, 1));
        panel.add(this.inputDomaine, GridBagConstraintsFactory.create(1, y, 1, 1));
        y++;

        panel.add(new JLabel("Competence: "), GridBagConstraintsFactory.create(0, y, 1, 1));
        panel.add(this.inputCompetence, GridBagConstraintsFactory.create(1, y, 1, 1));
        y++;

        panel.add(this.listPanel, GridBagConstraintsFactory.create(0, y, 3, 1));
        y++;

        panel.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 5));
        this.setLayout(new BorderLayout());

        JPanel panelButton = new JPanel();
        panelButton.setLayout(new GridLayout(1, 8));
        panelButton.add(this.selectAllButton);
        panelButton.add(this.unselectAllButton);
        panelButton.add(this.addCompetenceButton);

        panelButton.add(new JLabel());

        this.selectAllButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SelectionEnLotDeCompetencePanel.this.listPanel.selectAll();
            }
        });

        this.unselectAllButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SelectionEnLotDeCompetencePanel.this.listPanel.unselectAll();
            }
        });

        this.addCompetenceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectionEnLotDeCompetencePanel.this.onAddCompetenceButton();
            }
        });

        this.add(panel, BorderLayout.NORTH);
        this.add(this.listPanel, BorderLayout.CENTER);
        this.add(panelButton, BorderLayout.SOUTH);

        this.setSize(this.listPanel, new Dimension(1000, 700));

        this.refresh();
        this.refreshCompetence();

        this.inputGroupe.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event) {
                try {
                    SelectionEnLotDeCompetencePanel.this.listPanel.refresh();
                } catch (Exception e) {
                    LOGGER.error("error on change inputGroupe", e);
                }
            }
        });

        ItemListener itemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    try {
                        SelectionEnLotDeCompetencePanel.this.refreshCompetence();
                    } catch (Exception e) {
                        LOGGER.error("error on change ", e);
                    }
                }
            }
        };

        this.inputDomaine.addItemListener(itemListener);
        this.inputNiveau.addItemListener(itemListener);
    }

    public void setSize(Component component, Dimension dimension) {
        component.setSize(dimension);
        component.setPreferredSize(dimension);
        component.setMaximumSize(dimension);
        component.setMinimumSize(dimension);
    }

    public void refresh() throws Exception {
        CustomComboBoxModel<Niveau> modelNiveau = new CustomComboBoxModel<Niveau>(NiveauService.getAll());
        this.inputNiveau.setModel(modelNiveau);
        this.inputNiveau.setSelectedIndex(0);

        CustomComboBoxModel<Domaine> modelDomaine = new CustomComboBoxModel<Domaine>(DomaineService.getAll());
        this.inputDomaine.setModel(modelDomaine);
        this.inputDomaine.setSelectedIndex(0);

        List<Groupe> groupes = GroupeService.getAll();
        groupes.add(0, this.tous);
        CustomComboBoxModel<Groupe> modelGroupe = new CustomComboBoxModel<Groupe>(groupes);
        modelGroupe.setSelectedItem(this.tous);
        this.inputGroupe.setModel(modelGroupe);
        this.listPanel.refresh();

    }

    private void refreshCompetence() throws Exception {
        Niveau selectedNiveau = (Niveau) this.inputNiveau.getSelectedItem();
        Domaine selectedDomaine = (Domaine) this.inputDomaine.getSelectedItem();

        List<Competence> competences = CompetenceService.get(selectedNiveau, selectedDomaine);

        CustomComboBoxModel<Competence> modelCompetence = new CustomComboBoxModel<Competence>(competences);
        this.inputCompetence.setModel(modelCompetence);
        this.inputCompetence.setSelectedIndex(0);
        this.inputCompetence.repaint();

    }

    private void onAddCompetenceButton() {
        List<Eleve> selection = this.listPanel.getSelection();
        if (selection.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vous devez au moins séléctionner un elève", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Competence selectedCompetence = (Competence) this.inputCompetence.getSelectedItem();

        String msg = "";
        for (Eleve eleve : selection) {
            try {
                CompetenceService.add(selectedCompetence, eleve);
                msg += "\n" + eleve.toString();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Une erreur est survenue en ajoutant la compétence à " + eleve.toString(), "Erreur", JOptionPane.ERROR_MESSAGE);
                LOGGER.error("error on change ", e);
            }

        }

        msg = "La compétence \"" + selectedCompetence.getDescription() + "\" a été ajoutée à:" + msg;
        JOptionPane.showMessageDialog(null, msg, "Confirmation", JOptionPane.INFORMATION_MESSAGE);

    }

}
