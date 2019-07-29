package dbobjects;

public class Lister {

	public int Id;
	public String PaypalEmail;
	public int userSIN;
	
	// when creating listers from DB info
	public Lister(int id, String paypalEmail, int userSIN) {
		Id = id;
		PaypalEmail = paypalEmail;
		this.userSIN = userSIN;
	}
	
	// When making a lister to add to the DB.
	public Lister(String paypalEmail, int userSIN) {
		PaypalEmail = paypalEmail;
		this.userSIN = userSIN;
	}
	
	public void printLister() {
		System.out.println("Id: " + Id);
		System.out.println("PaypalEmail: " + PaypalEmail);
		System.out.println("userSIN: " + userSIN);
	}
	
}
