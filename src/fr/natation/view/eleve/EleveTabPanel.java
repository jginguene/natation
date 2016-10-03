package fr.natation.view.eleve;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import fr.natation.view.IRefreshListener;

public class EleveTabPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;
    private final ElevePanel panel = new ElevePanel();

    public EleveTabPanel() throws Exception {
        this.setLayout(new BorderLayout());
        this.add(this.panel, BorderLayout.CENTER);
    }

    @Override
    public void refresh() throws Exception {
    }

}
