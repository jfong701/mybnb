package dbobjects;

import java.sql.Time;
import java.util.List;

import jdbc.DAO;

public class Listing {
	public int Id;
	public String Title;
	public String ListingDescription;
	public double BasePrice;
	public double Latitude;
	public double Longitude;
	public String City;
	public String PostalCode;
	public String Address;
	public Time CheckInTime;
	public Time CheckOutTime;
	public int MaxNumGuests;
	public int CountryId;
	public int RoomTypeId;
	public int ListerId;
	public double DistanceFromSearch;
	
	// decoration properties (retrieved FK for country, roomtype, lister)
	public String CountryName, RoomTypeName, ListerName;
	public RoomType RoomType;
	public List<Amenity> Amenities;
	
	
	public Listing() {}
	

	public Listing(int id, String title, String listingDescription, double basePrice, double latitude, double longitude,
			String city, String postalCode, String address, Time checkInTime, Time checkOutTime, int maxNumGuests,
			int countryId, int roomTypeId, int listerId) {
		Id = id;
		Title = title;
		ListingDescription = listingDescription;
		BasePrice = basePrice;
		Latitude = latitude;
		Longitude = longitude;
		City = city;
		PostalCode = postalCode;
		Address = address;
		CheckInTime = checkInTime;
		CheckOutTime = checkOutTime;
		MaxNumGuests = maxNumGuests;
		CountryId = countryId;
		RoomTypeId = roomTypeId;
		ListerId = listerId;
	}
	
	public void printListingSmall() {
		System.out.println("Id: " + Id);
		System.out.println("Title: " + Title);
		System.out.println("BasePrice: " + BasePrice);
		System.out.println("City: " + City);
		System.out.println("Postal Code: " + PostalCode);
		System.out.println("Address: " + Address);
		System.out.println("Max num of guests: " + MaxNumGuests);
		if (DistanceFromSearch != 0) {
			System.out.println("Distance: (km)" + DistanceFromSearch);
		}
	}
	
	public void printListingSmallFlat() {
		System.out.print("Id: " + Id + ", ");
		System.out.print("Title: " + Title + ", ");
		System.out.print("BasePrice: " + BasePrice + ", ");
		System.out.print("City: " + City + ", ");
		System.out.print("Postal Code: " + PostalCode + ", ");
		System.out.print("Address: " + Address + ", ");
		System.out.print("Max num of guests: " + MaxNumGuests + ", ");
		if (DistanceFromSearch != 0) {
			System.out.println("Distance in km: " + DistanceFromSearch);
		}
		System.out.println();
	}
	
	public void printListingFull() {		
		System.out.println("Id: " + Id);
		System.out.println("Title: " + Title);
		System.out.println("ListingDescription: " + ListingDescription);
		System.out.println("BasePrice: " + BasePrice);
		System.out.println("Latitude: " + Latitude);
		System.out.println("Longitude: " + Longitude);
		System.out.println("City: " + City);
		System.out.println("Postal Code: " + PostalCode);
		System.out.println("Address: " + Address);
		System.out.println("CheckInTime: " + CheckInTime);
		System.out.println("CheckOutTime: " + CheckOutTime);
		System.out.println("MaxNumGuests: " + MaxNumGuests);
		System.out.println("CountryName: " + CountryName);
		System.out.println("RoomType: " + RoomType.RoomtypeName);
		System.out.println("ListerName: " + ListerName);
		if (DistanceFromSearch != 0) {
			System.out.println("Distance: (km)" + DistanceFromSearch);
		}
		System.out.print("Amenities: ");
		for (int i = 0; i < this.Amenities.size(); i++) {
			System.out.print(this.Amenities.get(i).AmenityName);
			if (i < this.Amenities.size() - 1) {
				System.out.print(", ");
			}
		}
		System.out.println();
	}
	
	public void fillDecorative(DAO dao) {
		this.CountryName = dao.getCountryNameById(this.CountryId);
		this.RoomType = dao.getRoomTypeById(this.RoomTypeId);
		this.ListerName = dao.getListerNameByListerId(this.ListerId);
		this.Amenities = dao.getListingAmenitesByListingId(this.Id);
	}

}
