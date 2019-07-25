package jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;

public class DAO {
	public static MySqlConnection db;

	public static void main(String[] args) {
		db = new MySqlConnection();
		DAO dao = new DAO();
		try {
			db.connect();

			dao.getAllAmenities();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.disconnect();
		}
	}

	public void getAllAmenities() throws SQLException {
		String query = "SELECT * FROM Amenities;";
		CachedRowSet result = db.execute(query);
		db.PrintResultSetOutput(result);
	}
}
