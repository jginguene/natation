package fr.natation.view;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import fr.natation.DiskTools;
import fr.natation.model.Eleve;
import fr.natation.view.capacite.CapaciteTabPanel;
import fr.natation.view.chart.AnalyseTabPanel;
import fr.natation.view.competence.CompetenceListTabPanel;
import fr.natation.view.competence.SelectionEnLotDeCompetencePanel;
import fr.natation.view.eleve.EcoleInfoEditPanel;
import fr.natation.view.eleve.EleveListTabPanel;
import fr.natation.view.eleve.EleveTabPanel;
import fr.natation.view.eleve.IEleveSelectListener;
import fr.natation.view.groupe.GroupeListTabPanel;
import fr.natation.view.navette.NavettePanel;

public class NatationFrame extends JFrame implements IEleveSelectListener, INatationMenuListener {

    public static NatationFrame FRAME;

    private final static Logger LOGGER = Logger.getLogger(NatationFrame.class.getName());

    private static final long serialVersionUID = 1L;

    private static final int ECOLE_TAB = 0;
    private static final int ELEVE_LIST_TAB = 1;
    private static final int ELEVE_TAB = 2;
    private static final int GROUPE_TAB = 4;
    private static final int COMPETENCE_TAB = 5;
    private static final int ASSIGNATION_TAB = 3;
    private static final int CAPACITE_TAB = 6;
    private static final int NAVETTE_TAB = 7;
    private static final int ANALYSE_TAB = 8;

    private final EleveListTabPanel eleveListTabPanel;

    private final EleveTabPanel eleveTabPanel;
    private final GroupeListTabPanel groupeListTabPanel;
    private final CompetenceListTabPanel competenceListTabPanel;
    private final CapaciteTabPanel capaciteListTabPanel;
    private final SelectionEnLotDeCompetencePanel selectionEnLotDeCompetencePanel;
    private final NavettePanel navettePanel;
    private final AnalyseTabPanel analyseTabPanel;
    private final EcoleInfoEditPanel ecolePanel;

    private final NatationMenu menu;

    private final JTabbedPane tabbedPane = new JTabbedPane();

    private boolean isInitialized = false;

    public NatationFrame() throws Exception {

        super("Passeport pour le natation");

        this.eleveListTabPanel = new EleveListTabPanel();
        this.eleveTabPanel = new EleveTabPanel();
        this.groupeListTabPanel = new GroupeListTabPanel();
        this.competenceListTabPanel = new CompetenceListTabPanel();
        this.capaciteListTabPanel = new CapaciteTabPanel();
        this.selectionEnLotDeCompetencePanel = new SelectionEnLotDeCompetencePanel();
        this.navettePanel = new NavettePanel();
        this.analyseTabPanel = new AnalyseTabPanel();
        this.ecolePanel = new EcoleInfoEditPanel();

        this.eleveTabPanel.addListener(this.analyseTabPanel);

        FRAME = this;

        this.setIconImage(Icon.Application.getImage().getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.menu = new NatationMenu(this);
        this.setJMenuBar(this.menu);

        this.addTabs();

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

                    if (NatationFrame.this.tabbedPane.getSelectedIndex() == COMPETENCE_TAB) {
                        NatationFrame.this.competenceListTabPanel.refresh();
                    }

                    if (NatationFrame.this.tabbedPane.getSelectedIndex() == ANALYSE_TAB) {
                        NatationFrame.this.analyseTabPanel.refresh();
                    }

                    if (NatationFrame.this.tabbedPane.getSelectedIndex() == NAVETTE_TAB) {
                        NatationFrame.this.navettePanel.refresh();
                    }

                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "Le rafraichissement de l'onglet a échoué");
                    NatationFrame.LOGGER.error("Le rafraichissement de l'onglet a échoué", e1);
                }
            }
        });

        this.add(this.tabbedPane);

        this.pack();
        this.setResizable(true);
        this.setSize(800, 800);

        this.isInitialized = true;
    }

    private void addTabs() {
        for (int i = 0; i < 20; i++) {

            if (i == ELEVE_LIST_TAB) {
                this.tabbedPane.addTab("Liste des élèves", Icon.Liste.getImage(), this.eleveListTabPanel, "");
            }

            if (i == ELEVE_TAB) {
                this.tabbedPane.addTab("Fiches des élèves", Icon.Eleve.getImage(), this.eleveTabPanel, "");
            }

            if (i == GROUPE_TAB) {
                this.tabbedPane.addTab("Liste des groupes", Icon.Liste.getImage(), this.groupeListTabPanel, "");
            }

            if (i == COMPETENCE_TAB) {
                this.tabbedPane.addTab("Les compétences", Icon.Competence.getImage(), this.competenceListTabPanel, "");
            }

            if (i == ASSIGNATION_TAB) {
                this.tabbedPane.addTab("Saisie collective des compétences", Icon.Competence.getImage(), this.selectionEnLotDeCompetencePanel, "");
            }

            if (i == CAPACITE_TAB) {
                this.tabbedPane.addTab("Les capacités", Icon.Capacite.getImage(), this.capaciteListTabPanel, "");
            }

            if (i == NAVETTE_TAB) {
                this.tabbedPane.addTab("Navette", Icon.Navette.getImage(), this.navettePanel, "");
            }

            if (i == ANALYSE_TAB) {
                this.tabbedPane.addTab("Analyse", Icon.Analyse.getImage(), this.analyseTabPanel, "");
            }

            if (i == ECOLE_TAB) {
                this.tabbedPane.addTab("Ecole", Icon.Ecole.getImage(), this.ecolePanel, "");
            }

        }
    }

    public boolean isInitialized() {
        return this.isInitialized;
    }

    @Override
    public void onChange(Eleve newEleve, Object source) {
        this.tabbedPane.setSelectedIndex(ELEVE_TAB);
        this.eleveTabPanel.onChange(newEleve, this);

    }

    @Override
    public void onSave() {
        try {
            final JFileChooser fileChooser = new JFileChooser("./");
            fileChooser.setApproveButtonText("Sauvegarder");

            FileNameExtensionFilter filter = new FileNameExtensionFilter("Base de données", "db");
            fileChooser.setFileFilter(filter);

            // In response to a button click:
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {

                File file = fileChooser.getSelectedFile();
                if (file.exists()) {
                    JOptionPane.showMessageDialog(null, "Le fichier " + file.getCanonicalPath() + " existe déja", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

                DiskTools.threadCopy(Paths.get("natation.db"), Paths.get(file.getCanonicalPath()));

                file.setLastModified(new Date().getTime());
                LOGGER.debug("Create backup " + file.getCanonicalPath());

                JOptionPane.showMessageDialog(null, "La base a été sauvegardée dans le fichier " + file.getCanonicalPath(), "Information", JOptionPane.INFORMATION_MESSAGE);

            }
        } catch (Exception e) {
            LOGGER.error("La sauvegarde a échoué", e);
            JOptionPane.showMessageDialog(null, "La sauvegarde a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void onLoad() {
        try {
            final JFileChooser fileChooser = new JFileChooser("./");
            fileChooser.setApproveButtonText("Charger");

            FileNameExtensionFilter filter = new FileNameExtensionFilter("Base de données", "db");
            fileChooser.setFileFilter(filter);

            // In response to a button click:
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                new File("backup/load_natation.db").delete();

                Files.copy(Paths.get("natation.db"), Paths.get("backup/load_natation.db"));

                new File("natation.db").delete();
                Files.copy(Paths.get(file.getCanonicalPath()), Paths.get("natation.db"));
                LOGGER.debug("Load backup " + file.getCanonicalPath());
                JOptionPane.showMessageDialog(null, "La base a été chargée depuis le fichier " + file.getCanonicalPath(), "Information", JOptionPane.INFORMATION_MESSAGE);

            }
        } catch (Exception e) {
            LOGGER.error("La sauvegarde a échoué", e);
            JOptionPane.showMessageDialog(null, "Le chargement a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void onExit() {
        System.exit(0);
    }
}
