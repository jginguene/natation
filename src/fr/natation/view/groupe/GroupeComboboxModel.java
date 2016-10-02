package fr.natation.view.groupe;

import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import fr.natation.model.Groupe;

public class GroupeComboboxModel extends AbstractListModel<Groupe> implements ComboBoxModel<Groupe> {

    private static final long serialVersionUID = 1L;

    private Groupe selectedItem;
    private final List<Groupe> groupes;

    public GroupeComboboxModel(List<Groupe> groupes) {
        this.groupes = groupes;
    }

    @Override
    public Groupe getSelectedItem() {
        return this.selectedItem;
    }

    @Override
    public int getSize() {
        return this.groupes.size();
    }

    @Override
    public Groupe getElementAt(int i) {
        return this.groupes.get(i);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        this.selectedItem = (Groupe) anItem;

    }

}
