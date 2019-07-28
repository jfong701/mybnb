[1mdiff --git a/mybnb-java/src/jdbc/DAO.java b/mybnb-java/src/jdbc/DAO.java[m
[1mindex 6e49754..49bfa28 100644[m
[1m--- a/mybnb-java/src/jdbc/DAO.java[m
[1m+++ b/mybnb-java/src/jdbc/DAO.java[m
[36m@@ -6,9 +6,11 @@[m [mimport java.sql.ResultSetMetaData;[m
 import java.sql.SQLException;[m
 import java.time.LocalDate;[m
 import java.util.ArrayList;[m
[32m+[m[32mimport java.util.List;[m
 [m
 import javax.sql.rowset.CachedRowSet;[m
 [m
[32m+[m[32mimport dbobjects.Amenity;[m
 import dbobjects.Creditcard;[m
 import dbobjects.Lister;[m
 import dbobjects.Listing;[m
[36m@@ -320,15 +322,20 @@[m [mpublic class DAO {[m
 	}[m
 	[m
 [m
[31m-	public CachedRowSet getAllAmenities() {[m
[31m-		CachedRowSet result = null;[m
[32m+[m	[32mpublic ArrayList<Amenity> getAllAmenities() {[m
[32m+[m		[32mArrayList<Amenity> amenityList = null;[m[41m [m
[32m+[m		[32mCachedRowSet rs = null;[m
 		String query = "SELECT * FROM Amenities;";[m
 		try {[m
[31m-			result = db.execute(query);[m
[32m+[m			[32mamenityList = new ArrayList<Amenity>();[m
[32m+[m			[32mrs = db.execute(query);[m
[32m+[m			[32mwhile(rs.next()) {[m
[32m+[m				[32mamenityList.add(rsToAmenity(rs));[m
[32m+[m			[32m}[m
 		} catch (SQLException e){[m
 			e.printStackTrace();[m
 		}[m
[31m-		return result;[m
[32m+[m		[32mreturn amenityList;[m
 	}[m
 	[m
 	public ArrayList<Listing> getListingsCustomSearch(Search search) {[m
[36m@@ -434,6 +441,43 @@[m [mpublic class DAO {[m
 		return listingIds;[m
 	}[m
 	[m
[32m+[m	[32mpublic ArrayList<Integer> getListingIdsWithAllAmenities(List<String>amenityIds) {[m
[32m+[m[41m		[m
[32m+[m		[32m// format the list of ints as a string like (1, 2, 3, 5)[m
[32m+[m		[32mString formattedAmenityIds = "(";[m
[32m+[m		[32mfor (int i = 0; i < amenityIds.size(); i++) {[m
[32m+[m			[32mformattedAmenityIds += amenityIds.get(i);[m
[32m+[m			[32mif (i < amenityIds.size() - 1) {[m
[32m+[m				[32mformattedAmenityIds += ", ";[m
[32m+[m			[32m}[m
[32m+[m		[32m}[m
[32m+[m		[32mformattedAmenityIds += ")";[m
[32m+[m[41m		[m
[32m+[m		[32m// returns only ListingIds which have everything[m
[32m+[m		[32mString query = "SELECT ListingId "[m
[32m+[m				[32m+ "FROM ListingsAmenities "[m
[32m+[m				[32m+ "WHERE AmenityId IN "[m
[32m+[m				[32m+ formattedAmenityIds + " "[m
[32m+[m				[32m+ "GROUP BY (ListingId) "[m
[32m+[m				[32m+ "HAVING COUNT(*) = "[m
[32m+[m				[32m+ amenityIds.size();[m
[32m+[m[41m		[m
[32m+[m		[32mArrayList<Integer> listingIds = null;[m
[32m+[m[41m		[m
[32m+[m		[32mif (Main.debug) {System.out.println(query);}[m
[32m+[m		[32mCachedRowSet rs = null;[m
[32m+[m		[32mtry {[m
[32m+[m			[32mrs = db.execute(query);[m
[32m+[m			[32mlistingIds = new ArrayList<Integer>();[m
[32m+[m			[32mwhile (rs.next()) {[m
[32m+[m				[32mlistingIds.add(rs.getInt("ListingId"));[m
[32m+[m			[32m}[m
[32m+[m		[32m} catch (SQLException e){[m
[32m+[m			[32me.printStackTrace();[m
[32m+[m		[32m}[m
[32m+[m[41m		[m
[32m+[m		[32mreturn listingIds;[m
[32m+[m	[32m}[m
 	[m
 	[m
 	public CachedRowSet getListingsWithinRadius(double lati, double longi, double searchRadiusKm){[m
[36m@@ -460,10 +504,6 @@[m [mpublic class DAO {[m
 		return result;[m
 	}[m
 	[m
[31m-	public CachedRowSet getListingsWithinRadiusAndAvailable(double lati, double longi, double searchRadiusKm, LocalDate checkInDate, LocalDate checkOutDate) {[m
[31m-		return null;[m
[31m-	}[m
[31m-	[m
 	[m
 	public CachedRowSet getListingsByPostalCode(String postalCode) throws SQLException{[m
 		String query = "SELECT * FROM Listings "[m
[36m@@ -510,5 +550,17 @@[m [mpublic class DAO {[m
 		l.fillDecorative(this);[m
 		return l;[m
 	}[m
[32m+[m[41m	[m
[32m+[m	[32m// converts an extracted resultset to a Amenities object[m
[32m+[m	[32mpublic Amenity rsToAmenity(ResultSet rs) throws SQLException {[m
[32m+[m		[32mAmenity a = new Amenity([m
[32m+[m				[32mrs.getInt("Id"),[m
[32m+[m				[32mrs.getString("AmenityName"),[m
[32m+[m				[32mrs.getString("AmenityDescription"),[m
[32m+[m				[32mrs.getInt("AmenitycategoryId")[m
[32m+[m			[32m);[m
[32m+[m[41m		[m
[32m+[m		[32mreturn a;[m
[32m+[m	[32m}[m
 [m
 }[m
[1mdiff --git a/mybnb-java/src/jdbc/View.java b/mybnb-java/src/jdbc/View.java[m
[1mindex 1271be6..61b427c 100644[m
[1m--- a/mybnb-java/src/jdbc/View.java[m
[1m+++ b/mybnb-java/src/jdbc/View.java[m
[36m@@ -4,9 +4,12 @@[m [mimport java.sql.Date;[m
 import java.text.ParseException;[m
 import java.text.SimpleDateFormat;[m
 import java.util.ArrayList;[m
[32m+[m[32mimport java.util.Arrays;[m
[32m+[m[32mimport java.util.List;[m
 import java.util.Scanner;[m
 import java.util.stream.Collectors;[m
 [m
[32m+[m[32mimport dbobjects.Amenity;[m
 import dbobjects.Creditcard;[m
 import dbobjects.Lister;[m
 import dbobjects.Listing;[m
[36m@@ -88,12 +91,18 @@[m [mpublic class View {[m
 			System.out.println("1. Set a maximum price per night");[m
 			System.out.println("2. No more filtering, see results!");[m
 			System.out.print("Choose one of the previous options [0-2]: ");[m
[32m+[m		[32m} else if (this.viewName.contentEquals("SEARCHOPTIONSSCREEN4")) {[m
[32m+[m			[32mSystem.out.println("=========SEARCH OPTIONS SCREEN (refinements) =========");[m
[32m+[m			[32mSystem.out.println("0. Exit.");[m
[32m+[m			[32mSystem.out.println("1. Require certain amenities");[m
[32m+[m			[32mSystem.out.println("2. No more filtering, see results!");[m
[32m+[m			[32mSystem.out.print("Choose one of the previous options [0-2]: ");[m
 		}[m
 	}[m
 [m
 	public String choiceAction(int choice, Scanner sc, DAO dao) {[m
 		String input = null;[m
[31m-		if (this.viewName == "INITIALSCREEN") {[m
[32m+[m		[32mif (this.viewName.equals("INITIALSCREEN")) {[m
 			switch (choice) { // Activate the desired functionality[m
 			case 1:[m
 				// log into existing[m
[36m@@ -167,7 +176,7 @@[m [mpublic class View {[m
 				break;[m
 			}[m
 			return "MAINSCREEN";[m
[31m-		} else if (this.viewName == "MAINSCREEN") {[m
[32m+[m		[32m} else if (this.viewName.equals("MAINSCREEN")) {[m
 			switch (choice) { // Activate the desired functionality[m
 			case 1:[m
 				// log in screen[m
[36m@@ -218,7 +227,7 @@[m [mpublic class View {[m
 				break;[m
 			}[m
 			return "MAINSCREEN";[m
[31m-		} else if (this.viewName == "BECOMEARENTERSCREEN") {[m
[32m+[m		[32m} else if (this.viewName.equals("BECOMEARENTERSCREEN")) {[m
 			switch (choice) { // Activate the desired functionality[m
 			case 1:[m
 				// become a renter -> ensure logged in, and ensure not a renter already then ask[m
[36m@@ -282,7 +291,7 @@[m [mpublic class View {[m
 			}[m
 			// go back to mainscreen when done everything[m
 			return "MAINSCREEN";[m
[31m-		} else if (this.viewName == "BECOMEALISTERSCREEN") {[m
[32m+[m		[32m} else if (this.viewName.equals("BECOMEALISTERSCREEN")) {[m
 			switch (choice) {[m
 			case 1:[m
 				// become a lister -> ensure logged in, and ensure not a lister already then ask[m
[36m@@ -335,7 +344,7 @@[m [mpublic class View {[m
 			}[m
 			// go back to mainscreen when done everything[m
 			return "MAINSCREEN";[m
[31m-		} else if (this.viewName == "SEARCHOPTIONSSCREEN") {[m
[32m+[m		[32m} else if (this.viewName.equals("SEARCHOPTIONSSCREEN")) {[m
 			[m
 			// reset the search if there are any leftovers from the last search[m
 			search.resetSearch();[m
[36m@@ -385,7 +394,7 @@[m [mpublic class View {[m
 				break;[m
 			}[m
 			return "SEARCHOPTIONSSCREEN";[m
[31m-		} else if (this.viewName == "SEARCHOPTIONSSCREEN2") {[m
[32m+[m		[32m} else if (this.viewName.equals("SEARCHOPTIONSSCREEN2")) {[m
 			switch (choice) {[m
 			case 1:[m
 				// add the temporal filter (dates)[m
[36m@@ -438,7 +447,7 @@[m [mpublic class View {[m
 				break;[m
 			}[m
 			return "SEARCHOPTIONSSCREEN";[m
[31m-		} else if (this.viewName == "SEARCHOPTIONSSCREEN3") {[m
[32m+[m		[32m} else if (this.viewName.equals("SEARCHOPTIONSSCREEN3")) {[m
 			switch(choice) {[m
 			case 1:[m
 				// Set a max price limit on any listings[m
[36m@@ -452,10 +461,38 @@[m [mpublic class View {[m
 				[m
 				// filter the previous results with this list of ids[m
 				search.searchResult = search.searchResult.parallelStream()[m
[31m-						.filter(l -> listingIds.contains(l.Id))[m
[32m+[m						[32m.filter(a -> listingIds.contains(a.Id))[m
 						.collect(Collectors.toList());[m
 			case 2:[m
 				[m
[32m+[m			[32mdefault:[m
[32m+[m				[32mreturn "SEARCHOPTIONSSCREEN4";[m
[32m+[m			[32m}[m
[32m+[m		[32m} else if (this.viewName.equals("SEARCHOPTIONSSCREEN4")) {[m
[32m+[m			[32mswitch(choice) {[m
[32m+[m			[32mcase 1:[m
[32m+[m				[32m// Show the amenities, and take in a comma separated list of them.[m
[32m+[m				[32mSystem.out.println("Available Amenities: ");[m
[32m+[m				[32mArrayList<Amenity> amenities = dao.getAllAmenities();[m
[32m+[m				[32mfor (Amenity a : amenities) {[m
[32m+[m					[32mSystem.out.println("Id: " + a.Id +", " + a.AmenityName);[m
[32m+[m				[32m}[m
[32m+[m
[32m+[m				[32mSystem.out.print("Enter a comma-separated list of the Amenity Ids that you require: ");[m
[32m+[m				[32minput = sc.nextLine();[m
[32m+[m[41m				[m
[32m+[m				[32m// remove any duplicates before feeding amenity Ids to dao[m
[32m+[m				[32mList<String> amenityIdsIn = Arrays.stream(input.split(","))[m
[32m+[m						[32m.map(a -> a.trim())[m
[32m+[m						[32m.distinct()[m
[32m+[m						[32m.collect(Collectors.toList());[m
[32m+[m[41m				[m
[32m+[m				[32mList<Integer> listingIds = dao.getListingIdsWithAllAmenities(amenityIdsIn);[m[41m [m
[32m+[m[41m				[m
[32m+[m				[32m// filter the search result with these listingIds.[m
[32m+[m				[32msearch.searchResult = search.searchResult.parallelStream()[m
[32m+[m						[32m.filter(a -> listingIds.contains(a.Id))[m
[32m+[m						[32m.collect(Collectors.toList());[m
 			default:[m
 				[m
 				// OUTPUT THE SEARCH RESULTS[m
[1mdiff --git a/sql/other-queries.sql b/sql/other-queries.sql[m
[1mindex 3abc0f7..418b494 100644[m
[1m--- a/sql/other-queries.sql[m
[1m+++ b/sql/other-queries.sql[m
[36m@@ -4,7 +4,7 @@[m [mSET @LongitudeIn = -117.161116;[m
 -- radius is in km.[m
 SET @earthRadius = 6378.1;[m
 SET @searchDistance = 0.6;[m
[31m--- Search for listings within a specified distance of a longitude and latitude[m
[32m+[m[32m-- Search for listings within a specified distance of a longitude and latitude --> now stored as a function in create-table-script[m
 SELECT * FROM Listings[m
 WHERE (2*@earthRadius[m
 	*ASIN([m
[36m@@ -29,6 +29,18 @@[m [mWHERE (2*@earthRadius[m
 ) < @searchDistance;[m
 [m
 [m
[32m+[m[32m-- CHECK IF THE ENTRIES ARE free from check in date to check out date.[m
[32m+[m[32mselect datediff('2019-07-30', '2019-07-27');[m
[32m+[m[32mSELECT Count(*) FROM Calendars WHERE ListingId = 3 AND IsAvailable = TRUE AND DayOfStay BETWEEN '2019-07-27' and subdate('2019-07-30', 1);[m
[32m+[m[32mselect last_insert_id();[m
[32m+[m[32mSET @ListingId = (SELECT Id FROM Listings WHERE Listings.Title = 'Studio (prés du Métro)');[m
[32m+[m[32mSET @CheckInDate = CURDATE();[m
[32m+[m[32mSet @CheckOutDate = ADDDATE(CURDATE(), 5);[m
[32m+[m[32mSet @NumDaysRequested = (SELECT datediff(@CheckOutDate, @CheckInDate));[m
[32m+[m[32mSet @NumDaysAvailable = (SELECT COUNT(*) FROM Calendars WHERE ListingId = @ListingId AND IsAvailable = TRUE AND DayOfStay BETWEEN @CheckInDate and SUBDATE(@CheckOutDate, 1));[m
[32m+[m[32mSELECT * FROM Calendars WHERE ListingId = @ListingId AND IsAvailable = TRUE AND DayOfStay BETWEEN @CheckInDate and SUBDATE(@CheckOutDate, 1);[m
[32m+[m[32mselect @CheckInDate, @CheckOutDate, @NumDaysRequested, @NumDaysAvailable;[m
[32m+[m
 [m
 -- Search for a listing by postal code[m
 SELECT * FROM Listings[m
[36m@@ -44,4 +56,15 @@[m [mSET @CheckOutDate = '2019-08-03';[m
 SELECT datediff('2019-08-03','2019-07-29') as a;[m
 Set @NumDaysRequested = (SELECT datediff(@CheckOutDate, @CheckInDate));[m
 SELECT @NumDaysRequested;[m
[31m-SELECT COUNT(*) AS DaysAvailable,ListingId FROM Calendars WHERE IsAvailable = TRUE AND DayOfStay BETWEEN @CheckInDate and SUBDATE(@CheckOutDate, 1) GROUP BY ListingId HAVING DaysAvailable = @NumDaysRequested;[m
\ No newline at end of file[m
[32m+[m[32mSELECT COUNT(*) AS DaysAvailable,ListingId FROM Calendars WHERE IsAvailable = TRUE AND DayOfStay BETWEEN @CheckInDate and SUBDATE(@CheckOutDate, 1) GROUP BY ListingId HAVING DaysAvailable = @NumDaysRequested;[m
[32m+[m
[32m+[m[32m-- Get all listingIds under a given price[m
[32m+[m[32mSET @MaxPrice = 50;[m
[32m+[m[32mSELECT Id FROM Listings WHERE BasePrice <= @MaxPrice;[m
[32m+[m
[32m+[m[32m-- Get all listingIds that have contain all of the given amenities[m
[32m+[m[32m-- SET @GivenAmenities = (1, 2, 3);[m
[32m+[m[32m-- SELECT @GivenAmenities;[m
[32m+[m[32m-- DO COUNT TRICK[m
[32m+[m[32mSET @GivenAmentiesSearchCount = 3;[m
[32m+[m[32mSELECT ListingId FROM ListingsAmenities WHERE AmenityId IN (1, 2, 3) GROUP BY(ListingId) HAVING COUNT(*) = @GivenAmentiesSearchCount;[m
\ No newline at end of file[m
