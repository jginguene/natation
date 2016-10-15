package fr.natation.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;

import fr.natation.model.Competence;
import fr.natation.model.Niveau;
import fr.natation.model.Domaine;
import fr.natation.service.CompetenceService;

public class AptitudeSelectionManager {

    private final Map<String, JComboBox<CompetenceComboModel>> map = new HashMap<String, JComboBox<CompetenceComboModel>>();

    public JComboBox<CompetenceComboModel> getComboBox(Niveau niveau, Domaine type) throws Exception {
        String key = this.getKey(niveau, type);

        if (this.map.containsKey(key)) {
            return this.map.get(key);
        } else {
            JComboBox<CompetenceComboModel> comboBox = new JComboBox<CompetenceComboModel>();
            List<CompetenceComboModel> list = new ArrayList<CompetenceComboModel>();
            list.add(new EmptyAptitudeComboModel());
            for (Competence aptitude : CompetenceService.get(niveau, type)) {
                list.add(new CompetenceComboModel(aptitude));
            }

            CustomComboBoxModel<CompetenceComboModel> model = new CustomComboBoxModel<CompetenceComboModel>(list);
            comboBox.setModel(model);
            this.map.put(key, comboBox);
            return comboBox;
        }

    }

    private String getKey(Niveau niveau, Domaine type) {
        return type.getNom() + "##" + niveau.getId();
    }

}
