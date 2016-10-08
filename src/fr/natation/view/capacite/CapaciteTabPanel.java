package fr.natation.view.capacite;

import java.awt.GridLayout;

import javax.swing.JPanel;

import fr.natation.view.IRefreshListener;

public class CapaciteTabPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;
    private final CapaciteListPanel listPanel = new CapaciteListPanel();

    public CapaciteTabPanel() throws Exception {
        this.setLayout(new GridLayout(1, 1));
        this.add(this.listPanel);
    }

    @Override
    public void refresh() throws Exception {
        this.listPanel.refresh();
    }
}
