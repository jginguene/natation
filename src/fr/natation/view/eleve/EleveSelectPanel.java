package fr.natation.view.eleve;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.natation.model.Eleve;
import fr.natation.service.EleveService;
import fr.natation.view.CustomComboBoxModel;
import fr.natation.view.IRefreshListener;

public class EleveSelectPanel extends JPanel implements IRefreshListener, IEleveSelectListener {

    private static final long serialVersionUID = 1L;

    private final JComboBox<Eleve> inputEleve = new JComboBox<Eleve>();

    private final List<IEleveSelectListener> listeners = new ArrayList<IEleveSelectListener>();

    public EleveSelectPanel() throws Exception {

        this.setLayout(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.fill = GridBagConstraints.HORIZONTAL;

        constraint.gridy = 0;
        constraint.gridx = 0;
        constraint.gridwidth = 1;
        this.add(new JLabel("Séléctionner un élève            "), constraint);

        constraint.gridx = 1;
        this.add(this.inputEleve, constraint);

        this.setVisible(true);
        this.refresh();

        this.inputEleve.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    EleveSelectPanel.this.onSelectEleve((Eleve) event.getItem());
                }
            }
        });
    }

    private void onSelectEleve(Eleve eleve) {
        for (IEleveSelectListener listener : this.listeners) {
            listener.onChange(eleve, this);
        }
    }

    @Override
    public void refresh() throws Exception {
        CustomComboBoxModel<Eleve> modelEleve = new CustomComboBoxModel<Eleve>(EleveService.getAll());
        this.inputEleve.setModel(modelEleve);
        this.inputEleve.setSelectedIndex(0);
    }

    public void addListener(IEleveSelectListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void onChange(Eleve newEleve, Object source) {
        if (source != this) {
            this.inputEleve.setSelectedItem(newEleve);
            this.onSelectEleve(newEleve);
        }
        this.repaint();

    }

}
