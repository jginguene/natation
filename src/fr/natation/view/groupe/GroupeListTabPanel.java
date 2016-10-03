package fr.natation.view.groupe;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import fr.natation.view.IRefreshListener;

public class GroupeListTabPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;
    private final GroupeListPanel listPanel = new GroupeListPanel();

    private final GroupeAddPanel addPanel = new GroupeAddPanel();

    public GroupeListTabPanel() throws Exception {

        this.setLayout(new BorderLayout());
        this.add(this.listPanel, BorderLayout.CENTER);
        this.add(this.addPanel, BorderLayout.PAGE_END);

        this.addPanel.addListener(this.listPanel);
    }

    @Override
    public void refresh() throws Exception {
        this.listPanel.refresh();
        this.addPanel.refresh();
    }
}
