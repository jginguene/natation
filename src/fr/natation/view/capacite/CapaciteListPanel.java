package fr.natation.view.capacite;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Logger;

import fr.natation.pdf.PdfUtils;
import fr.natation.service.CapaciteService;
import fr.natation.view.ButtonFactory;
import fr.natation.view.ListPanel;
import fr.natation.view.eleve.EleveListPanel;

public class CapaciteListPanel extends ListPanel implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    public static int ROW_HEIGHT = 85;

    private final static Logger LOGGER = Logger.getLogger(EleveListPanel.class.getName());

    private JButton exportButton = ButtonFactory.createPdfButton("Exporter");
    private Component componentToExport;

    public CapaciteListPanel() throws Exception {
        super("Liste des capacités");
    }

    @Override
    protected void init(String title) throws Exception {
        this.setBorder(BorderFactory.createTitledBorder(title));
        this.setLayout(new BorderLayout());
        this.refresh();
        this.add(new JScrollPane(this.table));
        JScrollPane pane = new JScrollPane(this.table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.table.setPreferredScrollableViewportSize(new Dimension(600, 400));
        this.add(pane, BorderLayout.CENTER);
        this.componentToExport = this.table;
        this.table.setBorder(BorderFactory.createLineBorder(Color.black));

        this.exportButton = ButtonFactory.createExcelButton("Exporter");
        this.add(this.exportButton, BorderLayout.SOUTH);

        this.exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CapaciteListPanel.this.onExportButton();
            }
        });

    }

    private void onExportButton() {
        PdfUtils.save("capacite.pdf", this.componentToExport, 60, 80, 0.70f);
    }

    @Override
    public void refresh() throws Exception {
        CapaciteTableModel model = new CapaciteTableModel(CapaciteService.getAll());
        this.table.setModel(model);
        this.hideColumn(CapaciteTableModel.COLUMN_ID);
        this.setColumnWidth(CapaciteTableModel.COLUMN_LOGO, ROW_HEIGHT);
        this.setColumnWidth(CapaciteTableModel.COLUMN_NOM, 100);
        this.table.setRowHeight(ROW_HEIGHT);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        return null;
    }
}
