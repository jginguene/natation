package fr.natation.view.eleve;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import fr.natation.model.Eleve;
import fr.natation.view.IRefreshListener;

public class EleveTabPanel extends JPanel implements IRefreshListener, IEleveSelectListener {

    private static final long serialVersionUID = 1L;
    private final ElevePanel panel = new ElevePanel();

    public EleveTabPanel() throws Exception {
        this.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        this.add(this.panel, constraints);
    }

    @Override
    public void refresh() throws Exception {
    }

    @Override
    public void onChange(Eleve newEleve, Object source) {
        this.panel.onChange(newEleve, source);
    }

}
