package fr.natation.view.navette;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import org.apache.log4j.Logger;

import fr.natation.Utils;
import fr.natation.model.Competence;
import fr.natation.model.Domaine;
import fr.natation.model.Eleve;
import fr.natation.model.Groupe;
import fr.natation.model.Niveau;
import fr.natation.pdf.NavetteGenerator;
import fr.natation.service.CompetenceService;
import fr.natation.service.DomaineService;
import fr.natation.service.EleveService;
import fr.natation.service.GroupeService;
import fr.natation.service.NiveauService;
import fr.natation.view.ButtonFactory;
import fr.natation.view.CustomComboBoxModel;
import fr.natation.view.EmptyGroupe;
import fr.natation.view.EmptyNiveau;
import fr.natation.view.GridBagConstraintsFactory;
import fr.natation.view.VerticalLabel;

public class NavettePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(NavettePanel.class.getName());

    private final JLabel labelGroupe = new JLabel("Groupe: ");
    private final JLabel labelNiveau = new JLabel("Niveau: ");

    private final JComboBox<Groupe> inputGroupe = new JComboBox<Groupe>();
    private final JComboBox<Niveau> inputNiveau = new JComboBox<Niveau>();

    private final EmptyGroupe allGroupes = new EmptyGroupe("Tous les groupes");
    private final EmptyNiveau allNiveaux = new EmptyNiveau("Tous les niveaux");

    private final Map<Niveau, List<Component>> mapNiveauComponent = new HashMap<Niveau, List<Component>>();
    private final Map<Eleve, List<Component>> mapEleveComponent = new HashMap<Eleve, List<Component>>();

    private final JButton navetteButton;

    public NavettePanel() throws Exception {

        for (Niveau niveau : NiveauService.getAll()) {
            this.mapNiveauComponent.put(niveau, new ArrayList<Component>());
        }
        for (Eleve eleve : EleveService.getAll()) {
            this.mapEleveComponent.put(eleve, new ArrayList<Component>());
        }

        this.refresh();
        this.setLayout(new BorderLayout());

        JPanel selectPanel = new JPanel();
        selectPanel.setLayout(new GridBagLayout());

        selectPanel.add(this.labelGroupe, GridBagConstraintsFactory.create(0, 1, 1, 1));
        selectPanel.add(this.inputGroupe, GridBagConstraintsFactory.create(1, 1, 1, 1));

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(100, 20));
        selectPanel.add(panel, GridBagConstraintsFactory.create(2, 1, 1, 1));

        selectPanel.add(this.labelNiveau, GridBagConstraintsFactory.create(3, 1, 1, 1));
        selectPanel.add(this.inputNiveau, GridBagConstraintsFactory.create(4, 1, 1, 1));

        this.add(selectPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(this.createViewPanel());

        this.setPreferredSize(new Dimension(450, 110));
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel panelButton = new JPanel();
        panelButton.setLayout(new GridBagLayout());

        this.navetteButton = ButtonFactory.createPdfButton("Créer la fiche navette");
        panelButton.add(this.navetteButton, GridBagConstraintsFactory.create(1, 1, 1, 1));
        panelButton.add(new JLabel(), GridBagConstraintsFactory.create(2, 1, 1, 1));
        panelButton.add(new JLabel(), GridBagConstraintsFactory.create(3, 1, 1, 1));
        panelButton.add(new JLabel(), GridBagConstraintsFactory.create(4, 1, 1, 1));

        this.add(panelButton, BorderLayout.SOUTH);

        this.inputNiveau.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                NavettePanel.this.refreshPanel();

            }
        });

        this.inputGroupe.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                NavettePanel.this.refreshPanel();
            }
        });

        this.navetteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                NavettePanel.this.onNavetteButton();
            }
        });

    }

    private void refresh() throws Exception {
        List<Groupe> groupes = GroupeService.getAll();
        groupes.add(0, this.allGroupes);
        CustomComboBoxModel<Groupe> modelGroupe = new CustomComboBoxModel<Groupe>(groupes);
        modelGroupe.setSelectedItem(this.allGroupes);
        this.inputGroupe.setModel(modelGroupe);

        List<Niveau> niveaux = NiveauService.getAll();
        niveaux.add(0, this.allNiveaux);
        CustomComboBoxModel<Niveau> modelNiveau = new CustomComboBoxModel<Niveau>(niveaux);
        modelNiveau.setSelectedItem(this.allNiveaux);
        this.inputNiveau.setModel(modelNiveau);
    }

    private JPanel createViewPanel() throws Exception {

        JPanel panel = new JPanel();

        panel.setLayout(new GridBagLayout());

        int x = 3;
        for (Niveau niveau : this.getNiveaux()) {

            GridBagConstraints constraint = GridBagConstraintsFactory.create(x, 0, niveau.getCompetencesCount(), 1);
            constraint.fill = GridBagConstraints.BOTH;

            JLabel labelNiveau = this.createLabelNiveau(niveau);
            panel.add(labelNiveau, constraint);
            this.mapNiveauComponent.get(niveau).add(labelNiveau);

            int domaineX = x;
            for (Domaine domaine : DomaineService.getAll()) {
                List<Competence> competences = CompetenceService.get(niveau, domaine);

                int competenceCount = competences.size();
                constraint = GridBagConstraintsFactory.create(domaineX, 1, competenceCount, 1);
                constraint.fill = GridBagConstraints.BOTH;

                JLabel labelDomaine = this.createLabelDomaine(domaine);
                panel.add(labelDomaine, constraint);
                this.mapNiveauComponent.get(niveau).add(labelDomaine);

                int competenceX = domaineX;
                for (Competence competence : competences) {

                    String text = Utils.cutStringHtml(" " + competence.getDescription(), 40);
                    VerticalLabel labelCompetence = new VerticalLabel(text);
                    labelCompetence.setRotation(VerticalLabel.ROTATE_LEFT);
                    Border border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);

                    labelCompetence.setBorder(new CompoundBorder(border, BorderFactory.createEmptyBorder(4, 4, 4, 4)));

                    constraint = GridBagConstraintsFactory.create(competenceX, 2, 1, 1);
                    constraint.fill = GridBagConstraints.BOTH;

                    panel.add(labelCompetence, constraint);

                    this.mapNiveauComponent.get(niveau).add(labelCompetence);
                    competenceX++;
                }

                domaineX += competenceCount;

            }

            x += niveau.getCompetencesCount();

        }

        int y = 3;

        GridBagConstraints constraint = GridBagConstraintsFactory.create(1, 2, 1, 1);
        constraint.anchor = GridBagConstraints.PAGE_END;

        JLabel titleEleve = this.createLabelTitle("Nom de l'élève");
        titleEleve.setPreferredSize(new Dimension(200, 20));
        panel.add(titleEleve, constraint);
        constraint.gridx++;

        JLabel titleGroupe = this.createLabelTitle("Groupe");
        titleGroupe.setPreferredSize(new Dimension(80, 20));
        panel.add(titleGroupe, constraint);

        for (Eleve eleve : this.getEleves()) {
            JLabel labelEleve = this.createLabel(eleve.toString());
            panel.add(labelEleve, GridBagConstraintsFactory.create(1, y, 1, 1));
            this.mapEleveComponent.get(eleve).add(labelEleve);

            JLabel labelGroupe = this.createLabel(eleve.getGroupeNom());
            labelGroupe.setHorizontalAlignment(JLabel.CENTER);
            panel.add(labelGroupe, GridBagConstraintsFactory.create(2, y, 1, 1));
            this.mapEleveComponent.get(eleve).add(labelGroupe);

            int competenceX = 3;
            for (Niveau niveau : this.getNiveaux()) {
                for (Domaine domaine : DomaineService.getAll()) {
                    List<Competence> competences = CompetenceService.get(niveau, domaine);
                    for (Competence competence : competences) {
                        String text = " ";
                        if (eleve.getCompetences(niveau, domaine).contains(competence)) {
                            text = "X";
                        }
                        constraint = GridBagConstraintsFactory.create(competenceX, y, 1, 1);

                        JLabel labelCompetence = this.createLabelTitle(text);
                        panel.add(labelCompetence, constraint);

                        this.mapNiveauComponent.get(niveau).add(labelCompetence);
                        this.mapEleveComponent.get(eleve).add(labelCompetence);
                        competenceX++;
                    }
                }
            }
            y++;
        }

        return panel;

    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 12));
        return label;
    }

    private JLabel createLabelTitle(String title) {
        JLabel label = new JLabel(title);
        label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 12));
        return label;
    }

    private JLabel createLabelNiveau(Niveau niveau) {
        JLabel labelNiveau = new JLabel("Niveau " + niveau.getNom());
        labelNiveau.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

        labelNiveau.setHorizontalAlignment(JLabel.CENTER);
        labelNiveau.setVerticalAlignment(JLabel.CENTER);
        labelNiveau.setFont(new Font(labelNiveau.getFont().getName(), Font.BOLD, 14));
        return labelNiveau;
    }

    private JLabel createLabelDomaine(Domaine domaine) {
        JLabel labelNiveau = new JLabel(domaine.getNom());
        labelNiveau.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

        labelNiveau.setHorizontalAlignment(JLabel.CENTER);
        labelNiveau.setVerticalAlignment(JLabel.CENTER);
        labelNiveau.setFont(new Font(labelNiveau.getFont().getName(), Font.PLAIN, 12));
        return labelNiveau;
    }

    private List<Niveau> getNiveaux() throws Exception {
        if (this.inputNiveau.getSelectedItem() == this.allNiveaux) {
            return NiveauService.getAll();
        } else {
            List<Niveau> list = new ArrayList<Niveau>();
            list.add((Niveau) this.inputNiveau.getSelectedItem());
            return list;
        }
    }

    private List<Eleve> getEleves() throws Exception {
        if (this.inputGroupe.getSelectedItem() == this.allGroupes) {
            return EleveService.getAll();
        } else {
            Groupe groupe = (Groupe) this.inputGroupe.getSelectedItem();
            return groupe.getEleves();
        }
    }

    private void refreshPanel() {
        try {

            for (Niveau niveau : NiveauService.getAll()) {
                for (Component component : this.mapNiveauComponent.get(niveau)) {
                    component.setVisible(true);
                }
            }

            for (Eleve eleve : EleveService.getAll()) {
                for (Component component : this.mapEleveComponent.get(eleve)) {
                    component.setVisible(true);
                }
            }

            List<Niveau> visibleNiveaux = this.getNiveaux();

            for (Niveau niveau : NiveauService.getAll()) {
                for (Component component : this.mapNiveauComponent.get(niveau)) {
                    if (!visibleNiveaux.contains(niveau)) {
                        component.setVisible(false);
                    }
                }
            }

            List<Eleve> visibleEleves = this.getEleves();
            for (Eleve eleve : EleveService.getAll()) {
                for (Component component : this.mapEleveComponent.get(eleve)) {
                    if (!visibleEleves.contains(eleve)) {
                        component.setVisible(false);
                    }
                }
            }

            this.validate();
            this.repaint();
        } catch (Exception e) {
            LOGGER.error("refreshPanel() failed", e);
        }

    }

    private void onNavetteButton() {
        try {
            NavetteGenerator generator = new NavetteGenerator(this.getNiveaux(), this.getEleves());

            String fileName = "navette_"
                    + this.inputGroupe.getSelectedItem().toString().replaceAll(" ", "-")
                    + "_"
                    + this.inputNiveau.getSelectedItem().toString().replaceAll(" ", "-")
                    + ".pdf";

            generator.generate(fileName);
            JOptionPane.showMessageDialog(null, "Le fichier " + fileName + " a été créé", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
            Desktop.getDesktop().open(new File(fileName));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "La génération de la fiche navette a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("La génération des navettes a échoué", e);
        }
    }

}
