package jdbc;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

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
			System.out.println("3. Search by adddress");
			System.out.println("4. Back to main screen");
			System.out.print("Choose one of the previous options [0-4]: ");
		}
	}

	public String choiceAction(int choice, Scanner sc, DAO dao) {
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
				String input = sc.nextLine();
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
					System.out.println("You are now logged in as: ");
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
					String input = sc.nextLine();
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
					String input = sc.nextLine();
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
			switch (choice) {
			case 1:
				// search by location
				System.out.print("Enter the latitude of a location to start searching from: ");
				String input = sc.nextLine();
				double latitude = Double.parseDouble(input);
				
				System.out.print("Enter the longitude of a location to start searching from: ");
				input = sc.nextLine();
				double longitude = Double.parseDouble(input);
				
				System.out.print("Enter a searchRadius in km: ");
				input = sc.nextLine();
				double searchRadius = Double.parseDouble(input);

				search.addCondition(search.generateLocationSearchCond(latitude, longitude, searchRadius));
				search.searchResult = dao.getListingsCustomSearch(search);
				System.out.println("SEARCH RESULTS:");
				for (Listing l : search.searchResult) {
					l.printListingSmallFlat();
				}
				System.out.println("Type the id of a Listing to view its full details");
				input = sc.nextLine();
				int id = Integer.parseInt(input);
				Listing l = dao.getListingById(id);
				System.out.println("Full listing details: ");
				l.printListingFull();
//				ArrayList<Listing>l = dao.getListingsCustomSearch(search);
//				for(Listing li : l) {
//					li.printListingSmall();
//				}
				return "SEARCHOPTIONSSCREEN";
			case 2:
				// search by postal code
				break;
			case 3:
				// search by address
				break;
			case 4:
				// back to main screen
				return "MAINSCREEN";
			default:
				break;
			}
			return "SEARCHOPTIONSSCREEN";
		}
		return "";
	}

}
