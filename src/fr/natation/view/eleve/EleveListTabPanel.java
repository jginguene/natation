package fr.natation.view.eleve;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import fr.natation.view.IRefreshListener;
import fr.natation.view.groupe.GroupePanel;

public class EleveListTabPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;
    private final EleveListPanel listPanel = new EleveListPanel();
    private final EleveAddPanel addPanel = new EleveAddPanel();

    public EleveListTabPanel() throws Exception {
        this.setLayout(new BorderLayout());
        this.add(new GroupePanel(), BorderLayout.PAGE_START);
        this.add(this.listPanel, BorderLayout.CENTER);

        this.listPanel.setEleveAddPanel(this.addPanel);
        this.addPanel.addListener(this.listPanel);
    }

    public void addListener(IEleveSelectListener listener) {
        this.listPanel.addListener(listener);
    }

    @Override
    public void refresh() throws Exception {
        this.listPanel.refresh();
        this.addPanel.refresh();
    }

}
