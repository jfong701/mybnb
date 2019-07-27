-- Countries (always the same)
LOAD DATA
	INFILE '../Uploads/Countries.txt'
	INTO TABLE Countries
	FIELDS TERMINATED BY '\t'
    IGNORE 1 LINES
	(CountryName)
	SET ID = NULL;
select * from countries;

-- Amenitycategories (always the same)
LOAD DATA
	INFILE '../Uploads/Amenitycategories.txt'
	INTO TABLE Amenitycategories
	FIELDS TERMINATED BY '\t'
    IGNORE 1 LINES
	(CategoryName)
	SET ID = NULL;
select * from Amenitycategories;

-- Amenities (always the same), linked to Amenitycategories
-- --------------------------------------------------------------------------------------------------------
SET @basicCategoryId = CAST((SELECT Id FROM Amenitycategories WHERE CategoryName='Basic') AS UNSIGNED);
INSERT INTO Amenities(AmenityName, AmenityDescription, AmenitycategoryId)
VALUES('Wifi', 'Continuous access in the listing', @basicCategoryId),
('Laptop-friendly workspace', 'A table or desk with space for a laptop and a chair that’s comfortable to work in', @basicCategoryId),
('Iron', NULL, @basicCategoryId),
('Dryer', 'In the building, free or for a fee', @basicCategoryId),
('Washer', 'In the building, free or for a fee', @basicCategoryId),
('Essentials', 'Towels, bed sheets, soap, and toilet paper', @basicCategoryId),
('Heating', 'Central heating or a heater in the listing', @basicCategoryId),
('TV', NULL, @basicCategoryId);

SET @familyFeaturesId = CAST((SELECT Id FROM Amenitycategories WHERE CategoryName='Family features') AS UNSIGNED);
INSERT INTO Amenities(AmenityName, AmenityDescription, AmenitycategoryId)
VALUES('High chair', NULL, @familyFeaturesId);

SET @facilitiesId = CAST((SELECT Id FROM Amenitycategories WHERE CategoryName='Facilities') AS UNSIGNED);
INSERT INTO Amenities(AmenityName, AmenityDescription, AmenitycategoryId)
VALUES('Paid parking off premises', NULL, @facilitiesId);

SET @diningId = CAST((SELECT Id FROM Amenitycategories WHERE CategoryName='Dining') AS UNSIGNED);
INSERT INTO Amenities(AmenityName, AmenityDescription, AmenitycategoryId)
VALUES('Kitchen', 'Space where guests can cook their own meals', @diningId),
('Coffee maker', NULL, @diningId),
('Cooking basics', 'Pots and pans, oil, salt and pepper', @diningId),
('Microwave', NULL, @diningId),
('Refrigerator', NULL, @diningId),
('Stove', NULL, @diningId)
;

SET @guestAccessId = CAST((SELECT Id FROM Amenitycategories WHERE CategoryName='Guest access') AS UNSIGNED);
INSERT INTO Amenities(AmenityName, AmenityDescription, AmenitycategoryId)
VALUES('Keypad', 'Check yourself into the home with a door code', @guestAccessId),
('Private entrance', 'Separate street or building entrance', @guestAccessId)
;

SET @logisticsId = CAST((SELECT Id FROM Amenitycategories WHERE CategoryName='Logistics') AS UNSIGNED);
INSERT INTO Amenities(AmenityName, AmenityDescription, AmenitycategoryId)
VALUES('Long-term stays allowed', 'Allow stay for 28 days or more', @logisticsId)
;

SET @bedAndBathId = CAST((SELECT Id FROM Amenitycategories WHERE CategoryName='Bed and bath') AS UNSIGNED);
INSERT INTO Amenities(AmenityName, AmenityDescription, AmenitycategoryId)
VALUES('Hangers', NULL, @bedAndBathId),
('Hair dryer', NULL, @bedAndBathId),
('Shampoo', NULL, @bedAndBathId),
('Lock on bedroom door', 'Private room which can be locked for safety and privacy', @bedAndBathId)
;

SET @safetyFeaturesId = CAST((SELECT Id FROM Amenitycategories WHERE CategoryName='Safety features') AS UNSIGNED);
INSERT INTO Amenities(AmenityName, AmenityDescription, AmenitycategoryId)
VALUES('Fire extinguisher', NULL, @safetyFeaturesId),
('Carbon monoxide detector', NULL, @safetyFeaturesId),
('Smoke detector', NULL, @safetyFeaturesId),
('First aid kit', NULL, @safetyFeaturesId)
;
-- --------------------------------------------------------------------------------------------------------

-- RoomTypes (always the same)
INSERT INTO RoomTypes(RoomtypeName, RoomtypeDescription)
VALUES('Entire home/apt', 'The entire listing is private. No facilities are shared.'),
('Private room', 'The listing has a private room, other facilities may be shared (eg shared washroom, or shared laundry facilities if available)'),
('Shared room', 'All rooms are shared with the homeowner and/or other guests')
;

---------------------------------------------
-- Create a User
SET @USAId = CAST((SELECT Id FROM Countries WHERE CountryName = 'United States') AS UNSIGNED);
SET @UKId = CAST((SELECT Id FROM Countries WHERE CountryName = 'United Kingdom') AS UNSIGNED);
SET @NZId = CAST((SELECT Id FROM Countries WHERE CountryName = 'New Zealand') AS UNSIGNED);
SET @JapanId = CAST((SELECT Id FROM Countries WHERE CountryName = 'Japan') AS UNSIGNED);
SET @FranceId = CAST((SELECT Id FROM Countries WHERE CountryName = 'France') AS UNSIGNED);
SET @CanadaId = CAST((SELECT Id FROM Countries WHERE CountryName = 'Canada') AS UNSIGNED);
INSERT INTO Users(UserSIN, Email, FirstName, LastName, Occupation, Address, City, DOB, CountryId)
VALUES
(63, 'km@m.com', 'Kurtis', 'Maceur', 'Marketing Manager', '7955 Badeau Drive', 'San Diego', '1994-02-24', @USAId),
(66, 'lh@h.com', 'Lurette', 'Habbershon', 'Legal Assistant', '19 Russell Lane', 'London', '1954-08-29', @UKId),
(53, 'vg@g.com', 'Vinnie', 'Goodridge', 'Help Desk Operator', '52427 Pennsylvania Park', 'Dunedin', '1970-10-04', @NZId),
(56, 'jc@c.com', 'Juliana', 'Cossins', 'Quality Control Specialist', '5709 Dorton Center', 'Kyoto', '1986-09-03', @JapanId),
(33, 'ls@s.com', 'Luke', 'Sandeford', 'Accounting Assistant I', '40324 Lien Drive', 'Sable-sur-Sarthe', '1998-01-03', @FranceId),
(15, 'rh@h.com', 'Richard', 'Hawkins', 'Senior Developer', '811 Victoria Crossing', 'Montréal', '1989-07-01', @CanadaId)
;

-- Choose Listers: Kurtis, Lurette, Richard
-- Choose Renters: Vinnie, Juliana, Richard
-- Neither one: Luke

-- set up paypal accounts
INSERT INTO Paypals(Email, AccountHolderName)
SELECT Email, CONCAT(FIRSTNAME, ' ', LASTNAME)
FROM Users
WHERE UserSIN IN (63, 66, 15);

-- set up listers
INSERT INTO Listers(PaypalEmail, UserSIN)
SELECT Email, UserSIN
FROM Users
WHERE UserSIN IN (63, 66, 15);

-- set up creditcards
INSERT INTO Creditcards(CardNumber, ExpiryDate, AccountHolderName)
VALUES
(1234123412341234123, '2020-08-05', 'Vinnie Goodridge'),
(4321432143214321, '2021-08-05', 'Juliana Cossins'),
(1867186718671867, '2020-08-05', 'Richard Hawkins');

-- set up renters
INSERT INTO Renters(CreditcardNumber, UserSIN)
SELECT CardNumber, UserSIN
FROM Creditcards AS C
LEFT JOIN Users AS U ON C.AccountHolderName = CONCAT(U.FirstName, ' ', U.LastName);

-- set up a listing
INSERT INTO
Listings(Title, ListingDescription, BasePrice, Latitude, Longitude, City, PostalCode, Address, CheckInTime, CheckOutTime, MaxNumGuests, CountryId, RoomTypeId, ListerId)
VALUES('Beautiful home in the Gaslamp quarter.', 'Ideally located home close to transit, and historic sights, here is some other stuff', 245, 32.713173, -117.161085, 'San Diego', 'CA 92101', '700 Fourth Ave', '16:00', '10:00', 4, @UsaId, 1, 1),
('Private apartment in downtown.', 'Conveniently located close to downtown, close to many tourist attractions', 159, 32.719834, -117.165591, 'San Diego', 'CA 92101', '1300 Union St', '12:00', '10:00', 6, @UsaId, 1, 1),
('Studio (prés du Métro)', '8 minute marche du métro Viau, à proximité se trouve le stade Olympique.', 48, 45.563018, -73.547782, 'Montréal', 'H1V 1A1', '2757 Rue Leclaire', '15:00', '11:00', 2, @CanadaId, 1, 1);

-- add All the odd amenities to the listings, except listing 1, give it all the amenities
INSERT INTO ListingsAmenities(ListingId, AmenityId)
SELECT Listings.Id, Amenities.Id
FROM Listings,Amenities
WHERE Amenities.Id % 2 = 1 OR Listings.Id = 1;

-- Make a booking in the first listings, for the current day to the next 5 days.
-- USE a transaction since we are updating multiple tables at once.

-- The application would give us a start date

-- DO IT ALL IN A TRANSACTION SINCE WE ARE UPDATING MULTIPLE TABLES AT ONCE, ENSURE DATA CONSISTENCY IN CASE ANYTHING GOES WRONG

START TRANSACTION;
-- Say Renter 'Vinnie' is staying at 'Richard's place
SET @ListingId = (SELECT Id FROM Listings WHERE Listings.Title = 'Studio (prés du Métro)');
SET @RenterId = (SELECT Id FROM Renters WHERE UserSIN = 53);
SET @ListerId = (SELECT Id FROM Listers WHERE UserSIN = 15);
SET @CheckInDate = CURDATE();
Set @CheckOutDate = ADDDATE(CURDATE(), 5);
Set @NumDaysRequested = (SELECT datediff(@CheckOutDate, @CheckInDate));
Set @NumDaysAvailable = (SELECT COUNT(*) FROM Calendars WHERE ListingId = @ListingId AND IsAvailable = TRUE AND DayOfStay BETWEEN @CheckInDate and SUBDATE(@CheckOutDate, 1));

-- create the booking
INSERT INTO Bookings(Amount, ProcessedOn, PaypalEmail, CreditcardNumber, ListingId, RenterId)
	SELECT
	(SELECT BasePrice*5 FROM Listings WHERE Listings.Id = @ListingId),
	CURDATE(),
	(SELECT PaypalEmail FROM Paypals LEFT JOIN Listers on Paypals.Email = Listers.PaypalEmail WHERE Listers.Id = @ListerId),
	(SELECT CardNumber FROM Creditcards LEFT JOIN Renters on Creditcards.CardNumber = Renters.CreditcardNumber WHERE Renters.Id = @RenterId),
	@ListingId,
	@RenterId
WHERE @NumDaysRequested = @NumDaysAvailable;

SET @BookingId = LAST_INSERT_ID();

-- mark the days in the Calendar
-- if the update was successful, then BookingId will exist.
UPDATE Calendars
SET
IsAvailable = false, BookingId = @BookingId
WHERE ListingId = @ListingId AND IsAvailable = TRUE AND DayOfStay BETWEEN @CheckInDate and SUBDATE(@CheckOutDate, 1);
COMMIT;
-- LOAD DATA 
-- 	INFILE '../Uploads/Paypals.txt'
-- 	INTO TABLE Paypals
--     FIELDS TERMINATED BY '\t'
--     IGNORE 1 LINES;
-- select * from paypals;

-- LOAD DATA
-- 	INFILE '../Uploads/User-attempt-1.txt'
--     INTO TABLE Users
--     FIELDS TERMINATED BY '\t'
--     IGNORE 1 LINES;
-- SELECT * FROM Users

