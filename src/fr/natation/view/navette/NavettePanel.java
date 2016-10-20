package fr.natation.view.navette;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import fr.natation.Utils;
import fr.natation.model.Competence;
import fr.natation.model.Domaine;
import fr.natation.model.Eleve;
import fr.natation.model.Niveau;
import fr.natation.service.CompetenceService;
import fr.natation.service.DomaineService;
import fr.natation.service.EleveService;
import fr.natation.service.NiveauService;
import fr.natation.view.GridBagConstraintsFactory;
import fr.natation.view.VerticalLabel;

public class NavettePanel extends JPanel {

    public NavettePanel() throws Exception {

        this.setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(this.createViewPanel());
        this.setPreferredSize(new Dimension(450, 110));
        this.add(scrollPane, BorderLayout.CENTER);

    }

    private JPanel createViewPanel() throws Exception {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        int x = 3;
        for (Niveau niveau : this.getNiveaux()) {

            GridBagConstraints constraint = GridBagConstraintsFactory.create(x, 0, niveau.getCompetencesCount(), 1);
            constraint.fill = GridBagConstraints.BOTH;

            panel.add(this.createLabelNiveau(niveau), constraint);

            int domaineX = x;
            for (Domaine domaine : DomaineService.getAll()) {
                List<Competence> competences = CompetenceService.get(niveau, domaine);

                int competenceCount = competences.size();
                constraint = GridBagConstraintsFactory.create(domaineX, 1, competenceCount, 1);
                constraint.fill = GridBagConstraints.BOTH;

                panel.add(this.createLabelDomaine(domaine), constraint);

                int competenceX = domaineX;
                for (Competence competence : competences) {

                    String text = Utils.cutStringHtml(" " + competence.getDescription(), 40);
                    VerticalLabel label = new VerticalLabel(text);
                    label.setRotation(VerticalLabel.ROTATE_LEFT);
                    Border border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);

                    label.setBorder(new CompoundBorder(border, BorderFactory.createEmptyBorder(4, 4, 4, 4)));

                    constraint = GridBagConstraintsFactory.create(competenceX, 2, 1, 1);
                    constraint.fill = GridBagConstraints.BOTH;

                    panel.add(label, constraint);
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

        for (Eleve eleve : EleveService.getAll()) {
            panel.add(this.createLabel(eleve.getNom()), GridBagConstraintsFactory.create(1, y, 1, 1));

            JLabel labelGroupe = this.createLabel(eleve.getGroupeNom());
            labelGroupe.setHorizontalAlignment(JLabel.CENTER);
            panel.add(labelGroupe, GridBagConstraintsFactory.create(2, y, 1, 1));

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

                        panel.add(this.createLabelTitle(text), constraint);
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
        return NiveauService.getAll();
    }

}
