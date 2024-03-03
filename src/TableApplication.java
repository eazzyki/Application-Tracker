import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

enum SortOrder {
    ASCENDING,
    DESCENDING
}

public class TableApplication extends AbstractTableModel {
    private String[] tableColumns = {"Date", "Company", "Position", "State"};
    private ArrayList<Item> data;
    private DataStorage dataStorage = new DataStorage();
    
    public TableApplication() {
        init();
    }

    private void init() {
        data = dataStorage.readData();
    }

    public void addNewItem(Item row) {
        assert row.length() == tableColumns.length : "Num of Columns of new Entry is not correct!";
        data.add(row);
    }

    public void writeToDatabase() {
        dataStorage.writeData(data);
    }

    public void readFromDatabase() {
        dataStorage.readData();
    }

    public void updateItem(String[] newRow, int row) {
        for (int i = 0; i < tableColumns.length; i++) {
            setValueAt(row, i, newRow[i]);
        }
    }

    public void sortTable(int columnIdx, SortOrder order) {
        switch (columnIdx) {
            // Date
            case 0:
                if (order == SortOrder.ASCENDING) {
                    data.sort(Comparator.comparing(Item::getDate));
                } else {
                    data.sort(Comparator.comparing(Item::getDate).reversed());
                }
                break;
            // Company
            case 1:
                if (order == SortOrder.ASCENDING) {
                    data.sort(Comparator.comparing(Item::getCompany));
                } else {
                    data.sort(Comparator.comparing(Item::getCompany).reversed());
                }
                break;
            // Position
            case 2:
                if (order == SortOrder.ASCENDING) {
                    data.sort(Comparator.comparing(Item::getPosition));
                } else {
                    data.sort(Comparator.comparing(Item::getPosition).reversed());
                }
                break;
            // State
            case 3:
                if (order == SortOrder.ASCENDING) {
                    data.sort(Comparator.comparing(Item::getState));
                } else {
                    data.sort(Comparator.comparing(Item::getState).reversed());
                }
                break;
            default:
                data.sort(Comparator.comparing(Item::getDate));
                break;
        }
    }

    public void deleteItem(int row) {
        data.remove(row);
    }

    public int[] getApplicationCounts() {
        int countOngoing = 0;
        int countInvited = 0;
        int countRejected = 0;
        for (int i = 0; i < data.size(); i++) {
            String state = data.get(i).getState().toString();
            switch (state) {
                case "ONGOING":
                    countOngoing += 1;
                    break;
                case "INVITED":
                    countInvited += 1;
                    break;
                case "REJECTED":
                    countRejected += 1;
                    break;
                default:
                    break;
            }
        }
        return new int[] {countOngoing, countInvited, countRejected};
    }

    public void setValueAt(int row, int col, Object val) {
        data.get(row).setValueAt(col, val);
    }

    @Override
    public int getColumnCount() {
        return tableColumns.length;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex).getValueAt(columnIndex);
    }

    @Override
    public String getColumnName(int index) {
        return tableColumns[index];
    }
}


class MyTable extends JTable {
    Color lightRed = new Color(255, 180, 180);
    Color lightGray = new Color(190, 190, 190);
    Color lightGreen = new Color(150, 255, 190);

    public MyTable(AbstractTableModel model) {
        setModel(model);
        getColumnModel()
        .getColumn(3)
        .setCellRenderer(new StateColumnRenderer());
        setRowHeight(30);
        
        // Renderer for Column Date
        DefaultTableCellRenderer dateRenderer = new DefaultTableCellRenderer();
        dateRenderer.setHorizontalAlignment(JLabel.CENTER);
        dateRenderer.setFont(new Font(getFont().toString(), 1, 15));
        getColumnModel().getColumn(0).setCellRenderer(dateRenderer);
        getColumnModel().getColumn(0).setPreferredWidth(5);
        
        
        // Renderer for Column State
        DefaultTableCellRenderer stateRenderer = new DefaultTableCellRenderer();
        stateRenderer.setHorizontalAlignment(JLabel.CENTER);
        stateRenderer.setFont(new Font(getFont().toString(), 1, 15));
        getColumnModel().getColumn(3).setCellRenderer(stateRenderer);
        getColumnModel().getColumn(3).setPreferredWidth(5);

    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int columnIndex) {
        JComponent component = (JComponent) super.prepareRenderer(renderer, rowIndex, columnIndex);  
        if (getValueAt(rowIndex, 3).toString() == "ONGOING" && columnIndex == 3) {
            component.setBackground(lightGray);
            component.setForeground(Color.BLACK);
        } else if (getValueAt(rowIndex, 3).toString() == "INVITED" && columnIndex == 3) {
            component.setBackground(lightGreen);
            component.setForeground(Color.BLACK);

        } else if (getValueAt(rowIndex, 3).toString() == "REJECTED" && columnIndex == 3) {
            component.setBackground(lightRed);
            component.setForeground(Color.BLACK);
        } else {
            component.setForeground(Color.BLACK);
        }

        if (columnIndex == 0) {
            component.setFont(new Font(getFont().toString(), 1, 13));
        }
        return component;
    }
}


class StateColumnRenderer extends DefaultTableCellRenderer {
    Color backgroundColor, foregroundColor;
    public StateColumnRenderer() {
        super();
    }

    public StateColumnRenderer(Color backgroundColor, Color foregroundColor) {
        super();
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,   boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        cell.setBackground(backgroundColor);
        cell.setForeground(foregroundColor);
        return cell;
    }
}
