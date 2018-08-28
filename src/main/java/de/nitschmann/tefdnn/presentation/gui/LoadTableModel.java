package de.nitschmann.tefdnn.presentation.gui;

import javax.swing.table.AbstractTableModel;

public class LoadTableModel extends AbstractTableModel {
    private String[] columnNames;
    private Object[][] data;

    public LoadTableModel(String[] columnNames, Object[][] data) {
        this.columnNames = columnNames;
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public void removeRow(Integer row) {
        Object[][] temp = new Object[data.length - 1][];
        boolean rowRemoved = false;
        for(int i = 0; i < data.length; i++) {
            if (i == row) {
                rowRemoved = true;
                continue;
            }
            if (rowRemoved) {
                temp[i-1] = data[i];
            } else {
                temp[i] = data[i];
            }
        }
        data = temp;
        this.fireTableDataChanged();
    }
}
