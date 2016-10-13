package fr.natation.view.groupe;

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
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.log4j.Logger;

import fr.natation.service.GroupeService;
import fr.natation.view.ButtonColumn;
import fr.natation.view.ButtonFactory;
import fr.natation.view.IVisibilityManager;
import fr.natation.view.Icon;
import fr.natation.view.ListPanel;
import fr.natation.view.NatationFrame;
import fr.natation.view.eleve.EleveListPanel;

public class GroupeListPanel extends ListPanel {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = Logger.getLogger(EleveListPanel.class.getName());

    private JButton addGroupeButton;

    private JDialog dialog;
    private GroupeAddPanel addPanel;

    public GroupeListPanel() throws Exception {
        super("Liste des groupes");

    }

    @Override
    protected void init(String title) throws Exception {
        super.init(title);

        JPanel panelButton = new JPanel();
        panelButton.setLayout(new GridLayout(1, 5));
        this.addGroupeButton = ButtonFactory.createAddButton("Ajouter un groupe");
        panelButton.add(this.addGroupeButton);
        panelButton.add(new JLabel());
        panelButton.add(new JLabel());
        panelButton.add(new JLabel());

        this.add(panelButton, BorderLayout.SOUTH);

        this.addGroupeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                GroupeListPanel.this.onAddGroupeButton();
            }
        });
    }

    @Override
    public void refresh() throws Exception {
        GroupeTableModel model = new GroupeTableModel(GroupeService.getAll());
        this.table.setModel(model);
        this.hideColumn(GroupeTableModel.COLUMN_ID);

        Action delete = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();
                table.getSelectedRow();

                int modelRow = Integer.valueOf(e.getActionCommand());
                GroupeTableModel model = (GroupeTableModel) table.getModel();
                Integer id = (Integer) (model).getValueAt(modelRow, GroupeTableModel.COLUMN_ID);

                int answer = JOptionPane.showConfirmDialog(
                        null,
                        "Voulez vous supprimer le groupe "
                                + model.getValueAt(modelRow, GroupeTableModel.COLUMN_NOM)
                                + " de l'application?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (answer == JOptionPane.YES_OPTION) {
                    try {
                        GroupeService.delete(id.intValue());
                        GroupeListPanel.this.refresh();

                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "La suppression a échouée", "Erreur", JOptionPane.ERROR_MESSAGE);
                        LOGGER.error("La suppression a échouée", e1);
                    }
                }
            }
        };

        new ButtonColumn(this.table, delete, GroupeTableModel.COLUMN_ACTION, Icon.Delete.getImage(), new IVisibilityManager() {
            @Override
            public boolean isVisible(int row) {
                GroupeTableModel model = (GroupeTableModel) GroupeListPanel.this.table.getModel();
                Integer nbEleve = (Integer) (model).getValueAt(row, GroupeTableModel.COLUMN_NB_ELEVE);
                return nbEleve == 0;
            }
        });

        this.setColumnWidth(GroupeTableModel.COLUMN_NOM, 60);
        this.setColumnWidth(GroupeTableModel.COLUMN_NB_ELEVE, 100);
        this.setColumnWidth(GroupeTableModel.COLUMN_ACTION, 60);
        this.setRowHeight(28);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setVerticalAlignment(JLabel.CENTER);
        this.table.getColumnModel().getColumn(GroupeTableModel.COLUMN_DESCRIPTION).setCellRenderer(renderer);

        renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        renderer.setVerticalAlignment(JLabel.CENTER);
        this.table.getColumnModel().getColumn(GroupeTableModel.COLUMN_ID).setCellRenderer(renderer);
        this.table.getColumnModel().getColumn(GroupeTableModel.COLUMN_NB_ELEVE).setCellRenderer(renderer);
        this.table.getColumnModel().getColumn(GroupeTableModel.COLUMN_NOM).setCellRenderer(renderer);

    }

    private void onAddGroupeButton() {
        this.getDialog().setVisible(true);
    }

    public void setGroupeAddPanel(GroupeAddPanel addPanel) {
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
