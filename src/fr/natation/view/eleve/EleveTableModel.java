package fr.natation.view.eleve;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import fr.natation.model.Eleve;
import fr.natation.service.EleveService;

public class EleveTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NOM = 1;
    public static final int COLUMN_PRENOM = 2;
    public static final int COLUMN_GROUPE = 3;
    public static final int COLUMN_CLASSE = 4;
    public static final int COLUMN_ACTION = 5;

    private final static Logger LOGGER = Logger.getLogger(EleveTableModel.class.getName());
    private final List<Eleve> list;

    private final String[] columnNames = new String[] { "Id", "Nom", "Prenom", "Groupe", "Classe", "Action" };

    @SuppressWarnings("rawtypes")
    private final Class[] columnClass = new Class[] { Integer.class, String.class, String.class, String.class, String.class, String.class };

    public EleveTableModel(List<Eleve> list) {
        this.list = list;

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

            default:
                return "";

            }
        } catch (Exception e) {
            throw new RuntimeException("getValueAt(" + rowIndex + ", " + columnIndex + ")", e);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == COLUMN_GROUPE) {
            return;
        }

        Eleve eleve = this.list.get(rowIndex);

        if (COLUMN_NOM == columnIndex) {
            eleve.setNom((String) aValue);
        } else if (COLUMN_PRENOM == columnIndex) {
            eleve.setPrenom((String) aValue);
        }

        try {
            EleveService.update(eleve);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "La mise à jour a échouée", "Erreur", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("La mise à jour a échouée", e);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == COLUMN_ACTION;
    }
}
