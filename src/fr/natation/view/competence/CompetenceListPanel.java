package fr.natation.view.competence;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import fr.natation.Utils;
import fr.natation.model.Capacite;
import fr.natation.model.Competence;
import fr.natation.model.Domaine;
import fr.natation.model.Niveau;
import fr.natation.pdf.PdfUtils;
import fr.natation.service.CompetenceService;
import fr.natation.service.DomaineService;
import fr.natation.service.NiveauService;
import fr.natation.view.ButtonFactory;
import fr.natation.view.GridBagConstraintsFactory;
import fr.natation.view.Icon;
import fr.natation.view.ViewUtils;

public class CompetenceListPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(CompetenceListPanel.class.getName());

    private final static int WIDTH = 200;
    private final static int HEIGHT = 60;

    private final JButton exportButton = ButtonFactory.createPdfButton("Exporter");
    private final JPanel panel;

    public CompetenceListPanel() throws Exception {
        this.setLayout(new GridBagLayout());
        this.panel = this.createPanel();
        this.add(this.panel, GridBagConstraintsFactory.create(1, 1, 1, 1));
        this.add(this.exportButton, GridBagConstraintsFactory.create(1, 2, 1, 1));

        this.exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CompetenceListPanel.this.onExportButton();

            }
        });

    }

    private void onExportButton() {
        PdfUtils.save("competences.pdf", this.panel, 20, 100, 0.55f);
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

    private JLabel createDomaineLabel(final Domaine domaine) {

        JLabel label = new JLabel(domaine.getNom());

        if (domaine.getUrl() != null) {
            label.setIcon(Icon.View.getImage());
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));

            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    CompetenceListPanel.this.onLink(domaine);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }

            });
        }

        label.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.black));
        label.setSize(WIDTH, HEIGHT);
        label.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 16));
        return label;
    }

    private void onLink(Domaine domaine) {
        try {
            Desktop.getDesktop().browse(new URI(domaine.getUrl()));
        } catch (Exception ex) {

        }

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
        label.setBackground(ViewUtils.getNiveauColor(niveau));
        label.setOpaque(true);
        return label;

    }

    private Component createCompetenceComponent(Competence competence, Niveau niveau) throws Exception {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        String text = Utils.cutStringHtml(competence.getDescription(), 40);
        JLabel labelCompetence = new JLabel(text);

        labelCompetence.setBackground(ViewUtils.getNiveauColor(niveau));
        labelCompetence.setOpaque(true);
        labelCompetence.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        panel.add(labelCompetence, BorderLayout.CENTER);

        Capacite capacite = competence.getCapacite();

        if (capacite != null) {
            JLabel labelIcon = new JLabel(ViewUtils.getCapaciteIcon(capacite, 40));
            panel.add(labelIcon, BorderLayout.WEST);
            panel.setBackground(ViewUtils.getNiveauColor(niveau));
            labelIcon.setSize(new Dimension(20, 20));
            labelIcon.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        }

        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.black));
        return panel;
    }

}
