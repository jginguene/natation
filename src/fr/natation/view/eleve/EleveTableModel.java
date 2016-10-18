package fr.natation.view.eleve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import fr.natation.model.Eleve;

public class EleveTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    public static final int COLUMN_SELECTED = 0;
    public static final int COLUMN_ID = 1;
    public static final int COLUMN_NOM = 2;
    public static final int COLUMN_PRENOM = 3;
    public static final int COLUMN_GROUPE = 4;
    public static final int COLUMN_CLASSE = 5;
    public static final int COLUMN_ACTION = 6;

    private final Map<Eleve, Boolean> selectionMap = new HashMap<>();
    private final Map<Integer, Eleve> eleveMap = new HashMap<>();

    private final static Logger LOGGER = Logger.getLogger(EleveTableModel.class.getName());
    private final List<Eleve> list;

    private final String[] columnNames = new String[] { "Selectionné", "Id", "Nom", "Prenom", "Groupe", "Classe", "Action" };

    @SuppressWarnings("rawtypes")
    private final Class[] columnClass = new Class[] { Boolean.class, Integer.class, String.class, String.class, String.class, String.class, String.class };

    public EleveTableModel(List<Eleve> list) {
        this.list = list;
        for (Eleve eleve : list) {
            this.selectionMap.put(eleve, Boolean.TRUE);
            this.eleveMap.put(eleve.getId(), eleve);
        }
    }

    @Override
    public String getColumnName(int column) {
        return this.columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return this.columnClass[columnIndex];
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public int getRowCount() {
        return this.list.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            Eleve eleve = this.list.get(rowIndex);
            switch (columnIndex) {
            case COLUMN_ID:
                return eleve.getId();

            case COLUMN_NOM:
                return eleve.getNom();

            case COLUMN_PRENOM:
                return eleve.getPrenom();

            case COLUMN_GROUPE:
                return eleve.getGroupeNom();

            case COLUMN_CLASSE:
                return eleve.getClasseNom();

            case COLUMN_SELECTED:
                return this.selectionMap.get(eleve);

            case COLUMN_ACTION:
                return "";

            default:
                throw new Exception("no value for column " + columnIndex);

            }
        } catch (Exception e) {
            throw new RuntimeException("getValueAt(" + rowIndex + ", " + columnIndex + ")", e);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == COLUMN_SELECTED) {
            Integer eleveId = (Integer) this.getValueAt(rowIndex, COLUMN_ID);
            Eleve eleve = this.eleveMap.get(eleveId);
            this.selectionMap.put(eleve, (Boolean) aValue);
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == COLUMN_ACTION || columnIndex == COLUMN_SELECTED;
    }

    public void selectAll() {
        for (Eleve eleve : this.selectionMap.keySet()) {
            this.selectionMap.put(eleve, true);
        }
    }

    public void unselectAll() {
        for (Eleve eleve : this.selectionMap.keySet()) {
            this.selectionMap.put(eleve, false);
        }
    }

    public List<Eleve> getSelection() {
        List<Eleve> selection = new ArrayList<>();
        for (Eleve eleve : this.selectionMap.keySet()) {
            if (this.selectionMap.get(eleve)) {
                selection.add(eleve);
            }
        }
        return selection;
    }
}
