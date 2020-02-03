package ui.admin;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.User;

public class UsersTableModel extends AbstractTableModel {

	private static final int COLUMNA_ID = 0;
	private static final int COLUMNA_CREATION_DATE = 1;
	private static final int COLUMNA_USER = 2;
	private static final int COLUMNA_PASSWORD = 3;
	private String[] columnNames = { "ID", "Creation Date", "User", "Password" };
	private Class[] columnTypes = { Integer.class, Timestamp.class, String.class, String.class };
	private List<User> content;

	public UsersTableModel() {
		content = new ArrayList<User>();
	}

	public UsersTableModel(List<User> initialContent) {
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

		User u = content.get(rowIndex);

		Object result = null;
		switch (columnIndex) {
		case COLUMNA_ID:
			result = u.getID();
			break;
		case COLUMNA_CREATION_DATE:
			result = u.getCreationDate();
			break;
		case COLUMNA_USER:
			result = u.getUser();
			break;
		case COLUMNA_PASSWORD:
			result = u.getPassword();
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

	public List<User> getContent() {
		return content;
	}

	public void setContent(List<User> content) {
		this.content = content;
	}
}
