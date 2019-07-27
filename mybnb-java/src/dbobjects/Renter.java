package dbobjects;

public class Renter {

	public int Id;
	public String CreditcardNumber;
	public int userSIN;
	
	public Renter(int id, String creditcardNumber, int userSIN) {
		Id = id;
		CreditcardNumber = creditcardNumber;
		this.userSIN = userSIN;
	}
	
	public void printRenter() {
		System.out.println("Id: " + Id);
		System.out.println("CreditcardNumber: " + CreditcardNumber);
		System.out.println("userSIN: " + userSIN);
	}
	
}
