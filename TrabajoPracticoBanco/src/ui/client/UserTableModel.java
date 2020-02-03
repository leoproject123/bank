package ui.client;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.Transaction;

public class UserTableModel extends AbstractTableModel {

	private static final int COLUMNA_ID = 0;
	private static final int COLUMNA_CREATION_DATE = 1;
	private static final int COLUMNA_TYPE = 2;
	private static final int COLUMNA_DEBIT = 3;
	private static final int COLUMNA_CREDIT = 4;
	private String[] columnNames = { "ID", "Creation Date", "Type", "Debit", "Credit" };
	private Class[] columnTypes = { Integer.class, Timestamp.class, String.class, Float.class, Float.class };
	private List<Transaction> content;

	public UserTableModel() {
		content = new ArrayList<Transaction>();
	}

	public UserTableModel(List<Transaction> initialContent) {
		content = initialContent;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return content.size();
	}

	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		super.setValueAt(value, rowIndex, columnIndex);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {

		Transaction u = content.get(rowIndex);

		Object result = null;
		switch (columnIndex) {
		case COLUMNA_ID:
			result = u.getID();
			break;
		case COLUMNA_CREATION_DATE:
			result = u.getDate();
			break;
		case COLUMNA_TYPE:
			result = u.getType().getDescription();
			break;
		case COLUMNA_DEBIT:
			if (u.getAmount() < 0) {
				result = Math.abs(u.getAmount());
			}
			break;
		case COLUMNA_CREDIT:
			if (u.getAmount() > 0) {
				result = u.getAmount();
			}
			break;
		default:
			result = new String("");
		}

		return result;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Class getColumnClass(int col) {
		return columnTypes[col];
	}

	public List<Transaction> getContent() {
		return content;
	}

	public void setContenido(List<Transaction> content) {
		this.content = content;
	}
}
