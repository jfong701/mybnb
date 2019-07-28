package dbobjects;

public class Paypal {

	public String Email;
	public String AccountHolderName;
	
	public Paypal() {}
	
	public Paypal(String email, String accountHolderName) {
		Email = email;
		AccountHolderName = accountHolderName;
	}



	public void printPaypal() {
		System.out.println("Email: " + Email);
		System.out.println("AccountHolderName: " + AccountHolderName);
	}
}
