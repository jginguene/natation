package fr.natation.view.aptitude;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.apache.log4j.Logger;

import fr.natation.service.AptitudeService;
import fr.natation.view.ButtonColumn;
import fr.natation.view.ButtonFactory;
import fr.natation.view.Icon;
import fr.natation.view.ListPanel;
import fr.natation.view.NatationFrame;

public class AptitudeListPanel extends ListPanel {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(AptitudeListPanel.class.getName());

    public static int ROW_HEIGHT = 25;

    private JButton addAptitudeButton;

    private AptitudeAddPanel addPanel;

    private JDialog dialog;

    public AptitudeListPanel() throws Exception {
        super("Liste des aptitudes");
    }

    @Override
    protected void init(String title) throws Exception {
        super.init(title);

        JPanel panelButton = new JPanel();
        panelButton.setLayout(new GridLayout(1, 5));
        this.addAptitudeButton = ButtonFactory.createAddButton("Ajouter une aptitude");
        panelButton.add(this.addAptitudeButton);
        panelButton.add(new JLabel());
        panelButton.add(new JLabel());
        panelButton.add(new JLabel());

        this.add(panelButton, BorderLayout.SOUTH);

        this.addAptitudeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                AptitudeListPanel.this.onAddAptitudeButton();
            }
        });
    }

    @Override
    public void refresh() throws Exception {

        AptitudeTableModel model = new AptitudeTableModel(AptitudeService.getAll());
        this.table.setModel(model);

        Action delete = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();
                table.getSelectedRow();

                int modelRow = Integer.valueOf(e.getActionCommand());
                AptitudeTableModel model = (AptitudeTableModel) table.getModel();
                Integer id = (Integer) (model).getValueAt(modelRow, AptitudeTableModel.COLUMN_ID);

                int answer = JOptionPane.showConfirmDialog(
                        null,
                        "Voulez vous supprimer l'aptitude "
                                + model.getValueAt(modelRow, AptitudeTableModel.COLUMN_DESC)
                                + " de l'application?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (answer == JOptionPane.YES_OPTION) {
                    try {
                        AptitudeService.delete(id.intValue());
                        AptitudeListPanel.this.refresh();

                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "La suppression a échouée", "Erreur", JOptionPane.ERROR_MESSAGE);
                        LOGGER.error("La suppression a échouée", e1);
                    }
                }
            }
        };

        new ButtonColumn(this.table, delete, AptitudeTableModel.COLUMN_ACTION, fr.natation.view.Icon.Delete.getImage(), null);

        this.setColumnWidth(AptitudeTableModel.COLUMN_SCORE, 50);
        this.setColumnWidth(AptitudeTableModel.COLUMN_NIVEAU, 100);
        this.setColumnWidth(AptitudeTableModel.COLUMN_TYPE, 100);
        this.setColumnWidth(AptitudeTableModel.COLUMN_CAPACITE, 80);
        this.setColumnWidth(AptitudeTableModel.COLUMN_ACTION, 60);

        this.table.setRowHeight(AptitudeTableModel.ROW_HEIGHT);

        this.hideColumn(AptitudeTableModel.COLUMN_ID);

    }

    private void onAddAptitudeButton() {
        this.getDialog().setVisible(true);
    }

    public void setAptitudeAddPanel(AptitudeAddPanel addPanel) {
        this.addPanel = addPanel;
    }

    private JDialog getDialog() {
        if (this.dialog == null) {
            this.dialog = new JDialog(NatationFrame.FRAME, "", Dialog.ModalityType.DOCUMENT_MODAL);
            this.dialog.setIconImage(Icon.Add.getImage().getImage());
            this.addPanel.setDialog(this.dialog);
            this.dialog.add(this.addPanel);
            this.dialog.pack();
            this.dialog.setResizable(false);
            this.dialog.setLocationRelativeTo(NatationFrame.FRAME);
            this.dialog.setTitle("Ajouter un groupe");
        }
        return this.dialog;
    }

}
