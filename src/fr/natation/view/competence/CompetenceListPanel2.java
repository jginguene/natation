package fr.natation.view.competence;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.natation.Utils;
import fr.natation.model.Capacite;
import fr.natation.model.Competence;
import fr.natation.model.Domaine;
import fr.natation.model.Niveau;
import fr.natation.service.CompetenceService;
import fr.natation.service.DomaineService;
import fr.natation.service.NiveauService;
import fr.natation.view.GridBagConstraintsFactory;
import fr.natation.view.ViewUtils;

public class CompetenceListPanel2 extends JPanel {

    private static final long serialVersionUID = 1L;

    private final static int WIDTH = 200;
    private final static int HEIGHT = 60;

    public CompetenceListPanel2() throws Exception {
        this.setLayout(new GridBagLayout());
        JPanel panel = this.createPanel();
        this.add(panel, GridBagConstraintsFactory.create(1, 1, 1, 1));

    }

    public JPanel createPanel() throws Exception {

        List<Niveau> niveaux = NiveauService.getAll();
        List<Domaine> domaines = DomaineService.getAll();

        JPanel panel = new JPanel();

        panel.setLayout(new GridBagLayout());

        int y = 0;
        int x = 0;
        JLabel labelCorner = new JLabel("");
        labelCorner.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        GridBagConstraints constraints = GridBagConstraintsFactory.create(x, y, 1, 1);
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(labelCorner, constraints);
        x++;

        Map<Niveau, Integer> maxCompetencePerNiveau = new HashMap<>();
        for (Niveau niveau : niveaux) {
            for (Domaine domaine : domaines) {
                int count = CompetenceService.get(niveau, domaine).size();
                if (!maxCompetencePerNiveau.containsKey(niveau)) {
                    maxCompetencePerNiveau.put(niveau, count);
                } else {
                    maxCompetencePerNiveau.put(niveau, Math.max(count, maxCompetencePerNiveau.get(niveau)));
                }
            }
        }

        for (Domaine domaine : domaines) {
            JLabel labelDomaine = this.createDomaineLabel(domaine);
            panel.add(labelDomaine, GridBagConstraintsFactory.create(x, 0, 1, 1));
            x++;
        }

        int niveauY = 1;

        for (Niveau niveau : niveaux) {
            x = 1;
            int maxNiveauCompetenceCount = 0;
            for (Domaine domaine : domaines) {

                int currentNiveauCompetenceCount = 0;

                y = niveauY;

                List<Competence> competences = CompetenceService.get(niveau, domaine);
                for (Competence competence : competences) {

                    Component competenceComponent = this.createCompetenceComponent(competence, niveau);
                    GridBagConstraints constraint = GridBagConstraintsFactory.create(x, y, 1, 1);
                    constraint.fill = GridBagConstraints.BOTH;
                    panel.add(competenceComponent, constraint);
                    y++;
                    currentNiveauCompetenceCount++;

                }

                if (currentNiveauCompetenceCount < maxCompetencePerNiveau.get(niveau)) {
                    GridBagConstraints constraint = GridBagConstraintsFactory.create(x, y, 1, maxCompetencePerNiveau.get(niveau) - currentNiveauCompetenceCount);
                    constraint.fill = GridBagConstraints.BOTH;
                    JLabel label = new JLabel("");
                    label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.black));
                    label.setHorizontalAlignment(JLabel.CENTER);
                    panel.add(label, constraint);
                }

                x++;

            }

            JLabel labelNiveau = this.createNiveauLabel(niveau);
            GridBagConstraints constraint = GridBagConstraintsFactory.create(0, niveauY, 1, maxCompetencePerNiveau.get(niveau));
            constraint.fill = GridBagConstraints.BOTH;
            panel.add(labelNiveau, constraint);

            niveauY += maxCompetencePerNiveau.get(niveau);
        }

        return panel;

    }

    private JLabel createDomaineLabel(Domaine domaine) {
        JLabel label = new JLabel(domaine.getNom());
        label.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.black));
        label.setSize(WIDTH, HEIGHT);
        label.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 16));
        return label;
    }

    private JLabel createNiveauLabel(Niveau niveau) {
        String txt = "Niveau " + niveau.getNom();
        String labelText = "<html>";
        for (int i = 0; i < txt.length(); i++) {
            labelText += "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + txt.substring(i, i + 1) + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>";
        }
        labelText += "</html>";

        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 14));
        label.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.black));
        label.setBackground(this.getNiveauColor(niveau));
        label.setOpaque(true);
        return label;

    }

    private Component createCompetenceComponent(Competence competence, Niveau niveau) throws Exception {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        String text = Utils.cutStringHtml(competence.getDescription(), 40);
        JLabel labelCompetence = new JLabel(text);

        labelCompetence.setBackground(this.getNiveauColor(niveau));
        labelCompetence.setOpaque(true);
        labelCompetence.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        panel.add(labelCompetence, BorderLayout.CENTER);

        Capacite capacite = competence.getCapacite();

        if (capacite != null) {
            JLabel labelIcon = new JLabel(ViewUtils.getCapaciteIcon(capacite, 40));
            panel.add(labelIcon, BorderLayout.WEST);
            panel.setBackground(this.getNiveauColor(niveau));
            labelIcon.setSize(new Dimension(20, 20));
            labelIcon.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        }

        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.black));
        return panel;
    }

    private Color getNiveauColor(Niveau niveau) {

        if (niveau.getNom().equals("1")) {
            return new Color(255, 153, 204);
        }

        if (niveau.getNom().equals("2")) {
            return new Color(153, 204, 255);
        }

        if (niveau.getNom().equals("3")) {
            return new Color(204, 255, 204);
        }

        return Color.GRAY;
    }

}
