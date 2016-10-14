package fr.natation.view.aptitude;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import fr.natation.view.IRefreshListener;

public class AptitudeListTabPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;
    private final AptitudeListPanel listPanel = new AptitudeListPanel();
    private final AptitudeAddPanel addPanel = new AptitudeAddPanel();

    public AptitudeListTabPanel() throws Exception {
        this.setLayout(new BorderLayout());
        this.add(this.listPanel, BorderLayout.CENTER);
        this.listPanel.setAptitudeAddPanel(this.addPanel);
        this.addPanel.addListener(this.listPanel);
    }

    @Override
    public void refresh() throws Exception {
        this.listPanel.refresh();
        this.addPanel.refresh();
    }

}
