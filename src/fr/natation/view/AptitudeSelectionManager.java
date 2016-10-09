package fr.natation.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;

import fr.natation.model.Aptitude;
import fr.natation.model.Niveau;
import fr.natation.model.TypeAptitude;
import fr.natation.service.AptitudeService;

public class AptitudeSelectionManager {

    private final Map<String, JComboBox<AptitudeComboModel>> map = new HashMap<String, JComboBox<AptitudeComboModel>>();

    public JComboBox<AptitudeComboModel> getComboBox(Niveau niveau, TypeAptitude type) throws Exception {
        String key = this.getKey(niveau, type);

        if (this.map.containsKey(key)) {
            return this.map.get(key);
        } else {
            JComboBox<AptitudeComboModel> comboBox = new JComboBox<AptitudeComboModel>();
            List<AptitudeComboModel> list = new ArrayList<AptitudeComboModel>();
            for (Aptitude aptitude : AptitudeService.get(niveau, type)) {
                list.add(new AptitudeComboModel(aptitude));
            }

            CustomComboBoxModel<AptitudeComboModel> model = new CustomComboBoxModel<AptitudeComboModel>(list);
            comboBox.setModel(model);
            this.map.put(key, comboBox);

            return comboBox;

        }

    }

    private String getKey(Niveau niveau, TypeAptitude type) {
        return type.getNom() + "##" + niveau.getId();
    }

}
