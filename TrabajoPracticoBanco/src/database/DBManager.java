package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public abstract class DBManager {

	private static final String DB_DRIVER = "org.h2.Driver";
	private static final String DB_BASE_URL = "jdbc:h2:tcp://localhost//{DIR}";
	private static final String DB_NAME = "/tp";
	private static final String DB_USERNAME = "sa";
	private static final String DB_PASSWORD = "";

	public static Connection connect() {
		Connection c = null;
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		try {
			String url = DB_BASE_URL + DB_NAME;
			url = url.replace("{DIR}", obtainDatabaseLocation());
			c = DriverManager.getConnection(url, DB_USERNAME, DB_PASSWORD);
			c.setAutoCommit(false);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"La conexion con la base de datos esta rota. El programa detendra su ejecucion. ", "Lo siento!",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		return c;
	}

	private static String obtainDatabaseLocation() {
		File currDir = new File("h2/base_de_datos/");
		return currDir.getAbsolutePath();
	}

}
