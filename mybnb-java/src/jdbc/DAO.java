package jdbc;

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
			dao.getListingsWithinRadius(32.715658, -117.16116, 0.6);

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
	
	public void getListingsWithinRadius(double lati, double longi, double searchRadiusKm) throws SQLException{
		double earthRadius = 6378.1;
		String query = "SELECT * FROM Listings "
				+ "WHERE (2 * " + earthRadius
				+ " * ASIN( SQRT( POWER( SIN( "
				+ "(RADIANS(" + lati + ") - "
				+ "RADIANS(Latitude))/2)"
				+ ",2)"
				+ "+ COS(RADIANS(Latitude))"
				+ "* COS(RADIANS(" + lati + "))"
				+ "* POWER( SIN( "
				+ "(RADIANS( " + longi + ") - "
				+ "RADIANS(Longitude))/2)"
				+ ",2)"
				+ "))) > " + searchRadiusKm + ";";
		CachedRowSet result = db.execute(query);
		db.PrintResultSetOutput(result);
	}
}
