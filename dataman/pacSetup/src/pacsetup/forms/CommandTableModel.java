package pacsetup.forms;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class CommandTableModel extends AbstractTableModel {

    private List<AbstractCommand> lstCommands;
    private String[] colNames = {"Command", "Current Executing Action", "Status"};

    public CommandTableModel(AbstractCommand... commands) {
        lstCommands = new ArrayList<>();
        for (int i = 0; i < commands.length; i++) {
            lstCommands.add(commands[i]);
        }
    }

    public List<AbstractCommand> getCommands() {
        return lstCommands;
    }

    @Override
    public int getRowCount() {
        return lstCommands.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = "";
        AbstractCommand command = lstCommands.get(rowIndex);
        if (columnIndex == 0) {
            value = command.getDisplayCommand().trim();
        } else if (columnIndex == 1) {
            value = command.getCurrentExecutingActionString();
        } else if (columnIndex == 2) {
            value = command.getStatusString().trim();
        }
        fireTableDataChanged();
        return value;
    }

    @Override
    public String getColumnName(int column) {
        return this.colNames[column];
    }

    @Override
    public int findColumn(String columnName) {
        int colIndex = -1;
        for (int i = 0; i < this.colNames.length; i++) {
            if (columnName.equalsIgnoreCase(this.colNames[i])) {
                colIndex = i;
                break;
            }
        }
        return colIndex;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

}
