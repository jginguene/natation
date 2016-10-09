package fr.natation.view.eleve;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.natation.model.Eleve;
import fr.natation.model.Niveau;
import fr.natation.model.TypeAptitude;
import fr.natation.service.NiveauService;
import fr.natation.service.TypeAptitudeService;
import fr.natation.view.AptitudeSelectionManager;

public class EleveAptitudeAssociationPanel extends JPanel implements IEleveSelectListener {

    private static final long serialVersionUID = 1L;

    private Eleve eleve;
    private final GridLayout layout;

    AptitudeSelectionManager manager = new AptitudeSelectionManager();

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
                this.add(this.manager.getComboBox(niveau, type));
            }
        }

        JLabel total = new JLabel("Total");
        total.setFont(total.getFont().deriveFont(Font.BOLD));
        this.add(new JLabel("Total"));

        for (Niveau niveau : niveaux) {
            this.add(new JLabel("0"));
        }

    }

    @Override
    public void onChange(Eleve newEleve) {
        this.eleve = newEleve;

    }

}
