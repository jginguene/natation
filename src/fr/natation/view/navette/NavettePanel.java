package fr.natation.view.navette;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import fr.natation.Utils;
import fr.natation.model.Competence;
import fr.natation.model.Domaine;
import fr.natation.model.Niveau;
import fr.natation.service.CompetenceService;
import fr.natation.service.DomaineService;
import fr.natation.service.NiveauService;
import fr.natation.view.GridBagConstraintsFactory;
import fr.natation.view.VerticalLabel;

public class NavettePanel extends JPanel {

    public NavettePanel() throws Exception {

        this.setLayout(new BorderLayout());

        this.add(this.createViewPanel(), BorderLayout.CENTER);

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
            break;
        }

        return panel;

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
