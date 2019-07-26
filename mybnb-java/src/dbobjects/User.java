package dbobjects;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {

	public int UserSIN;
	public String Email;
	public String FirstName;
	public String LastName;
	public String Occupation;
	public String Address;
	public String City;
	public Date DOB;
	public Timestamp CreatedAt;
	public Timestamp LastLoggedInAt;
	public Integer CountryId;

	public User() {
	}

	public User(int userSIN, String email, String firstName, String lastName, String occupation, String address,
			String city, Date dOB, Timestamp createdAt, Timestamp lastLoggedInAt, Integer countryId) {
		UserSIN = userSIN;
		Email = email;
		FirstName = firstName;
		LastName = lastName;
		Occupation = occupation;
		Address = address;
		City = city;
		DOB = DOB;
		CreatedAt = createdAt;
		LastLoggedInAt = lastLoggedInAt;
		CountryId = countryId;
	}

	public void printUser() {
		System.out.println("UserSIN: " + UserSIN);
		System.out.println("Email: " + Email);
		System.out.println("FirstName: " + FirstName);
		System.out.println("LastName: " + LastName);
		System.out.println("Occupation: " + Occupation);
		System.out.println("Address: " + Address);
		System.out.println("City: " + City);
		System.out.println("DOB: " + DOB);
		System.out.println("CreatedAt: " + CreatedAt);
		System.out.println("LastLoggedInAt: " + LastLoggedInAt);
		System.out.println("CountryId: " + CountryId);
	}

}
