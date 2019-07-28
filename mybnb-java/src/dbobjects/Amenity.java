package dbobjects;

public class Amenity {
	public int Id;
	public String AmenityName;
	public String AmenityDescription;
	public int AmenitycategoryId;
	
	public Amenity() {
	}

	public Amenity(int id, String amenityName, String amenityDescription, int amenitycategoryId) {
		Id = id;
		AmenityName = amenityName;
		AmenityDescription = amenityDescription;
		AmenitycategoryId = amenitycategoryId;
	}
}
