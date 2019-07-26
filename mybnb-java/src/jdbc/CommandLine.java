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
			
			View v = new View("INITIALSCREEN");
			
			do {
				v.printMenu();
				input = sc.nextLine();
				try {
					choice = Integer.parseInt(input);
					v.viewName = v.choiceAction(choice, sc, dao);
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
}
