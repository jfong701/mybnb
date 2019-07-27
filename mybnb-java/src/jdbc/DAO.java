package jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.sql.rowset.CachedRowSet;

import dbobjects.Creditcard;
import dbobjects.Lister;
import dbobjects.Renter;
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
	
	/*/
	 * Adds single quotes around a string
	 * (Looks cleaner than "'" everywhere.
	 */
	private String q(Object toQuote) {
		return "'" + toQuote + "'"; 
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
	
	public boolean addCreditcard(Creditcard cc) {
		String query = "INSERT INTO Creditcards(CardNumber, ExpiryDate, AccountHolderName) "
				+ "VALUES ("
				+ q(cc.CardNumber) + ", "
				+ q(cc.ExpiryDate) + ", "
				+ q(cc.AccountHolderName) + ");";
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
	
	public boolean addRenter(Renter renter) {
		String query = "INSERT INTO Renters(CreditcardNumber, userSIN) "
				+ "VALUES ("
				+ q(renter.CreditcardNumber) + ", "
				+ renter.userSIN + ");";
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
	
	public Lister getListerByUserSIN(int userSIN) {
		String query = "SELECT * FROM Listers WHERE userSIN = " + userSIN + ";";
		CachedRowSet rs = null;
		Lister lister = null;
		try {
			rs = db.execute(query);
			if (rs.first()) {
				lister = new Lister(
					rs.getInt("Id"),
					rs.getString("PaypalEmail"),
					rs.getInt("userSIN")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lister;
	}
	
	public Renter getRenterByUserSIN(int userSIN) {
		String query = "SELECT * FROM Renters WHERE userSIN = " + userSIN + ";";
		CachedRowSet rs = null;
		Renter renter = null;
		try {
			rs = db.execute(query);
			if (rs.first()) {
				renter = new Renter(
					rs.getInt("Id"),
					rs.getString("CreditcardNumber"),
					rs.getInt("userSIN")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return renter;
	}
	
	public boolean isUserALister(int userSIN) {
		if (getListerByUserSIN(userSIN) != null) {
			return true;
		}
		return false;
	}
	
	public boolean isUserARenter(int userSIN) {
		if (getRenterByUserSIN(userSIN) != null) {
			return true;
		}
		return false;
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
