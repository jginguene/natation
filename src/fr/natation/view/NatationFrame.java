package fr.natation.view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import fr.natation.Utils;
import fr.natation.view.aptitude.AptitudeListTabPanel;
import fr.natation.view.capacite.CapaciteTabPanel;
import fr.natation.view.eleve.EleveListTabPanel;
import fr.natation.view.eleve.EleveTabPanel;
import fr.natation.view.groupe.GroupeListTabPanel;

public class NatationFrame extends JFrame {

    private final static Logger LOGGER = Logger.getLogger(NatationFrame.class.getName());

    private static final long serialVersionUID = 1L;

    private final EleveListTabPanel eleveListTabPanel = new EleveListTabPanel();
    private final EleveTabPanel eleveTabPanel = new EleveTabPanel();
    private final GroupeListTabPanel groupeListTabPanel = new GroupeListTabPanel();
    private final AptitudeListTabPanel aptitudeListTabPanel = new AptitudeListTabPanel();
    private final CapaciteTabPanel capaciteListTabPanel = new CapaciteTabPanel();

    public NatationFrame() throws Exception {
        super("Natation");

        this.setIconImage(Utils.getExternalImage("img/app.png").getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Liste des élèves", null, this.eleveListTabPanel, "");
        tabbedPane.addTab("Fiche des élèves", null, this.eleveTabPanel, "");
        tabbedPane.addTab("Liste des groupes", null, this.groupeListTabPanel, "");
        tabbedPane.addTab("Liste des aptitudes", null, this.aptitudeListTabPanel, "");
        tabbedPane.addTab("Liste des capacités", null, this.capaciteListTabPanel, "");

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    if (tabbedPane.getSelectedIndex() == 0) {
                        NatationFrame.this.eleveListTabPanel.refresh();
                    }

                    if (tabbedPane.getSelectedIndex() == 2) {
                        NatationFrame.this.eleveTabPanel.refresh();
                    }

                    if (tabbedPane.getSelectedIndex() == 3) {
                        NatationFrame.this.groupeListTabPanel.refresh();
                    }

                    if (tabbedPane.getSelectedIndex() == 4) {
                        NatationFrame.this.aptitudeListTabPanel.refresh();
                    }

                    if (tabbedPane.getSelectedIndex() == 5) {
                        NatationFrame.this.capaciteListTabPanel.refresh();
                    }

                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "Le rafraichissement de l'onglet a échoué");
                    NatationFrame.LOGGER.error("Le rafraichissement de l'onglet a échoué", e1);
                }
            }
        });

        this.add(tabbedPane);

        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }
}
