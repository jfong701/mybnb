package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlConnection {
	private static final String dbClassName = "com.mysql.cj.jdbc.Driver";
	private static final String CONNECTION = "jdbc:mysql://127.0.0.1/";
	private static final String dbName = "mybnbdev";
	private static final String USER = "root";
	private static final String PASS = "";
	// Object that establishes and keeps the state of our application's
	// connection with the MySQL backend.
	private Connection conn = null;
	private Statement stmt = null;

	// Object which communicates with the SQL backend delivering to it the
	// desired query from our application and returning the results of this
	// execution the same way that are received from the SQL backend.
	public static void main(String[] args) {
		MySqlConnection a = new MySqlConnection();
		a.connect();

		String testQuery = "SELECT * FROM Countries;";
		try {
			ResultSet rs = a.execute(testQuery);
			a.PrintResultSetOutput(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		a.disconnect();
	}

	/* Connect to Database */
	public void connect() {
		try {
			// Register JDBC Driver
			Class.forName(dbClassName);
			String connection = CONNECTION + dbName;
			conn = DriverManager.getConnection(connection, USER, PASS);
			stmt = conn.createStatement();
			System.out.println("Connected to " + dbClassName + "/" + dbName + " successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			disconnect();
		}
	}

	/* Disconnect from Database */
	public void disconnect() {
		try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			System.err.println("Exception occured while disconnecting!");
			e.printStackTrace();
		} finally {
			stmt = null;
			conn = null;
		}
	}

	/* Generic function to execute a query, and get a ResultSet */
	public ResultSet execute(String query) throws SQLException {
		ResultSet result = null;
		try {
			Statement statement = conn.createStatement();
			result = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void PrintResultSetOutput(ResultSet rs) {
		try {
			ResultSetMetaData rsmd = rs.getMetaData();

			// get header
			// System.out.print("COLNUM, ");
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				if (i > 1) {
					System.out.print(", ");
				}
				System.out.print(rsmd.getColumnName(i));
			}
			System.out.println("");

			rs.beforeFirst();
			// for each row
			// int colNum = 1;
			while (rs.next()) {

				// System.out.print(colNum + " | ");

				// for each column in a row
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					if (i > 1) {
						System.out.print(" | ");
					}
					System.out.print(rs.getString(i));
				}
				// colNum++;
				System.out.println("");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
