package dbobjects;

public class RoomType {
	public int Id;
	public String RoomtypeName;
	public String RoomtypeDescription;

	public RoomType(int id, String roomtypeName, String roomtypeDescription) {
		Id = id;
		RoomtypeName = roomtypeName;
		RoomtypeDescription = roomtypeDescription;
	}

	public void printRoomTypeFlat() {
		System.out.println("Id: " + this.Id + ", Room Type: " + this.RoomtypeName + ", Description: " + this.RoomtypeDescription);
	}
	
	public void printRoomTypeFlatFriendly() {
		System.out.println("Id: " + this.Id + ", : " + this.RoomtypeName + ", Description: " + this.RoomtypeDescription);
	}

}
