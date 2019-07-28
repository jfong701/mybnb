package jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import dbobjects.Amenity;
import dbobjects.Creditcard;
import dbobjects.Lister;
import dbobjects.Listing;
import dbobjects.Paypal;
import dbobjects.Renter;
import dbobjects.RoomType;
import dbobjects.User;

public class DAO {
	
	private static MySqlConnection db;
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
		if (Main.debug) {
			System.out.println(query);
		}
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
		if (Main.debug) {
			System.out.println(query);
		}
		try {
			db.executeUpdate(query);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean addPaypal(Paypal pp) {
		String query = "INSERT INTO Paypals(Email, AccountHolderName) "
				+ "VALUES ("
				+ q(pp.Email) + ", "
				+ q(pp.AccountHolderName) + ");";
		if (Main.debug) {
			System.out.println(query);
		}
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
		if (Main.debug) {
			System.out.println(query);
		}
		try {
			db.executeUpdate(query);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean addLister(Lister lister) {
		String query = "INSERT INTO Listers(PaypalEmail, userSIN) "
				+ "VALUES ("
				+ q(lister.PaypalEmail) + ", "
				+ lister.userSIN + ");";
		if (Main.debug) {
			System.out.println(query);
		}
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
	
	public String getListerNameByListerId(int id) {
		String query = "SELECT CONCAT(Users.FirstName, ' ', Users.LastName) "
				+ "AS FullName FROM Listers "
				+ "LEFT JOIN Users on Listers.UserSIN =  Users.UserSIN "
				+ "WHERE Listers.Id = " + id + ";";
		CachedRowSet rs = null;
		String fullName = null;
		try {
			rs = db.execute(query);
			if (rs.first()) {
				fullName = rs.getString("FullName");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fullName;
	}
	
	
	public ArrayList<Amenity> getListingAmenitesByListingId(int listingId) {
		String query = "SELECT Amenities.Id, AmenityName, AmenityDescription, AmenitycategoryId "
				+ "FROM ListingsAmenities LEFT JOIN Amenities "
				+ "ON ListingsAmenities.AmenityId = Amenities.Id "
				+ "WHERE ListingId = " + listingId + ";";
		ArrayList<Amenity> amenities = null;
		CachedRowSet rs = null;
		if (Main.debug) { System.out.println(query); }
		try {
			amenities = new ArrayList<Amenity>();
			rs = db.execute(query);
			while(rs.next()) {
				amenities.add(rsToAmenity(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return amenities;
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
	
	public RoomType getRoomTypeById(int id) {
		String query = "SELECT * FROM RoomTypes WHERE Id = " + id + ";";
		CachedRowSet rs = null;
		RoomType roomtype = null;
		try {
			rs = db.execute(query);
			if (rs.first()) {
				roomtype = new RoomType(
					rs.getInt("Id"),
					rs.getString("RoomtypeName"),
					rs.getString("RoomtypeDescription")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return roomtype;
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
	
	public String getCountryNameById(int id) {
		String query = "SELECT CountryName FROM Countries WHERE Id = " + id + ";";
		String returnVal = null;
		CachedRowSet rs = null;
		try {
			rs = db.execute(query);
			if (rs.first()) {
				returnVal = rs.getString("CountryName");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnVal;
	}
	

	public ArrayList<Amenity> getAllAmenities() {
		ArrayList<Amenity> amenityList = null; 
		CachedRowSet rs = null;
		String query = "SELECT * FROM Amenities;";
		try {
			amenityList = new ArrayList<Amenity>();
			rs = db.execute(query);
			while(rs.next()) {
				amenityList.add(rsToAmenity(rs));
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return amenityList;
	}
	
	public ArrayList<Listing> getListingsCustomSearch(Search search) {
		String query = "SELECT *";
		if (search.inLatitude >= -180 && search.inLongitude >= -180) {
			query += ",haversine(Latitude, Longitude, " + search.inLatitude + ", " + search.inLongitude + ") AS Distance";
		}
		query += " FROM Listings "
				+ "WHERE " + search.getAllConditionsString() + ";";
		CachedRowSet rs = null;
		ArrayList<Listing> listings = null;
		if (Main.debug) {
			System.out.println(query);
		}
		try {
			rs = db.execute(query);
			listings = new ArrayList<Listing>();
			while (rs.next()) {
				listings.add(rsToListing(rs));
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return listings;
	}
	
	public Listing getListingById(int id) {
		String query = "SELECT * FROM Listings WHERE Id = " + id;
		CachedRowSet rs = null;
		Listing listing = null;
		if (Main.debug) {
			System.out.println(query);
		}
		try {
			rs = db.execute(query);
			listing = new Listing();
			if (rs.first()) {
				listing = rsToListing(rs);
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return listing;
	}
	
	public ArrayList<Integer> getListingIdsAvailableByDateRange(Date checkInDate, Date checkOutDate) {
		
		// get number of days available needed
		String query1 = "SELECT DATEDIFF(" + q(checkOutDate.toString()) + ", " + q(checkInDate.toString())  + ");";
		int numDaysNeeded = 0;
		if (Main.debug) {System.out.println(query1);}
		CachedRowSet rs = null;
		try {
			rs = db.execute(query1);
			if (rs.first()) {
				numDaysNeeded = rs.getInt(1); // get value of first column
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		ArrayList<Integer> listingIds = null;
		// get listing Ids that meet the criteria of number of days needed
		String query2 = "SELECT COUNT(*) AS DaysAvailable, ListingId "
				+ "FROM Calendars "
				+ "WHERE IsAvailable = TRUE AND "
				+ "DayOfStay BETWEEN " + q(checkInDate.toString()) + " AND SUBDATE(" + q(checkOutDate.toString()) + ", 1) "
				+ "GROUP BY ListingId "
				+ "HAVING DaysAvailable = " + numDaysNeeded + ";";
		if (Main.debug) {System.out.println(query2);}
		rs = null;
		try {
			rs = db.execute(query2);
			listingIds = new ArrayList<Integer>();
			while (rs.next()) {
				listingIds.add(rs.getInt("ListingId"));
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		return listingIds;
	}
	
	public ArrayList<Integer> getListingIdsUnderMaxPrice(double maxPrice) {
		
		String query = "SELECT Id FROM Listings WHERE BasePrice <= " + maxPrice + ";";
		
		ArrayList<Integer> listingIds = null;
		
		if (Main.debug) {System.out.println(query);}
		CachedRowSet rs = null;
		try {
			rs = db.execute(query);
			listingIds = new ArrayList<Integer>();
			while (rs.next()) {
				listingIds.add(rs.getInt("Id"));
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		return listingIds;
	}
	
	public ArrayList<Integer> getListingIdsWithAllAmenities(List<String>amenityIds) {
		
		// format the list of ints as a string like (1, 2, 3, 5)
		String formattedAmenityIds = "(";
		for (int i = 0; i < amenityIds.size(); i++) {
			formattedAmenityIds += amenityIds.get(i);
			if (i < amenityIds.size() - 1) {
				formattedAmenityIds += ", ";
			}
		}
		formattedAmenityIds += ")";
		
		// returns only ListingIds which have everything
		String query = "SELECT ListingId "
				+ "FROM ListingsAmenities "
				+ "WHERE AmenityId IN "
				+ formattedAmenityIds + " "
				+ "GROUP BY (ListingId) "
				+ "HAVING COUNT(*) = "
				+ amenityIds.size();
		
		ArrayList<Integer> listingIds = null;
		
		if (Main.debug) {System.out.println(query);}
		CachedRowSet rs = null;
		try {
			rs = db.execute(query);
			listingIds = new ArrayList<Integer>();
			while (rs.next()) {
				listingIds.add(rs.getInt("ListingId"));
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		return listingIds;
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
				+ "))) < " + searchRadiusKm + ";";
		CachedRowSet result = null;
		try {
			result = db.execute(query);
		} catch (SQLException e){
			e.printStackTrace();
		}
		return result;
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
	
	// converts an extracted resultset to a Listing object
	public Listing rsToListing(ResultSet rs) throws SQLException {
		Listing l = new Listing(
				rs.getInt("Id"),
				rs.getString("Title"),
				rs.getString("ListingDescription"),
				rs.getDouble("BasePrice"),
				rs.getDouble("Latitude"),
				rs.getDouble("Longitude"),
				rs.getString("City"),
				rs.getString("PostalCode"),
				rs.getString("Address"),
				rs.getTime("CheckInTime"),
				rs.getTime("CheckOutTime"),
				rs.getInt("MaxNumGuests"),
				rs.getInt("CountryId"),
				rs.getInt("RoomTypeId"),
				rs.getInt("ListerId")
				);
		
		// only fill in distance if it exists in the output
		try {
			l.DistanceFromSearch = rs.getDouble("Distance");
		} catch(SQLException e) {
			if (Main.debug) {
				System.out.println("swallowed the exception");
			}
		}

		l.fillDecorative(this);
		return l;
	}
	
	// converts an extracted resultset to a Amenities object
	public Amenity rsToAmenity(ResultSet rs) throws SQLException {
		Amenity a = new Amenity(
				rs.getInt("Id"),
				rs.getString("AmenityName"),
				rs.getString("AmenityDescription"),
				rs.getInt("AmenitycategoryId")
			);
		
		return a;
	}

}
