package dbobjects;

import java.sql.Date;
import java.sql.Timestamp;;

public class Booking {
	public int Id;
	public double Amount;
	public Timestamp ProcessedOn;
	public Timestamp RefundedOn;
	public String PaypalEmail;
	public String CreditcardNumber;
	public Integer CancelledById;
	
	
	public Booking(int id, double amount, Timestamp processedOn, Timestamp refundedOn, String paypalEmail,
			String creditcardNumber, Integer cancelledById, int listingId, int renterId) {
		Id = id;
		Amount = amount;
		ProcessedOn = processedOn;
		RefundedOn = refundedOn;
		PaypalEmail = paypalEmail;
		CreditcardNumber = creditcardNumber;
		CancelledById = cancelledById;
		ListingId = listingId;
		RenterId = renterId;
	}
	public int ListingId;
	public int RenterId;
	
	public void printBookingFlat() {
		System.out.print("Id: " + Id + ", ");
		System.out.print("Amount: " + Amount + ", ");
		System.out.print("ProcessedOn: " + ProcessedOn + ", ");
		System.out.print("RefundedOn: " + RefundedOn + ", ");
		System.out.print("PaypalEmail: " + PaypalEmail + ", ");
		System.out.print("CreditcardNumber: " + CreditcardNumber + ", ");
		System.out.print("CancelledById: " + CancelledById);
		System.out.println();
	}
}
