package fr.natation.view.eleve;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import fr.natation.model.Eleve;
import fr.natation.view.IEleveUpdateListener;
import fr.natation.view.IRefreshListener;

public class EleveTabPanel extends JPanel implements IRefreshListener, IEleveSelectListener {

    private static final long serialVersionUID = 1L;

    private final ElevePanel panel = new ElevePanel();

    public EleveTabPanel() throws Exception {
        this.setLayout(new BorderLayout());

        this.add(this.panel, BorderLayout.CENTER);
    }

    @Override
    public void refresh() throws Exception {
    }

    @Override
    public void onChange(Eleve newEleve, Object source) {
        this.panel.onChange(newEleve, source);
    }

    public void addListener(IEleveUpdateListener listener) {
        this.panel.addListener(listener);
    }

}
