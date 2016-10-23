package fr.natation.view.competence;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.natation.view.IRefreshListener;

public class CompetenceListTabPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;
    private final CompetenceListPanel2 listPanel = new CompetenceListPanel2();

    public CompetenceListTabPanel() throws Exception {
        this.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(this.listPanel);
        this.add(scrollPane, BorderLayout.CENTER);

    }

    @Override
    public void refresh() throws Exception {
    }

}
