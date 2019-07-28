package jdbc;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

import dbobjects.Creditcard;
import dbobjects.Lister;
import dbobjects.Listing;
import dbobjects.Paypal;
import dbobjects.Renter;
import dbobjects.User;

public class View {

	// fields in View represent our active state
	public String viewName;
	public User loggedInUser = null;
	public Search search = new Search();
	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public View(String viewName) {
		this.viewName = viewName;
	}

	public void printMenu() {
		if (loggedInUser == null) {
			System.out.print("Not logged in: ");
		} else {
			System.out.print("(" + loggedInUser.FirstName + " " + loggedInUser.LastName + ")");
		}
		if (this.viewName.equals("INITIALSCREEN")) {
			System.out.println("=========INITIAL SCREEN=========");
			System.out.println("0. Exit.");
			System.out.println("1. Log in to an existing user");
			System.out.println("2. Register as a new user");
			System.out.println("3. Proceed as guest");
			System.out.print("Choose one of the previous options [0-3]: ");
		} else if (this.viewName.equals("MAINSCREEN")) {
			System.out.println("=========MAIN SCREEN=========");
			System.out.println("0. Exit.");
			System.out.println("1. Log in to an existing user");
			System.out.println("2. Search for listings");
			System.out.println("3. View your listings (listers only)");
			System.out.println("4. View your bookings (renters only)");
			System.out.println("5. Admin Panel");
			System.out.println("6. Become a renter");
			System.out.println("7. Become a lister");
			System.out.println("8. Back to login screen");
			System.out.print("Choose one of the previous options [0-8]: ");
		} else if (this.viewName.equals("LISTINGSCREEN")) {
			System.out.println("TODO: LISTINGSCREEN NOT IMPLEMENTED YET");
		} else if (this.viewName == "BOOKINGSSCREEN") {
			System.out.println("TODO: BOOKINGS SCREEN NOT IMPLEMENTED YET");
		} else if (this.viewName.equals("BECOMEARENTERSCREEN")) {
			System.out.println("=========BECOME A RENTER SCREEN=========");
			System.out.println("0. Exit.");
			System.out.println("1. Become a renter");
			System.out.println("2. Back to main screen");
			System.out.print("Choose one of the previous options [0-2]: ");
		} else if (this.viewName.equals("BECOMEALISTERSCREEN")) {
			System.out.println("=========BECOME A LISTER SCREEN=========");
			System.out.println("0. Exit.");
			System.out.println("1. Become a lister");
			System.out.println("2. Back to main screen");
			System.out.print("Choose one of the previous options [0-2]: ");
		} else if (this.viewName.equals("SEARCHOPTIONSSCREEN")) {
			System.out.println("=========SEARCH OPTIONS SCREEN=========");
			System.out.println("0. Exit.");
			System.out.println("1. Search by location");
			System.out.println("2. Search by postal code");
			System.out.println("3. Search by address");
			System.out.println("4. Search by city");
			System.out.println("5. Back to main screen");
			System.out.print("Choose one of the previous options [0-5]: ");
		} else if (this.viewName.equals("SEARCHOPTIONSSCREEN2")) {
			System.out.println("=========SEARCH OPTIONS SCREEN (refinements) =========");
			System.out.println("0. Exit.");
			System.out.println("1. Refine search with dates units are available");
			System.out.println("2. No more filters, show me what's there");
			System.out.print("Choose one of the previous options [0-2]: ");
		}
	}

	public String choiceAction(int choice, Scanner sc, DAO dao) {
		String input = null;
		if (this.viewName == "INITIALSCREEN") {
			switch (choice) { // Activate the desired functionality
			case 1:
				// log into existing
				System.out.print("Enter an email address: ");
				loggedInUser = new User();
				this.loggedInUser = dao.getUserByEmail(sc.nextLine());
				if (this.loggedInUser == null) {
					System.out.println("User not found, please check that email is valid.");
				} else {
					System.out.println("Welcome " + loggedInUser.FirstName + " " + loggedInUser.LastName);
				}
				break;
			case 2:
				// register new user
				System.out.println("Welcome new user! Please enter the data at the prompts.");
				User tempUser = new User();
				System.out.print("Enter SIN: ");
				input = sc.nextLine();
				tempUser.UserSIN = Integer.parseInt(input);

				System.out.print("Enter Email: ");
				tempUser.Email = sc.nextLine();

				System.out.print("Enter FirstName: ");
				tempUser.FirstName = sc.nextLine();

				System.out.print("Enter LastName: ");
				tempUser.LastName = sc.nextLine();

				System.out.print("Enter Occupation: ");
				tempUser.Occupation = sc.nextLine();

				System.out.print("Enter Address: ");
				tempUser.Address = sc.nextLine();

				System.out.print("Enter City: ");
				tempUser.City = sc.nextLine();

				System.out.print("Enter DOB in YYYY-MM-DD: ");
				input = sc.nextLine();
				if (input != null && input.trim().length() > 0) {
					try {
						java.util.Date parsed = dateFormat.parse(input);
						tempUser.DOB = new java.sql.Date(parsed.getTime());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				System.out.print("Enter Country: ");
				input = sc.nextLine();
				while (dao.getCountryIdByCountryName(input.trim()) == dao.INVALID_ID) {
					System.out.println("Please enter a valid country:");
					input = sc.nextLine();
				}
				tempUser.CountryId = dao.getCountryIdByCountryName(input.trim());

				tempUser.printUser();
				if (dao.addUser(tempUser)) {
					System.out.println("User successfully added.");
					System.out.println("Please choose option 1 on the next screento log in to your new account");
					return "INITIALSCREEN";
				} else {
					System.out.println("Error adding user");
				}
				break;
			case 3:
				// log in as guest
				loggedInUser = null;
				break;
			default:
				break;
			}
			return "MAINSCREEN";
		} else if (this.viewName == "MAINSCREEN") {
			switch (choice) { // Activate the desired functionality
			case 1:
				// log in screen
				return "INITIALSCREEN";
			case 2:
				// Search screen
				return "SEARCHOPTIONSSCREEN";
			case 3:
				// View your listings
				// only allowed if logged in, and a lister.
				if (this.loggedInUser == null) {
					System.out.println("You are not currently logged in. Please log in to see your listings.");
				} else if (!dao.isUserALister(this.loggedInUser.UserSIN)) {
					System.out.println("You are logged in, but not a lister. Please become a lister first.");
				} else {
					return "LISTINGSCREEN";
				}
				break;
			case 4:
				// View your bookings
				// Only allowed if logged in, and a renter
				if (this.loggedInUser == null) {
					System.out.println("You are not currently logged in. Please log in to see your bookings.");
				} else if (!dao.isUserARenter(this.loggedInUser.UserSIN)) {
					System.out.println("You are logged in, but not a renter. Please become a renter first.");
				} else {
					return "BOOKINGSCREEN";
				}
				break;
			case 5:
				// Go straight to the admin panel - forgot to add extra permissions in DB, would
				// probably have added this as a separate flag in users, or as a different kind
				// of user

				break;
			case 6:
				// Become a renter
				// let the 'become a renter' screen handle not logged in users
				return "BECOMEARENTERSCREEN";
			case 7:
				// become a lister
				// let the 'become a lister' screen handle not logged in users
				return "BECOMEALISTERSCREEN";
			case 8:
				// go back to initial screen - where user can log out.
				return "INITIALSCREEN";
			default:
				break;
			}
			return "MAINSCREEN";
		} else if (this.viewName == "BECOMEARENTERSCREEN") {
			switch (choice) { // Activate the desired functionality
			case 1:
				// become a renter -> ensure logged in, and ensure not a renter already then ask
				// for prompts.
				Renter tempRenter = null;
				Creditcard tempCard = null;
				if (this.loggedInUser != null && !dao.isUserARenter(this.loggedInUser.UserSIN)) {
					tempCard = new Creditcard();

					// add a credit card first					
					System.out.print("Enter a credit card number: ");
					input = sc.nextLine();
					tempCard.CardNumber = input.trim();
					
					System.out.print("Enter the card's expiry date YYYY-MM-DD: ");
					input = sc.nextLine();
					if (input != null && input.trim().length() > 0) {
						try {
							java.util.Date parsed = dateFormat.parse(input);
							tempCard.ExpiryDate = new java.sql.Date(parsed.getTime());
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					
					System.out.print("Enter the card holder's name: ");
					input = sc.nextLine();
					tempCard.AccountHolderName = input.trim();
					
					// save this card to the DB.
					if (dao.addCreditcard(tempCard)) {
						System.out.println("Card added successfully");
						
						// using this credit card, add the renter.
						tempRenter = new Renter(
								tempCard.CardNumber,
								this.loggedInUser.UserSIN
								);
						if (dao.addRenter(tempRenter)) {
							System.out.println("Account successfully set as renter");
							return "MAINSCREEN";
						} else {
							System.out.println("error setting as renter");
						}
					} else {
						System.out.println("error adding credit card");
					}

				} else if (this.loggedInUser == null) {
					System.out.println("Log into a user first to become a renter");
					return "INITIALSCREEN";
				} else {
					System.out.println("User is already a renter");
					return "MAINSCREEN";
				}
			case 2:
				// Go back to main screen
				return "MAINSCREEN";
			default:
				break;
			}
			// go back to mainscreen when done everything
			return "MAINSCREEN";
		} else if (this.viewName == "BECOMEALISTERSCREEN") {
			switch (choice) {
			case 1:
				// become a lister -> ensure logged in, and ensure not a lister already then ask
				// for prompts.
				Lister tempLister = null;
				Paypal tempPaypal = null;
				if (this.loggedInUser != null && !dao.isUserALister(this.loggedInUser.UserSIN)) {
					tempPaypal = new Paypal();

					// add a credit card first					
					System.out.print("Enter a Paypal email: ");
					input = sc.nextLine();
					tempPaypal.Email = input.trim();
					
					System.out.print("Enter the paypal account holder's name: ");
					input = sc.nextLine();
					tempPaypal.AccountHolderName = input.trim();
					
					// save this paypal to the DB.
					if (dao.addPaypal(tempPaypal)) {
						System.out.println("Paypal added successfully");
						
						// using this credit card, add the renter.
						tempLister = new Lister(
								tempPaypal.Email,
								this.loggedInUser.UserSIN
								);
						if (dao.addLister(tempLister)) {
							System.out.println("Account successfully set as lister");
							return "MAINSCREEN";
						} else {
							System.out.println("error setting as lister");
						}
					} else {
						System.out.println("error adding paypal");
					}

				} else if (this.loggedInUser == null) {
					System.out.println("Log into a user first to become a lister");
					return "INITIALSCREEN";
				} else {
					System.out.println("User is already a lister");
					return "MAINSCREEN";
				}
			case 2:
				// Go back to main screen
				return "MAINSCREEN";
			default:
				break;
			}
			// go back to mainscreen when done everything
			return "MAINSCREEN";
		} else if (this.viewName == "SEARCHOPTIONSSCREEN") {
			
			// reset the search if there are any leftovers from the last search
			search.resetSearch();
			switch (choice) {
			case 1:
				// search by location
				System.out.print("Enter the latitude of a location to start searching from: ");
				input = sc.nextLine();
				double latitude = Double.parseDouble(input);
				
				System.out.print("Enter the longitude of a location to start searching from: ");
				input = sc.nextLine();
				double longitude = Double.parseDouble(input);
				
				System.out.print("Enter a searchRadius in km: ");
				input = sc.nextLine();
				double searchRadius = Double.parseDouble(input);

				search.addCondition(search.generateLocationSearchCond(latitude, longitude, searchRadius));
				search.searchResult = dao.getListingsCustomSearch(search);
				return "SEARCHOPTIONSSCREEN2";
			case 2:
				// search by postal code
				System.out.print("Enter the starting part of a postal code to start searching for: ");
				input = sc.nextLine().trim();
				search.addCondition(search.generatePostalCodeSearchCond(input));
				search.searchResult = dao.getListingsCustomSearch(search);
				return "SEARCHOPTIONSSCREEN2";
			case 3:
				// search by exact address
				System.out.print("Enter an exact address to search for: ");
				input = sc.nextLine().trim();
				search.addCondition(search.generateExactAddressSearchCond(input));
				search.searchResult = dao.getListingsCustomSearch(search);
				return "SEARCHOPTIONSSCREEN2";
			case 4:
				// search by city name
				System.out.print("Enter a city name: ");
				input = sc.nextLine().trim();
				search.addCondition(search.generateCitySearchCond(input));
				search.searchResult = dao.getListingsCustomSearch(search);
				return "SEARCHOPTIONSSCREEN2";
			case 5:
				// back to main screen
				return "MAINSCREEN";
			default:
				break;
			}
			return "SEARCHOPTIONSSCREEN";
		} else if (this.viewName == "SEARCHOPTIONSSCREEN2") {
			switch (choice) {
			case 1:
				// add the temporal filter (dates)
				// we can assume there is an initial filter for location, so check db for listing Ids that are valid in this range
				// and only keep listingIds that are valid in the time range and are present in the original search.
				System.out.print("Enter the day you want to check IN in the form YYYY-MM-DD: ");
				input = sc.nextLine();
				Date CheckInDate = null; 
				if (input != null && input.trim().length() > 0) {
					try {
						java.util.Date parsed = dateFormat.parse(input);
						CheckInDate = new java.sql.Date(parsed.getTime());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				
				System.out.print("Enter the day you want to check OUT in the form YYYY-MM-DD: ");
				input = sc.nextLine();
				Date CheckOutDate = null; 
				if (input != null && input.trim().length() > 0) {
					try {
						java.util.Date parsed = dateFormat.parse(input);
						CheckOutDate = new java.sql.Date(parsed.getTime());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				
				if (CheckInDate.getTime() > CheckOutDate.getTime()) {
					System.out.println("Error: Checkin date is after checkout date");
					return "SEARCHOPTIONSSCREEN";
				} else {
					// get the listing Ids with dates available in this range
					ArrayList<Integer> listingIdsAvailable = dao.getListingIdsAvailableByDateRange(CheckInDate, CheckOutDate);
					
					// filter the previous results on this list of ids.
					search.searchResult =
					(ArrayList<Listing>) search.searchResult.stream()
						.filter(listing -> listingIdsAvailable.contains(listing.Id))
						.collect(Collectors.toList());
				}
			case 2:
				// continue without refining
			default:
				
				// OUTPUT THE SEARCH RESULTS
				
				System.out.println("!!! SEARCH RESULTS: !!!");
				if (search.searchResult.size() == 0) {
					System.out.println("0 results meet the filter criteria");
				} else {
					for (Listing l : search.searchResult) {
						l.printListingSmallFlat();
					}
				}
				System.out.println("Type the id of a Listing to view its full details - type 0 to go back to SEARCH");
				input = sc.nextLine();
				int id = Integer.parseInt(input);
				if (id == 0) {
					return "SEARCHOPTIONSSCREEN";
				}
				Listing l = dao.getListingById(id);
				System.out.println("Full listing details: ");
				l.printListingFull();
				return "SEARCHOPTIONSSCREEN";
			}
		}
		return "";
	}

}
