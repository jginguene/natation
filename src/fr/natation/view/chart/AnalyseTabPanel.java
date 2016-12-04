package fr.natation.view.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import fr.natation.model.Bilan;
import fr.natation.model.Capacite;
import fr.natation.model.Competence;
import fr.natation.model.Domaine;
import fr.natation.model.Eleve;
import fr.natation.model.Groupe;
import fr.natation.model.Niveau;
import fr.natation.model.Status;
import fr.natation.pdf.PdfUtils;
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
import fr.natation.view.IEleveUpdateListener;
import fr.natation.view.IRefreshListener;
import fr.natation.view.Icon;
import fr.natation.view.StatusLabel;
import fr.natation.view.VerticalLabel;
import fr.natation.view.ViewUtils;

public class AnalyseTabPanel extends JPanel implements IRefreshListener, IEleveUpdateListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(AnalyseTabPanel.class.getName());

    private final JLabel labelGroupe = new JLabel("Groupe: ");
    private final JLabel labelNiveau = new JLabel("Niveau: ");

    private final JComboBox<Groupe> inputGroupe = new JComboBox<Groupe>();
    private final JComboBox<Niveau> inputNiveau = new JComboBox<Niveau>();

    private final EmptyGroupe allGroupes = new EmptyGroupe("Tous les groupes");
    private final EmptyNiveau allNiveaux = new EmptyNiveau("Tous les niveaux");

    private final Map<Niveau, List<Component>> mapNiveauComponent = new HashMap<Niveau, List<Component>>();
    private final Map<Eleve, List<Component>> mapEleveComponent = new HashMap<Eleve, List<Component>>();

    private final Map<String, StatusLabel> mapEleve = new HashMap<String, StatusLabel>();

    private final JScrollPane scrollPane;

    private final JButton exportButton = ButtonFactory.createExcelButton("Exporter");
    private final Component componentToExport;

    public AnalyseTabPanel() throws Exception {

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

        this.componentToExport = this.createViewPanel();
        this.scrollPane = new JScrollPane(this.componentToExport);

        this.setPreferredSize(new Dimension(450, 110));
        this.add(this.scrollPane, BorderLayout.CENTER);

        this.inputNiveau.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    AnalyseTabPanel.this.refreshPanel();
                }
            }
        });

        this.inputGroupe.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    AnalyseTabPanel.this.refreshPanel();
                }
            }
        });

        this.add(this.exportButton, BorderLayout.SOUTH);

        this.exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnalyseTabPanel.this.onExportButton();

            }
        });

    }

    private void onExportButton() {
        PdfUtils.save("analyse.pdf", this.componentToExport, 20, 100, 0.55f);
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

    }

    private JPanel createViewPanel() throws Exception {

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        int x = 3;

        for (Niveau niveau : this.getNiveaux()) {
            int niveauWidth = 1;
            int niveauX = x;
            Color niveauColor = ViewUtils.getNiveauColor(niveau);
            GridBagConstraints constraint = null;

            this.register(niveau, this.labelNiveau);

            int domaineX = x;
            int domaineCount = 0;
            for (Domaine domaine : DomaineService.getAll()) {

                if (CompetenceService.get(niveau, domaine).size() > 0) {
                    niveauWidth++;

                    constraint = GridBagConstraintsFactory.create(domaineX, 1, 1, 1);
                    constraint.fill = GridBagConstraints.BOTH;

                    JLabel labelDomaine = this.createLabelDomaine(domaine);
                    labelDomaine.setBackground(niveauColor);
                    panel.add(labelDomaine, constraint);
                    this.register(niveau, labelDomaine);

                    domaineCount++;
                    domaineX += 1;
                }
            }

            constraint = GridBagConstraintsFactory.create(domaineX, 1, 1, 1);
            constraint.fill = GridBagConstraints.BOTH;

            JLabel labelTotal = this.createLabelTotal();
            labelTotal.setBackground(niveauColor);
            panel.add(labelTotal, constraint);
            this.register(niveau, labelTotal);
            domaineCount++;

            x += domaineCount;

            constraint = GridBagConstraintsFactory.create(niveauX, 0, niveauWidth, 1);
            constraint.fill = GridBagConstraints.BOTH;

            JLabel labelNiveau = this.createLabelNiveau(niveau);
            labelNiveau.setBackground(niveauColor);
            panel.add(labelNiveau, constraint);

        }

        GridBagConstraints constraint = GridBagConstraintsFactory.create(x++, 0, 1, 2);
        panel.add(this.createLabelTitle("Total"), constraint);

        constraint = GridBagConstraintsFactory.create(x++, 0, 1, 2);
        panel.add(this.createLabelTitle("Niveau atteint"), constraint);

        constraint = GridBagConstraintsFactory.create(x++, 0, 1, 2);
        panel.add(this.createLabelTitle("Savoir nager"), constraint);

        constraint = GridBagConstraintsFactory.create(x++, 0, 1, 2);
        panel.add(this.createLabelTitle("Capacité"), constraint);

        int y = 2;

        constraint = GridBagConstraintsFactory.create(1, 1, 1, 1);
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

            Bilan bilan = new Bilan(eleve);
            Color backgroundColor = Color.white;
            if (i % 2 == 0) {
                backgroundColor = Color.LIGHT_GRAY;
            }
            i++;

            JLabel labelEleve = this.createLabel(eleve.toString(), backgroundColor);
            panel.add(labelEleve, GridBagConstraintsFactory.create(1, y, 1, 1));
            this.register(eleve, labelEleve);

            JLabel labelGroupe = this.createLabel(eleve.getGroupeNom(), backgroundColor);
            labelGroupe.setHorizontalAlignment(JLabel.CENTER);
            panel.add(labelGroupe, GridBagConstraintsFactory.create(2, y, 1, 1));
            this.register(eleve, labelGroupe);

            int domaineX = 3;
            for (Niveau niveau : this.getNiveaux()) {

                int total = 0;
                for (Domaine domaine : DomaineService.getAll()) {

                    if (CompetenceService.get(niveau, domaine).size() > 0) {
                        List<Competence> competences = eleve.getCompetences(niveau, domaine);

                        Status status = bilan.getStatus(niveau, domaine, competences.size());
                        constraint = GridBagConstraintsFactory.create(domaineX, y, 1, 1);
                        total += competences.size();

                        StatusLabel labelTotal = this.createStatusLabel(Integer.toString(competences.size()), status, backgroundColor);

                        panel.add(labelTotal, constraint);

                        this.register(niveau, labelTotal);
                        this.register(eleve, labelTotal);

                        this.register(eleve, niveau, domaine, labelTotal);

                        domaineX++;
                    }
                }

                constraint = GridBagConstraintsFactory.create(domaineX, y, 1, 1);

                Status status = bilan.getStatus(niveau, eleve.getCompetences(niveau));
                StatusLabel labelTotal = this.createStatusTitleLabel(Integer.toString(total), status, backgroundColor);
                panel.add(labelTotal, constraint);

                this.register(niveau, labelTotal);
                this.register(eleve, labelTotal);
                this.register(eleve, niveau, null, labelTotal);
                domaineX++;
            }

            constraint = GridBagConstraintsFactory.create(domaineX++, y, 1, 1);

            JLabel labelTotal = this.createLabelTitle(Integer.toString(eleve.getCompetences().size()), backgroundColor);
            panel.add(labelTotal, constraint);
            this.register(eleve, labelTotal);

            constraint = GridBagConstraintsFactory.create(domaineX++, y, 1, 1);
            JLabel labelNiveauAtteint = this.createLabelTitle(bilan.getNiveauAsStr(), backgroundColor);
            if (bilan.getNiveau() != null) {
                labelNiveauAtteint.setBackground(ViewUtils.getNiveauColor(bilan.getNiveau()));
            }
            panel.add(labelNiveauAtteint, constraint);
            this.register(eleve, labelNiveauAtteint);

            constraint = GridBagConstraintsFactory.create(domaineX++, y, 1, 1);
            JLabel labelSavoirNager = this.createLabel(bilan.getAssnAsStr(), backgroundColor);
            panel.add(labelSavoirNager, constraint);
            this.register(eleve, labelSavoirNager);

            constraint = GridBagConstraintsFactory.create(domaineX++, y, 1, 1);
            JLabel labelCapactite = this.createLabelTitle("", backgroundColor);
            Capacite capacite = eleve.getCapacite();
            if (capacite != null) {
                labelCapactite.setIcon(ViewUtils.getCapaciteIcon(capacite, Icon.Green.getImage().getIconHeight()));
            }
            panel.add(labelCapactite, constraint);
            this.register(eleve, labelCapactite);

            y++;

        }

        return panel;

    }

    private StatusLabel createStatusLabel(String text, Status status, Color backgroundColor) {
        StatusLabel label = new StatusLabel(status, text);
        label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 12));

        label.setBackground(backgroundColor);
        label.setOpaque(true);
        return label;
    }

    private StatusLabel createStatusTitleLabel(String text, Status status, Color backgroundColor) {
        StatusLabel label = new StatusLabel(status, text);
        label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 14));

        label.setBackground(backgroundColor);
        label.setOpaque(true);
        return label;
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
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 16));

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
        VerticalLabel label = new VerticalLabel("  " + domaine.getNom() + "  ");
        label.setRotation(VerticalLabel.ROTATE_LEFT);

        label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font(this.labelNiveau.getFont().getName(), Font.PLAIN, 12));
        label.setBackground(Color.white);
        label.setOpaque(true);

        return label;
    }

    private JLabel createLabelTitle(String title) {
        VerticalLabel label = new VerticalLabel("  " + title + "  ");
        label.setRotation(VerticalLabel.ROTATE_LEFT);

        label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font(this.labelNiveau.getFont().getName(), Font.BOLD, 14));
        label.setBackground(Color.white);
        label.setOpaque(true);

        return label;
    }

    private JLabel createLabelTotal() {
        VerticalLabel label = new VerticalLabel("  Total  ");
        label.setRotation(VerticalLabel.ROTATE_LEFT);
        label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font(this.labelNiveau.getFont().getName(), Font.BOLD, 14));
        label.setBackground(Color.white);
        label.setOpaque(true);

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

            for (Eleve eleve : visibleEleves)

                this.validate();
            this.repaint();

        } catch (Exception e) {
            LOGGER.error("refreshPanel() failed", e);
        }

    }

    private void register(Eleve eleve, Component component) {
        this.mapEleveComponent.get(eleve).add(component);
    }

    private void register(Niveau niveau, Component component) {
        this.mapNiveauComponent.get(niveau).add(component);
    }

    private void register(Eleve eleve, Niveau niveau, Domaine domaine, StatusLabel label) {
        this.mapEleve.put(this.getKey(eleve, niveau, domaine), label);
    }

    private StatusLabel getEleveComponent(Eleve eleve, Niveau niveau, Domaine domaine) {
        return this.mapEleve.get(this.getKey(eleve, niveau, domaine));
    }

    private String getKey(Eleve eleve, Niveau niveau, Domaine domaine) {
        String key = eleve.getId() + "#" + niveau.getId() + "#";
        if (domaine != null) {
            key += domaine.getId();
        }
        return key;

    }

    @Override
    public void refresh(Eleve eleve) throws Exception {

        Bilan bilan = new Bilan(eleve);

        for (Niveau niveau : this.getNiveaux()) {

            int total = 0;
            for (Domaine domaine : DomaineService.getAll()) {

                if (CompetenceService.get(niveau, domaine).size() > 0) {
                    List<Competence> competences = eleve.getCompetences(niveau, domaine);

                    StatusLabel labelTotal = this.getEleveComponent(eleve, niveau, domaine);
                    Status status = bilan.getStatus(niveau, domaine, competences.size());
                    labelTotal.update(Integer.toString(competences.size()), status);
                    total += competences.size();
                }

            }

            StatusLabel labelTotal = this.getEleveComponent(eleve, niveau, null);
            Status status = bilan.getStatus(niveau, eleve.getCompetences(niveau));
            labelTotal.update(Integer.toString(total), status);

        }

    }

}
