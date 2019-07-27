package dbobjects;

import java.sql.Date;

public class Creditcard {

	public String CardNumber;
	public Date ExpiryDate;
	public String AccountHolderName;
	
	public Creditcard() {}
	
	public Creditcard(String cardNumber, Date expiryDate, String accountHolderName) {
		CardNumber = cardNumber;
		ExpiryDate = expiryDate;
		AccountHolderName = accountHolderName;
	}



	public void printCreditcard() {
		System.out.println("CardNumber: " + CardNumber);
		System.out.println("ExpiryDate: " + ExpiryDate);
		System.out.println("AccountHolderName: " + AccountHolderName);
	}
}
