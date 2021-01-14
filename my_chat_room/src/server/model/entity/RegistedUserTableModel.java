package server.model.entity;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

//��ע���û��б���
public class RegistedUserTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -6299791067241594227L;

    //��������
    private String[] title = {"�˺�","����","�ǳ�","�Ա�"};
    //������
    private List<String[]> rows = new ArrayList<String[]>();

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return title.length;
    }

    public String getColumnName(int column){
        return title[column];
    }

    @Override
    public String getValueAt(int rowIndex, int columnIndex) {
        return (rows.get(rowIndex))[columnIndex];
    }

    public void add(String[] value){
        int row = rows.size();
        rows.add(value);
        fireTableRowsInserted(row, row);
    }

    public void remove(long id){
        int row = -1;
        for(int i=0;i<=rows.size();i++){
            if (String.valueOf(id).equals(getValueAt(i, 0))) {
                row = i;
                break;
            }
        }
        rows.remove(row);
        fireTableRowsDeleted(2, 3);
    }
}
