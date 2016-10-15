package fr.natation.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;

import fr.natation.model.Competence;
import fr.natation.model.Domaine;
import fr.natation.model.Niveau;
import fr.natation.service.CompetenceService;

public class CompetenceSelectionManager {

    private final Map<String, JComboBox<CompetenceComboModel>> map = new HashMap<String, JComboBox<CompetenceComboModel>>();

    public JComboBox<CompetenceComboModel> getComboBox(Niveau niveau, Domaine domaine) throws Exception {
        String key = this.getKey(niveau, domaine);

        if (this.map.containsKey(key)) {
            return this.map.get(key);
        } else {
            JComboBox<CompetenceComboModel> comboBox = new JComboBox<CompetenceComboModel>();
            List<CompetenceComboModel> list = new ArrayList<CompetenceComboModel>();
            list.add(new EmptyCompetenceComboModel());
            for (Competence competence : CompetenceService.get(niveau, domaine)) {
                list.add(new CompetenceComboModel(competence));
            }

            CustomComboBoxModel<CompetenceComboModel> model = new CustomComboBoxModel<CompetenceComboModel>(list);
            comboBox.setModel(model);
            this.map.put(key, comboBox);
            return comboBox;
        }

    }

    private String getKey(Niveau niveau, Domaine domaine) {
        return domaine.getNom() + "##" + niveau.getId();
    }

}
