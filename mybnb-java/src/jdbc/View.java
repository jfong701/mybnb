package jdbc;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import dbobjects.User;

public class View {
	
	// fields in View represent our active state
	public String viewName;
	public User loggedInUser = null;
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
		if (this.viewName == "INITIALSCREEN") {
			System.out.println("=========INITIAL SCREEN=========");
			System.out.println("0. Exit.");
			System.out.println("1. Log in to an existing user");
			System.out.println("2. Register as a new user");
			System.out.println("3. Proceed as guest");
			System.out.print("Choose one of the previous options [0-3]: ");
		} else if (this.viewName == "MAINSCREEN") {
			System.out.println("=========MAIN SCREEN=========");
			System.out.println("0. Exit.");
			System.out.println("1. Log in to an existing user");
			System.out.println("2. Search for listings");
			System.out.println("3. View your listings (listers only)");
			System.out.println("4. View your bookings (renters only)");
			System.out.println("5. Admin Panel");
			System.out.println("6. Become a renter");
			System.out.println("7. Become a lister");
			System.out.print("Choose one of the previous options [0-5]: ");
		} else if (this.viewName == "LISTINGSCREEN") {
			System.out.println("TODO: LISTINGSCREEN NOT IMPLEMENTED YET");
		}
	}
	
	public String choiceAction(int choice, Scanner sc, DAO dao) {
		if (this.viewName == "INITIALSCREEN") {
			switch (choice) { //Activate the desired functionality
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
			switch (choice) { //Activate the desired functionality
			case 1:
				// log in screen
				return "INITIALSCREEN";
			case 2:
				// Search screen
				return "SEARCHSCREEN";
			case 3:
				// View your listings
				// check if logged in + user is a lister
				if (this.loggedInUser == null) {
					System.out.println("You are not currently logged in. Please log in to see your listings.");
				} else if (dao.getListerByUserSIN(this.loggedInUser.UserSIN) == null) {
					System.out.println("You are logged in, but not a lister. Please become a lister first.");
				} else {
					return "LISTINGSCREEN";
				}
				break;
			case 4:
				// View your bookings
				// check if logged in + user is a renter (if they have no bookings, say they have no bookings)
				break;
			case 5:
				// Go straight to the admin panel - forgot to add extra permissions in DB, would
				// probably have added this as a separate flag in users, or as a different kind
				// of user

				break;
			default:
				break;
			}
			return "MAINSCREEN";
		}
		return "";
	}

}
