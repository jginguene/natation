package fr.natation.view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import fr.natation.Utils;
import fr.natation.model.Eleve;
import fr.natation.view.aptitude.AptitudeListTabPanel;
import fr.natation.view.capacite.CapaciteTabPanel;
import fr.natation.view.eleve.EleveListTabPanel;
import fr.natation.view.eleve.EleveTabPanel;
import fr.natation.view.eleve.IEleveSelectListener;
import fr.natation.view.groupe.GroupeListTabPanel;

public class NatationFrame extends JFrame implements IEleveSelectListener {

    private final static Logger LOGGER = Logger.getLogger(NatationFrame.class.getName());

    private static final long serialVersionUID = 1L;

    private static final int ELEVE_LIST_TAB = 0;
    private static final int ELEVE_TAB = 1;
    private static final int GROUPE_TAB = 2;
    private static final int APTITUDE_TAB = 3;
    private static final int CAPACITE_TAB = 4;

    private final EleveListTabPanel eleveListTabPanel = new EleveListTabPanel();
    private final EleveTabPanel eleveTabPanel = new EleveTabPanel();
    private final GroupeListTabPanel groupeListTabPanel = new GroupeListTabPanel();
    private final AptitudeListTabPanel aptitudeListTabPanel = new AptitudeListTabPanel();
    private final CapaciteTabPanel capaciteListTabPanel = new CapaciteTabPanel();

    private final JTabbedPane tabbedPane = new JTabbedPane();

    public NatationFrame() throws Exception {
        super("Natation");

        this.setIconImage(Utils.getExternalImage("img/app.png").getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.tabbedPane.addTab("Liste des élèves", Icon.Liste.getImage(), this.eleveListTabPanel, "");
        this.tabbedPane.addTab("Fiche des élèves", Icon.Eleve.getImage(), this.eleveTabPanel, "");
        this.tabbedPane.addTab("Liste des groupes", Icon.Liste.getImage(), this.groupeListTabPanel, "");
        this.tabbedPane.addTab("Liste des aptitudes", Icon.Aptitude.getImage(), this.aptitudeListTabPanel, "");
        this.tabbedPane.addTab("Liste des capacités", Icon.Capacite.getImage(), this.capaciteListTabPanel, "");

        this.eleveListTabPanel.addListener(this);

        this.tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    if (NatationFrame.this.tabbedPane.getSelectedIndex() == ELEVE_LIST_TAB) {
                        NatationFrame.this.eleveListTabPanel.refresh();
                    }

                    if (NatationFrame.this.tabbedPane.getSelectedIndex() == ELEVE_TAB) {
                        NatationFrame.this.eleveTabPanel.refresh();
                    }

                    if (NatationFrame.this.tabbedPane.getSelectedIndex() == GROUPE_TAB) {
                        NatationFrame.this.groupeListTabPanel.refresh();
                    }

                    if (NatationFrame.this.tabbedPane.getSelectedIndex() == APTITUDE_TAB) {
                        NatationFrame.this.aptitudeListTabPanel.refresh();
                    }

                    if (NatationFrame.this.tabbedPane.getSelectedIndex() == CAPACITE_TAB) {
                        NatationFrame.this.capaciteListTabPanel.refresh();
                    }

                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "Le rafraichissement de l'onglet a échoué");
                    NatationFrame.LOGGER.error("Le rafraichissement de l'onglet a échoué", e1);
                }
            }
        });

        this.add(this.tabbedPane);

        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public void onChange(Eleve newEleve, Object source) {
        this.tabbedPane.setSelectedIndex(ELEVE_TAB);
        this.eleveTabPanel.onChange(newEleve, this);

    }
}
