package fr.natation.view;

import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class CustomComboBoxModel<E extends Object> extends AbstractListModel<E> implements ComboBoxModel<E> {

    private static final long serialVersionUID = 1L;

    private E selectedItem;
    private final List<E> list;

    public CustomComboBoxModel(List<E> list) {
        this.list = list;
    }

    @Override
    public E getSelectedItem() {
        return this.selectedItem;
    }

    @Override
    public int getSize() {
        return this.list.size();
    }

    @Override
    public E getElementAt(int i) {
        return this.list.get(i);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setSelectedItem(Object anItem) {
        this.selectedItem = (E) anItem;
    }

}
