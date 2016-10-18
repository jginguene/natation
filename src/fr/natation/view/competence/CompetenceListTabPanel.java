package fr.natation.view.competence;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import fr.natation.view.IRefreshListener;

public class CompetenceListTabPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;
    private final CompetenceListPanel listPanel = new CompetenceListPanel();
    private final CompetenceAddPanel addPanel = new CompetenceAddPanel();

    public CompetenceListTabPanel() throws Exception {
        this.setLayout(new BorderLayout());
        this.add(this.listPanel, BorderLayout.CENTER);
        this.listPanel.setCompetenceAddPanel(this.addPanel);
        this.addPanel.addListener(this.listPanel);
    }

    @Override
    public void refresh() throws Exception {
        this.listPanel.refresh();
        this.addPanel.refresh();
    }

}