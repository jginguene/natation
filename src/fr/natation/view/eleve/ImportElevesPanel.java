package fr.natation.view.eleve;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import fr.natation.model.Classe;
import fr.natation.model.ElevesImporter;
import fr.natation.service.ClasseService;
import fr.natation.view.GridBagConstraintsFactory;
import fr.natation.view.IRefreshListener;
import fr.natation.view.Icon;
import fr.natation.view.NatationFrame;

public class ImportElevesPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(ImportElevesPanel.class.getName());

    private final ElevesImporter importer = new ElevesImporter();
    private JDialog dialog;
    private final JTextField textFieldFile;
    private final JComboBox<Classe> comboClasse;

    private final IRefreshListener listener;

    public ImportElevesPanel(IRefreshListener listener) throws Exception {
        this.listener = listener;

        this.setLayout(new GridBagLayout());

        JLabel labelClasse = new JLabel("Classe");
        this.add(labelClasse, GridBagConstraintsFactory.create(1, 1, 3, 1));

        labelClasse.setPreferredSize(new Dimension(50, 20));
        labelClasse.setMinimumSize(new Dimension(50, 20));
        labelClasse.setSize(new Dimension(50, 20));

        this.comboClasse = new JComboBox<Classe>();
        for (Classe classe : ClasseService.getAll()) {
            this.comboClasse.addItem(classe);
        }
        this.comboClasse.setSelectedIndex(0);
        this.add(this.comboClasse, GridBagConstraintsFactory.create(2, 1, 1, 1));
        this.comboClasse.setPreferredSize(new Dimension(300, 20));
        this.comboClasse.setMinimumSize(new Dimension(300, 20));
        this.comboClasse.setSize(new Dimension(300, 20));

        JButton fileChooser = new JButton(Icon.Excel.getImage());

        JLabel labelFile = new JLabel("Fichier");
        labelFile.setPreferredSize(new Dimension(50, 20));
        labelFile.setMinimumSize(new Dimension(50, 20));
        labelFile.setSize(new Dimension(50, 20));

        this.add(labelFile, GridBagConstraintsFactory.create(1, 2, 1, 1));
        this.textFieldFile = new JTextField();

        this.textFieldFile.setPreferredSize(new Dimension(300, 20));
        this.textFieldFile.setMinimumSize(new Dimension(300, 20));
        this.textFieldFile.setSize(new Dimension(300, 20));

        this.add(this.textFieldFile, GridBagConstraintsFactory.create(2, 2, 1, 1));
        this.add(fileChooser, GridBagConstraintsFactory.create(3, 2, 1, 1));

        fileChooser.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fileChooser = new JFileChooser("./");
                fileChooser.setApproveButtonText("Charger");

                FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier base élève", "csv");
                fileChooser.setFileFilter(filter);

                // In response to a button click:
                int returnVal = fileChooser.showOpenDialog(NatationFrame.FRAME);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    ImportElevesPanel.this.textFieldFile.setText(file.getPath());
                }

            }
        });

        JButton addButton = new JButton("Importer les élèves du fichier", Icon.Excel.getImage());
        this.add(addButton, GridBagConstraintsFactory.create(1, 3, 3, 1));

        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ImportElevesPanel.this.onAddButton();
            }
        });
    }

    private void onAddButton() {
        try {
            int nbEleve = this.importer.importEleves(this.textFieldFile.getText(), (Classe) this.comboClasse.getSelectedItem());
            this.listener.refresh();

            JOptionPane.showMessageDialog(null, "L'import est terminé: " + nbEleve + " élèves ont été ajouté", "Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "L'import a echoué: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            LOGGER.error(" onAddButton() failed", e);

        }

        if (this.dialog != null) {
            this.dialog.dispose();
        }

    }

    public void setDialog(JDialog dialog) {
        this.dialog = dialog;
    }

}
