package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
	
	private static Connection conn = null;
	private static String dbName = "./elaboratilaurea";
	private static String username = "sa";
	private static String password = "";
	
	private DBManager() {}
	
	public static Connection getConnection() throws SQLException {
		if (conn == null) {
			conn = DriverManager.getConnection("jdbc:h2:"+dbName, username, password);
		}
		return conn;
	}
	
	public static void closeConnection() throws SQLException {
		if (conn != null) {
			conn.close();
		}
	}
}
