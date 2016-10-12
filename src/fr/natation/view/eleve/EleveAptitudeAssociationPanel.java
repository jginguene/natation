package fr.natation.view.eleve;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import fr.natation.model.Aptitude;
import fr.natation.model.Eleve;
import fr.natation.model.Niveau;
import fr.natation.model.TypeAptitude;
import fr.natation.service.AptitudeService;
import fr.natation.service.NiveauService;
import fr.natation.service.TypeAptitudeService;
import fr.natation.view.AptitudeComboModel;
import fr.natation.view.AptitudeSelectionManager;

public class EleveAptitudeAssociationPanel extends JPanel implements IEleveSelectListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(EleveAptitudeAssociationPanel.class.getName());

    private Eleve eleve;
    private final GridLayout layout;

    private final AptitudeSelectionManager manager = new AptitudeSelectionManager();
    private final Map<Niveau, JLabel> map = new HashMap<Niveau, JLabel>();

    public EleveAptitudeAssociationPanel() throws Exception {

        this.setBorder(BorderFactory.createTitledBorder("Aptitudes de l'élève"));

        List<Niveau> niveaux = NiveauService.getAll();
        this.layout = new GridLayout(0, 1 + niveaux.size());
        this.setLayout(this.layout);

        this.add(new JLabel());

        for (Niveau niveau : niveaux) {
            JLabel labelNiveau = new JLabel("Niveau " + niveau.getNom());
            labelNiveau.setHorizontalAlignment(JLabel.CENTER);
            labelNiveau.setVerticalAlignment(JLabel.BOTTOM);
            labelNiveau.setFont(labelNiveau.getFont().deriveFont(Font.BOLD));
            this.add(labelNiveau);
        }

        List<TypeAptitude> types = TypeAptitudeService.getAll();

        for (TypeAptitude type : types) {
            this.add(new JLabel(type.getNom()));
            for (Niveau niveau : niveaux) {
                JComboBox<AptitudeComboModel> comboBox = this.manager.getComboBox(niveau, type);
                comboBox.setSelectedIndex(0);

                this.add(this.manager.getComboBox(niveau, type));
            }
        }

        JLabel total = new JLabel("Total");
        total.setFont(total.getFont().deriveFont(Font.BOLD));
        this.add(new JLabel("Total"));

        for (Niveau niveau : niveaux) {
            JLabel label = new JLabel("0");
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setFont(label.getFont().deriveFont(Font.BOLD));

            this.add(label);
            this.map.put(niveau, label);
        }

    }

    @Override
    public void onChange(Eleve newEleve) {
        this.eleve = newEleve;

        try {
            List<Niveau> niveaux = NiveauService.getAll();
            List<TypeAptitude> types = TypeAptitudeService.getAll();

            for (Niveau niveau : niveaux) {
                for (TypeAptitude type : types) {
                    JComboBox<AptitudeComboModel> comboBox = this.manager.getComboBox(niveau, type);

                    comboBox.addItemListener(new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent e) {
                            EleveAptitudeAssociationPanel.this.refreshScore();
                        }
                    });

                    Aptitude aptitude = this.eleve.getAptitude(niveau, type);
                    if (aptitude != null) {
                        comboBox.setSelectedIndex(aptitude.getScore());
                    } else {
                        comboBox.setSelectedIndex(0);
                    }

                    comboBox.repaint();
                }
            }

            this.refreshScore();

        } catch (Exception e) {
            LOGGER.error("onChange(" + newEleve + ") failed", e);
        }
    }

    public void refreshScore() {
        try {
            List<Niveau> niveaux = NiveauService.getAll();
            List<TypeAptitude> types = TypeAptitudeService.getAll();

            for (Niveau niveau : niveaux) {
                int score = 0;
                for (TypeAptitude type : types) {
                    JComboBox<AptitudeComboModel> comboBox = this.manager.getComboBox(niveau, type);
                    Aptitude aptitude = ((AptitudeComboModel) comboBox.getSelectedItem()).getAptitude();
                    if (aptitude != null) {
                        score += aptitude.getScore();
                    }
                }
                this.map.get(niveau).setText(Integer.toString(score));
            }
        } catch (Exception e) {
            LOGGER.error("refreshScore() failed", e);
        }
    }

    public void updateEleve(Eleve eleve) {
        try {
            List<Niveau> niveaux = NiveauService.getAll();
            List<TypeAptitude> types = TypeAptitudeService.getAll();

            AptitudeService.removeAll(eleve);

            for (Niveau niveau : niveaux) {
                for (TypeAptitude type : types) {
                    JComboBox<AptitudeComboModel> comboBox = this.manager.getComboBox(niveau, type);
                    Aptitude aptitude = ((AptitudeComboModel) comboBox.getSelectedItem()).getAptitude();
                    if (aptitude != null) {
                        AptitudeService.add(aptitude, eleve);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("updateEleve(" + eleve + ") failed", e);
        }
    }

}
