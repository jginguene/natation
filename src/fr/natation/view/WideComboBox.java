package fr.natation.view;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

public class WideComboBox<E> extends JComboBox<E> {

    private static final long serialVersionUID = 1L;

    public WideComboBox() {
    }

    public WideComboBox(final E items[]) {
        super(items);
    }

    public WideComboBox(Vector<E> items) {
        super(items);
    }

    public WideComboBox(ComboBoxModel<E> aModel) {
        super(aModel);
    }

    private boolean layingOut = false;

    @Override
    public void doLayout() {
        try {
            this.layingOut = true;
            super.doLayout();
        } finally {
            this.layingOut = false;
        }
    }

    @Override
    public Dimension getSize() {
        Dimension dim = super.getSize();
        if (!this.layingOut)
            dim.width = Math.max(dim.width, this.getPreferredSize().width);
        return dim;
    }
}