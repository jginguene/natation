package fr.natation.view.eleve;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import fr.natation.view.IRefreshListener;

public class ElevePanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(EleveAddPanel.class.getName());

    private final EleveSelectPanel selectPanel = new EleveSelectPanel();
    private final EleveInfoEditPanel editPanel = new EleveInfoEditPanel();

    public ElevePanel() throws Exception {
        this.setLayout(new BorderLayout());

        this.selectPanel.addListener(this.editPanel);

        this.add(this.selectPanel, BorderLayout.PAGE_START);
        this.add(this.editPanel, BorderLayout.CENTER);

        this.refresh();

    }

    @Override
    public void refresh() throws Exception {
        this.selectPanel.refresh();
        this.editPanel.refresh();
    }

}
