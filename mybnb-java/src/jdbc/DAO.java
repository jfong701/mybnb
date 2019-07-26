package jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.sql.rowset.CachedRowSet;

public class DAO {
	
	private MySqlConnection db;
	
	public DAO() throws SQLException {
		db = new MySqlConnection();
		db.connect();
	}
	
	public void endDAO() {
		db.disconnect();
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
