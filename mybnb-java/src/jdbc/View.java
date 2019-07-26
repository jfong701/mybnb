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
			System.out.print("Logged in: (" + loggedInUser.FirstName + ")");
		}
		if (this.viewName == "INITIALSCREEN") {
			System.out.println("=========INITIAL SCREEN=========");
			System.out.println("0. Exit.");
			System.out.println("1. Log in to an existing user");
			System.out.println("2. Register as a new user");
			System.out.println("3. Proceed as guest");
			System.out.print("Choose one of the previous options [0-3]: ");
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
				;
				break;
			default:
				break;
			}
			return "INITIALSCREEN";
		}
		return "";
	}

}
