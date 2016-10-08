package fr.natation.view;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public abstract class ListPanel extends JPanel implements IRefreshListener {

    private static final long serialVersionUID = 1L;

    protected final JTable table = new JTable();

    public ListPanel(String title) throws Exception {
        this.init(title);
        this.table.setDefaultRenderer(String.class, new MultiLineCellRenderer());
    }

    protected void init(String title) throws Exception {
        this.setBorder(BorderFactory.createTitledBorder(title));
        this.refresh();
        this.add(new JScrollPane(this.table));
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

}
