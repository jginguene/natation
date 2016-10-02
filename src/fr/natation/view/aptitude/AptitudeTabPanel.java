package fr.natation.view.aptitude;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import fr.natation.view.IRefreshListener;

public class AptitudeTabPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;
    private final AptitudeListPanel listPanel = new AptitudeListPanel();
    private final AptitudeAddPanel addPanel = new AptitudeAddPanel();

    public AptitudeTabPanel() throws Exception {
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
