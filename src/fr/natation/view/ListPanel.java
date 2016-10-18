package fr.natation.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public abstract class ListPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;

    protected final JTable table = new JTable();

    protected boolean isSelectList = false;

    public ListPanel(String title) throws Exception {
        this(title, false);
    }

    public ListPanel(String title, boolean isSelectList) throws Exception {
        this.isSelectList = isSelectList;
        this.init(title);

        this.table.setDefaultRenderer(String.class, new MultiLineCellRenderer());
    }

    protected void init(String title) throws Exception {
        this.setBorder(BorderFactory.createTitledBorder(title));
        this.setLayout(new BorderLayout());
        this.refresh();
        this.add(new JScrollPane(this.table), BorderLayout.CENTER);
        JScrollPane pane = new JScrollPane(this.table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.table.setPreferredScrollableViewportSize(new Dimension(800, 300));

        this.add(pane);
    }

    protected void setColumnWidth(int column, int Width) {
        this.table.getColumnModel().getColumn(column).setWidth(Width);
        this.table.getColumnModel().getColumn(column).setMinWidth(Width);
        this.table.getColumnModel().getColumn(column).setMaxWidth(Width);
    }

    protected void hideColumn(int column) {
        this.table.getColumnModel().getColumn(column).setWidth(0);
        this.table.getColumnModel().getColumn(column).setMinWidth(0);
        this.table.getColumnModel().getColumn(column).setMaxWidth(0);
        this.table.getColumnModel().getColumn(column).setHeaderValue("");

    }

    protected void setRowHeight(int height) {
        this.table.setRowHeight(height);
    }

    public void exportTable(String fileName) throws IOException {

        File file = new File(fileName);
        TableModel model = this.table.getModel();
        FileWriter out = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(out);
        for (int i = 0; i < model.getColumnCount(); i++) {
            if (!"action".equalsIgnoreCase(model.getColumnName(i))) {
                bw.write(model.getColumnName(i) + ";");
            }
        }
        bw.write("\n");
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                if (!"action".equalsIgnoreCase(model.getColumnName(j))) {
                    bw.write(model.getValueAt(i, j).toString() + ";");
                }
            }
            bw.write("\n");
        }
        bw.close();

    }

}
