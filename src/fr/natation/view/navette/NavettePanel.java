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
import fr.natation.view.IRefreshListener;
import fr.natation.view.VerticalLabel;

public class NavettePanel extends JPanel implements IRefreshListener {

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
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    NavettePanel.this.refreshPanel();
                }
            }
        });

        this.inputGroupe.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    NavettePanel.this.refreshPanel();
                }
            }
        });

        this.navetteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                NavettePanel.this.onNavetteButton();
            }
        });

    }

    @Override
    public void refresh() throws Exception {
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

        this.refreshPanel();
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
            this.register(niveau, labelNiveau);

            int domaineX = x;
            for (Domaine domaine : DomaineService.getAll()) {
                List<Competence> competences = CompetenceService.get(niveau, domaine);

                int competenceCount = competences.size();
                constraint = GridBagConstraintsFactory.create(domaineX, 1, competenceCount, 1);
                constraint.fill = GridBagConstraints.BOTH;

                JLabel labelDomaine = this.createLabelDomaine(domaine);
                panel.add(labelDomaine, constraint);
                this.register(niveau, labelDomaine);

                int competenceX = domaineX;
                for (Competence competence : competences) {

                    JLabel labelCompetence = this.createLabelCompetence(competence);
                    constraint = GridBagConstraintsFactory.create(competenceX, 2, 1, 1);
                    constraint.fill = GridBagConstraints.BOTH;
                    panel.add(labelCompetence, constraint);
                    this.register(niveau, labelCompetence);
                    competenceX++;
                }

                domaineX += competenceCount;
            }

            x += niveau.getCompetencesCount();

        }

        int y = 3;

        GridBagConstraints constraint = GridBagConstraintsFactory.create(1, 2, 1, 1);
        constraint.anchor = GridBagConstraints.PAGE_END;

        JLabel titleEleve = this.createLabelTitle("Nom de l'élève", Color.WHITE);
        titleEleve.setPreferredSize(new Dimension(200, 20));
        panel.add(titleEleve, constraint);
        constraint.gridx++;

        JLabel titleGroupe = this.createLabelTitle("Groupe", Color.WHITE);
        titleGroupe.setPreferredSize(new Dimension(80, 20));
        panel.add(titleGroupe, constraint);

        int i = 0;
        for (Eleve eleve : this.getEleves()) {
            Color backgroundColor = Color.white;
            if (i % 2 == 0) {
                backgroundColor = Color.LIGHT_GRAY;
            }
            i++;

            JLabel labelEleve = this.createLabel(eleve.toString(), backgroundColor);
            labelEleve.setName("nom");

            panel.add(labelEleve, GridBagConstraintsFactory.create(1, y, 1, 1));
            this.register(eleve, labelEleve);

            JLabel labelGroupe = this.createLabel(eleve.getGroupeNom(), backgroundColor);
            labelGroupe.setName("groupe");
            labelGroupe.setHorizontalAlignment(JLabel.CENTER);
            panel.add(labelGroupe, GridBagConstraintsFactory.create(2, y, 1, 1));
            this.register(eleve, labelGroupe);

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

                        JLabel labelCompetence = this.createLabelTitle(text, backgroundColor);
                        panel.add(labelCompetence, constraint);

                        this.register(niveau, labelCompetence);
                        this.register(eleve, labelCompetence);
                        competenceX++;

                    }
                }
            }
            y++;
        }

        return panel;

    }

    private JLabel createLabel(String text, Color backgroundColor) {
        JLabel label = new JLabel(text);
        label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 12));

        label.setBackground(backgroundColor);
        label.setOpaque(true);

        return label;
    }

    private JLabel createLabelTitle(String title, Color backgroundColor) {
        JLabel label = new JLabel(title);
        label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 12));

        label.setBackground(backgroundColor);
        label.setOpaque(true);
        return label;
    }

    private JLabel createLabelNiveau(Niveau niveau) {
        JLabel label = new JLabel("Niveau " + niveau.getNom());
        label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font(this.labelNiveau.getFont().getName(), Font.BOLD, 14));

        label.setBackground(Color.WHITE);
        label.setOpaque(true);
        return label;
    }

    private JLabel createLabelDomaine(Domaine domaine) {
        JLabel label = new JLabel(domaine.getNom());
        label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font(this.labelNiveau.getFont().getName(), Font.PLAIN, 12));
        label.setBackground(Color.white);
        label.setOpaque(true);

        return label;
    }

    private JLabel createLabelCompetence(Competence competence) {
        String text = Utils.cutStringHtml(" " + competence.getDescription(), 40);
        VerticalLabel label = new VerticalLabel(text);
        label.setRotation(VerticalLabel.ROTATE_LEFT);
        Border border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);
        label.setBackground(Color.white);
        label.setOpaque(true);
        label.setBorder(new CompoundBorder(border, BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        return label;
    }

    private List<Niveau> getNiveaux() throws Exception {
        Niveau selectedNiveau = (Niveau) this.inputNiveau.getSelectedItem();
        if (selectedNiveau.getId() == this.allNiveaux.getId()) {
            return NiveauService.getAll();
        } else {
            List<Niveau> list = new ArrayList<Niveau>();
            list.add(selectedNiveau);
            return list;
        }
    }

    private List<Eleve> getEleves() throws Exception {
        if (this.inputGroupe.getSelectedItem().equals(this.allGroupes)) {
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
            List<Niveau> niveauxToHide = new ArrayList<>(NiveauService.getAll());
            niveauxToHide.removeAll(visibleNiveaux);
            for (Niveau niveau : niveauxToHide) {
                for (Component component : this.mapNiveauComponent.get(niveau)) {
                    component.setVisible(false);
                }
            }

            List<Eleve> visibleEleves = this.getEleves();
            List<Eleve> elevesToHide = new ArrayList<>(EleveService.getAll());
            elevesToHide.removeAll(visibleEleves);
            for (Eleve eleve : elevesToHide) {
                for (Component component : this.mapEleveComponent.get(eleve)) {
                    component.setVisible(false);
                }
            }

            for (Eleve eleve : visibleEleves) {
                for (Component component : this.mapEleveComponent.get(eleve)) {
                    if ("groupe".equals(component.getName())) {
                        JLabel labelGroupe = (JLabel) component;
                        labelGroupe.setText(eleve.getGroupeNom());
                    }
                    if ("nom".equals(component.getName())) {
                        JLabel labelNom = (JLabel) component;
                        labelNom.setText(eleve.toString());

                    }
                }
            }

            long stop = System.currentTimeMillis();

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

    private void register(Eleve eleve, Component component) {
        this.mapEleveComponent.get(eleve).add(component);
    }

    private void register(Niveau niveau, Component component) {
        this.mapNiveauComponent.get(niveau).add(component);
    }

}
