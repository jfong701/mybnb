package dbobjects;

public class Lister {

	public int Id;
	public String PaypalEmail;
	public int userSIN;
	
	public Lister(int id, String paypalEmail, int userSIN) {
		Id = id;
		PaypalEmail = paypalEmail;
		this.userSIN = userSIN;
	}
	
	public void printLister() {
		System.out.println("Id: " + Id);
		System.out.println("PaypalEmail: " + PaypalEmail);
		System.out.println("userSIN: " + userSIN);
	}
	
}
