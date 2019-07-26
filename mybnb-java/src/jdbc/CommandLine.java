package jdbc;
import java.sql.SQLException;
import java.util.Scanner;

public class CommandLine {
	
    // 'sc' is needed in order to scan the inputs provided by the user
	private Scanner sc = null;
	
	private DAO dao = null;
	
	public boolean startSession() {
		boolean success = true;
		if (sc == null) {
			sc = new Scanner(System.in);
		}
		if (dao == null) {
			try {
				dao = new DAO();
			} catch (SQLException e) {
				success = false;
				e.printStackTrace();
			}
		}
		return success;
	}
	
	public void endSession() {
		if (dao != null) {
			dao.endDAO();
		}
		if (sc != null) {
			sc.close();
		}
		dao = null;
		sc = null;
	}
	
	/* Function that executes an infinite loop and activates the respective 
     * functionality according to user's choice. At each time it also outputs
     * the menu of core functionalities supported from our application.
     */
	public boolean execute() {
		if (sc != null && dao != null) {
			System.out.println("");
			System.out.println("***************************");
			System.out.println("******ACCESS GRANTED*******");
			System.out.println("***************************");
			System.out.println("");
			
			String input = "";
			int choice = -1;
			boolean exit = false;
			String accountEmail = "";
			do {
				loginScreen(); //Print Menu
				input = sc.nextLine();
				try {
					choice = Integer.parseInt(input);
					switch (choice) { //Activate the desired functionality
					case 1:
						dao.PrintResultSetOutput(dao.getAllAmenities());
						break;
					default:
						break;
					}
				} catch (NumberFormatException e) {
					input = "-1";
				}
			} while (input.compareTo("0") != 0);
			
			return true;
		} else {
			System.out.println("");
			System.out.println("Connection could not been established! Bye!");
			System.out.println("");
			return false;
		}
	}
	
	//Print menu options
	private static void loginScreen() {
		System.out.println("=========LOGIN SCREEN=========");
		System.out.println("0. Exit.");
		System.out.println("1. Get all amenities");
		System.out.println("2. Register as a new user");
		System.out.println("3. Proceed as guest");
		System.out.print("Choose one of the previous options [0-3]: ");
	}
}
