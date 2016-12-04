package fr.natation.view.eleve;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import fr.natation.Utils;
import fr.natation.model.Bilan;
import fr.natation.model.Capacite;
import fr.natation.model.Competence;
import fr.natation.model.Domaine;
import fr.natation.model.Eleve;
import fr.natation.model.Niveau;
import fr.natation.model.Status;
import fr.natation.service.CapaciteService;
import fr.natation.service.CompetenceService;
import fr.natation.service.DomaineService;
import fr.natation.service.NiveauService;
import fr.natation.view.GridBagConstraintsFactory;
import fr.natation.view.Icon;
import fr.natation.view.ViewUtils;

public class EleveCompetenceAssociationPanel extends JPanel implements IEleveSelectListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(EleveCompetenceAssociationPanel.class.getName());

    private Eleve eleve;

    private final Map<Niveau, JLabel> map = new HashMap<Niveau, JLabel>();

    private final Map<Competence, JCheckBox> mapCompetence = new HashMap<>();
    private final Map<Niveau, JLabel> mapNiveau = new HashMap<>();

    private final Map<Domaine, JLabel> mapDomaine = new HashMap<>();

    private final JLabel labelCapacite;

    public EleveCompetenceAssociationPanel() throws Exception {

        this.setBorder(BorderFactory.createTitledBorder("Competences de l'élève"));
        List<Niveau> niveaux = NiveauService.getAll();
        this.setLayout(new GridBagLayout());
        this.add(new JLabel());
        int x = 1;

        this.labelCapacite = new JLabel();
        this.labelCapacite.setHorizontalAlignment(JLabel.CENTER);
        this.labelCapacite.setVerticalAlignment(JLabel.CENTER);
        this.labelCapacite.setBorder(BorderFactory.createLineBorder(Color.black));
        this.add(this.labelCapacite, GridBagConstraintsFactory.create(0, 0, 1, 1));

        for (Niveau niveau : niveaux) {
            JLabel labelNiveau = new JLabel("Niveau " + niveau.getNom());
            labelNiveau.setHorizontalAlignment(JLabel.CENTER);
            labelNiveau.setVerticalAlignment(JLabel.CENTER);
            labelNiveau.setFont(labelNiveau.getFont().deriveFont(Font.BOLD, 14));
            labelNiveau.setBorder(BorderFactory.createLineBorder(Color.black));
            this.add(labelNiveau, GridBagConstraintsFactory.create(x, 0, 2, 1));
            this.mapNiveau.put(niveau, labelNiveau);
            x += 2;
        }

        List<Domaine> domaines = DomaineService.getAll();

        int y = 1;

        for (Domaine domaine : domaines) {
            JLabel labelDomaine = new JLabel("  " + domaine.getNom() + "  ");
            labelDomaine.setBorder(BorderFactory.createLineBorder(Color.black));
            labelDomaine.setFont(labelDomaine.getFont().deriveFont(Font.BOLD, 14));
            this.mapDomaine.put(domaine, labelDomaine);
            this.add(labelDomaine, GridBagConstraintsFactory.create(0, y, 1, 2));

            x = 1;
            for (Niveau niveau : niveaux) {
                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createLineBorder(Color.black));
                panel.setLayout(new GridLayout(5, 2));
                for (Competence competence : CompetenceService.get(niveau, domaine)) {

                    JPanel panelCheckBox = new JPanel();
                    panelCheckBox.setLayout(new BorderLayout());
                    JCheckBox checkBox = new JCheckBox(Utils.cutStringHtml(competence.getDescription(), 40));

                    checkBox.setToolTipText(competence.getDescription());
                    checkBox.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            EleveCompetenceAssociationPanel.this.refreshScore();
                        }
                    });
                    this.mapCompetence.put(competence, checkBox);

                    Capacite capacite = competence.getCapacite();
                    if (capacite != null) {
                        panelCheckBox.add(new JLabel(ViewUtils.getCapaciteIcon(capacite, 40)), BorderLayout.EAST);
                    }

                    panelCheckBox.add(checkBox, BorderLayout.CENTER);
                    panel.add(panelCheckBox);

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

    private ImageIcon getImageStatus(Status status) {
        switch (status) {
        case Green:
            return Icon.Green.getImage();

        case Blue:
            return Icon.Blue.getImage();
        case Orange:
            return Icon.Orange.getImage();

        default:
            return Icon.Red.getImage();

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
            Map<Integer, Integer> mapNiveauCompetenceCount = new HashMap<Integer, Integer>();
            Map<Integer, Integer> mapDomaineCompetenceCount = new HashMap<Integer, Integer>();
            List<Competence> selectedCompetences = new ArrayList<Competence>();

            for (Competence competence : CompetenceService.getAll()) {
                JCheckBox checkbox = this.mapCompetence.get(competence);
                if (checkbox != null && checkbox.isSelected()) {
                    selectedCompetences.add(competence);

                    if (!mapNiveauCompetenceCount.containsKey(competence.getNiveau().getId())) {
                        mapNiveauCompetenceCount.put(competence.getNiveau().getId(), 0);
                    }

                    int niveauCount = mapNiveauCompetenceCount.get(competence.getNiveau().getId()) + 1;
                    mapNiveauCompetenceCount.put(competence.getNiveau().getId(), niveauCount);

                    if (competence != null && competence.getNiveau() != null && competence.getNiveau().equals(this.eleve.getRequiredNiveau())) {
                        Domaine domaine = competence.getDomaine();
                        if (!mapDomaineCompetenceCount.containsKey(domaine.getId())) {
                            mapDomaineCompetenceCount.put(domaine.getId(), 0);
                        }

                        int domainCount = mapDomaineCompetenceCount.get(domaine.getId()) + 1;
                        mapDomaineCompetenceCount.put(domaine.getId(), domainCount);
                    }
                }

            }

            for (Niveau niveau : NiveauService.getAll()) {
                int nbNiveau = 0;
                if (mapNiveauCompetenceCount.containsKey(niveau.getId())) {
                    nbNiveau = mapNiveauCompetenceCount.get(niveau.getId());
                }
                this.map.get(niveau).setText(Integer.toString(nbNiveau));
            }

            Bilan bilan = new Bilan(this.eleve);
            for (Niveau niveau : NiveauService.getAll()) {

                JLabel labelNiveau = this.mapNiveau.get(niveau);

                if (niveau.equals(this.eleve.getRequiredNiveau())) {
                    Status status;
                    if (mapNiveauCompetenceCount.containsKey(niveau.getId())) {
                        status = bilan.getStatus(niveau, selectedCompetences);

                    } else {
                        status = Status.Red;
                    }
                    labelNiveau.setIcon(this.getImageStatus(status));
                } else {
                    labelNiveau.setIcon(null);
                }
                labelNiveau.repaint();
            }

            for (Domaine domaine : DomaineService.getAll()) {
                JLabel labelDomaine = this.mapDomaine.get(domaine);
                Status status;
                if (mapDomaineCompetenceCount.containsKey(domaine.getId())) {
                    status = bilan.getStatus(this.eleve.getRequiredNiveau(), domaine, mapDomaineCompetenceCount.get(domaine.getId()));
                } else {
                    status = Status.Red;
                }
                labelDomaine.setIcon(this.getImageStatus(status));

            }

            Capacite eleveCapacite = null;
            for (Capacite capacite : CapaciteService.getAll()) {
                List<Competence> allCapaciteCompetence = capacite.getCompetences();
                if (!allCapaciteCompetence.isEmpty() && selectedCompetences.containsAll(allCapaciteCompetence)) {
                    eleveCapacite = capacite;
                }
            }

            if (eleveCapacite != null) {
                this.labelCapacite.setIcon(ViewUtils.getCapaciteIcon(eleveCapacite, 50));
            } else {
                this.labelCapacite.setIcon(null);
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
