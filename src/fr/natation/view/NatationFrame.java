package fr.natation.view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import fr.natation.view.eleve.EleveTabPanel;
import fr.natation.view.groupe.GroupeTabPanel;

public class NatationFrame extends JFrame {

    private final static Logger LOGGER = Logger.getLogger(NatationFrame.class.getName());

    private static final long serialVersionUID = 1L;

    private final EleveTabPanel eleveTabPanel = new EleveTabPanel();
    private final GroupeTabPanel groupeTabPanel = new GroupeTabPanel();

    public NatationFrame() throws Exception {
        super("Natation");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add the table to the frame

        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Liste des élèves", null, this.eleveTabPanel, "");
        // tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        tabbedPane.addTab("Liste des groupes", null, this.groupeTabPanel, "");
        // tabbedPane.setMnemonicAt(1, KeyEvent.VK_1);

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    if (tabbedPane.getSelectedIndex() == 0) {
                        NatationFrame.this.eleveTabPanel.refresh();
                    }

                    if (tabbedPane.getSelectedIndex() == 1) {
                        NatationFrame.this.groupeTabPanel.refresh();
                    }

                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "Le rafraichissement de l'onglet a échoué");
                    NatationFrame.LOGGER.error("Le rafraichissement de l'onglet a échoué", e1);
                }

                System.out.println("Tab: " + tabbedPane.getSelectedIndex());
                // Prints the string 3 times if there are 3 tabs etc
            }
        });

        this.add(tabbedPane);

        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }
}
