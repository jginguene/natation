package fr.natation.view.capacite;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import fr.natation.view.IRefreshListener;

public class CapaciteTabPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;
    private final CapaciteListPanel listPanel = new CapaciteListPanel();

    public CapaciteTabPanel() throws Exception {

        this.setLayout(new BorderLayout());
        this.add(this.listPanel, BorderLayout.CENTER);

        
    }

    @Override
    public void refresh() throws Exception {
        this.listPanel.refresh();
    }
}
