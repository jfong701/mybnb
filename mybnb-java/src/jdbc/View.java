package jdbc;
import java.util.Scanner;

import dbobjects.User;

public class View {
	
	// fields in View represent our active state
	public String viewName;
	public User loggedInUser = null;
	
	public View(String viewName) {
		this.viewName = viewName;
	}
	
	public void printMenu() {
		
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
				;
				break;
			case 3:
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
