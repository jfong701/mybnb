package jdbc;

import java.sql.SQLException;
import java.time.LocalDate;

import javax.sql.rowset.CachedRowSet;

public class DAO {
	public static MySqlConnection db;

	public static void main(String[] args) {
		db = new MySqlConnection();
		DAO dao = new DAO();
		try {
			db.connect();

			db.PrintResultSetOutput(dao.getAllAmenities());
			db.PrintResultSetOutput(dao.getListingsWithinRadius(32.715658, -117.16116, 0.6));
			db.PrintResultSetOutput(dao.getListingsByPostalCode("CA"));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.disconnect();
		}
	}

	public CachedRowSet getAllAmenities() {
		CachedRowSet result = null;
		String query = "SELECT * FROM Amenities;";
		try {
			result = db.execute(query);
		} catch (SQLException e){
			e.printStackTrace();
		}
		return result;
	}
	
	public CachedRowSet getListingsWithinRadius(double lati, double longi, double searchRadiusKm){
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
		CachedRowSet result = null;
		try {
			result = db.execute(query);
		} catch (SQLException e){
			e.printStackTrace();
		}
		return result;
	}
	
	public CachedRowSet getListingsWithinRadiusAndAvailable(double lati, double longi, double searchRadiusKm, LocalDate checkInDate, LocalDate checkOutDate) {
		return null;
	}
	
	
	public CachedRowSet getListingsByPostalCode(String postalCode) throws SQLException{
		String query = "SELECT * FROM Listings "
				+ "WHERE "
				+ "PostalCode LIKE '" + postalCode + "%'";
		CachedRowSet result = null;
		try {
			result = db.execute(query);
		} catch (SQLException e){
			e.printStackTrace();
		}
		return result;
	}
}
