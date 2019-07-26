package jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.sql.rowset.CachedRowSet;

import dbobjects.User;

public class DAO {
	
	private MySqlConnection db;
	public static final int INVALID_ID = -1;
	
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
	
	public boolean addUser(User user) {
		String query = "INSERT INTO Users(UserSIN, Email, FirstName, LastName, Occupation, Address, City, DOB, CountryId) " 
				+ "VALUES ("
				+ user.UserSIN + ", "
				+ "'" + user.Email + "'" + ", "
				+ "'" + user.FirstName + "'"  + ", "
				+ "'" + user.LastName + "'" + ", "
				+ "'" + user.Occupation + "'" + ", "
				+ "'" + user.Address + "'" + ", "
				+ "'" + user.City + "'" + ", "
				+ "'" + user.DOB + "'" + ", "
				+ user.CountryId + ");";
		System.out.println(query);
		try {
			db.executeUpdate(query);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public User getUserByEmail(String email) {
		String query = "SELECT * FROM Users WHERE Email = '" + email + "'";
		CachedRowSet rs = null;
		User user = null;
		try {
			rs = db.execute(query);
			if (rs.first()) {
				user = new User();
				user.UserSIN = rs.getInt("UserSIN");
				user.Email = rs.getString("Email");
				user.FirstName = rs.getString("FirstName");
				user.LastName = rs.getString("LastName");
				user.Occupation = rs.getString("Occupation");
				user.Address = rs.getString("Address");
				user.City = rs.getString("City");
				user.DOB = rs.getDate("DOB");
				user.CreatedAt = rs.getTimestamp("CreatedAt");
				user.LastLoggedInAt = rs.getTimestamp("LastLoggedInAt");
				user.CountryId = rs.getInt("CountryId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	// returns -1 if not a valid country
	public int getCountryIdByCountryName(String countryName) {
		String query = "SELECT Id FROM Countries WHERE CountryName = '" + countryName + "';";
		CachedRowSet rs = null;
		int returnId = INVALID_ID;
		try {
			rs = db.execute(query);
			if (rs.first()) {
				returnId = rs.getInt("Id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnId;
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
