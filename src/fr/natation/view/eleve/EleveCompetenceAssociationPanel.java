package fr.natation.view.eleve;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import fr.natation.Utils;
import fr.natation.model.Competence;
import fr.natation.model.Domaine;
import fr.natation.model.Eleve;
import fr.natation.model.Niveau;
import fr.natation.service.CompetenceService;
import fr.natation.service.DomaineService;
import fr.natation.service.NiveauService;
import fr.natation.view.GridBagConstraintsFactory;

public class EleveCompetenceAssociationPanel extends JPanel implements IEleveSelectListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(EleveCompetenceAssociationPanel.class.getName());

    private Eleve eleve;

    //  private final CompetenceSelectionManager manager = new CompetenceSelectionManager();
    private final Map<Niveau, JLabel> map = new HashMap<Niveau, JLabel>();

    private final Map<Competence, JCheckBox> mapCompetence = new HashMap<>();

    public EleveCompetenceAssociationPanel() throws Exception {

        this.setBorder(BorderFactory.createTitledBorder("Competences de l'élève"));

        List<Niveau> niveaux = NiveauService.getAll();

        this.setLayout(new GridBagLayout());

        this.add(new JLabel());

        int x = 1;

        for (Niveau niveau : niveaux) {
            JLabel labelNiveau = new JLabel("Niveau " + niveau.getNom());
            labelNiveau.setHorizontalAlignment(JLabel.CENTER);
            labelNiveau.setVerticalAlignment(JLabel.BOTTOM);
            labelNiveau.setFont(labelNiveau.getFont().deriveFont(Font.BOLD, 14));
            labelNiveau.setBorder(BorderFactory.createLineBorder(Color.black));

            this.add(labelNiveau, GridBagConstraintsFactory.create(x, 0, 2, 1));

            x += 2;
        }

        List<Domaine> domaines = DomaineService.getAll();

        int y = 1;

        for (Domaine domaine : domaines) {

            JLabel labelDomaine = new JLabel("  " + domaine.getNom() + "  ");
            labelDomaine.setBorder(BorderFactory.createLineBorder(Color.black));
            labelDomaine.setFont(labelDomaine.getFont().deriveFont(Font.BOLD, 14));

            this.add(labelDomaine, GridBagConstraintsFactory.create(0, y, 1, 2));

            x = 1;
            for (Niveau niveau : niveaux) {
                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createLineBorder(Color.black));
                panel.setLayout(new GridLayout(5, 2));
                for (Competence competence : CompetenceService.get(niveau, domaine)) {

                    JCheckBox checkBox = new JCheckBox(Utils.cutStringHtml(competence.getDescription(), 40));

                    checkBox.setToolTipText(competence.getDescription());
                    checkBox.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            EleveCompetenceAssociationPanel.this.refreshScore();
                        }
                    });
                    this.mapCompetence.put(competence, checkBox);

                    panel.add(checkBox);

                }
                this.add(panel, GridBagConstraintsFactory.create(x, y, 2, 2));

                x += 2;
            }
            y += 2;
        }

        JLabel total = new JLabel("Total");
        total.setFont(total.getFont().deriveFont(Font.BOLD));
        total.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.add(total, GridBagConstraintsFactory.create(0, y, 1, 1));

        x = 1;
        for (Niveau niveau : niveaux) {
            JLabel label = new JLabel("0");
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setFont(label.getFont().deriveFont(Font.BOLD));
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.add(label, GridBagConstraintsFactory.create(x, y, 2, 1));
            x += 2;
            this.map.put(niveau, label);
        }
    }

    @Override
    public void onChange(Eleve newEleve, Object source) {
        this.eleve = newEleve;

        try {
            List<Competence> eleveCompetences = newEleve.getCompetences();

            for (Competence competence : CompetenceService.getAll()) {
                JCheckBox checkbox = this.mapCompetence.get(competence);
                if (checkbox != null) {
                    checkbox.setSelected(eleveCompetences.contains(competence));
                    checkbox.repaint();
                }
            }
            this.refreshScore();
        } catch (Exception e) {
            LOGGER.error("onChange(" + newEleve + ") failed", e);
        }
    }

    public void refreshScore() {
        try {
            Map<Niveau, Integer> map = new HashMap<Niveau, Integer>();

            for (Competence competence : CompetenceService.getAll()) {
                JCheckBox checkbox = this.mapCompetence.get(competence);
                if (checkbox != null && checkbox.isSelected()) {

                    if (!map.containsKey(competence.getNiveau())) {
                        map.put(competence.getNiveau(), 0);
                    }

                    int nb = map.get(competence.getNiveau()) + 1;
                    map.put(competence.getNiveau(), nb);
                }
            }

            for (Niveau niveau : NiveauService.getAll()) {
                int nbNiveau = 0;
                if (map.containsKey(niveau)) {
                    nbNiveau = map.get(niveau);
                }
                this.map.get(niveau).setText(Integer.toString(nbNiveau));
            }
        } catch (Exception e) {
            LOGGER.error("refreshScore() failed", e);
        }
    }

    public void updateEleve(Eleve eleve) {
        try {
            CompetenceService.removeAll(eleve);

            for (Competence competence : CompetenceService.getAll()) {
                JCheckBox checkbox = this.mapCompetence.get(competence);
                if (checkbox.isSelected()) {
                    CompetenceService.add(competence, eleve);
                }
            }

        } catch (Exception e) {
            LOGGER.error("updateEleve(" + eleve + ") failed", e);
        }
    }

}
