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
INSERT INTO Users(SIN, Email, FirstName, LastName, Occupation, Address, City, DOB, CountryId)
VALUES
(63, 'km@m.com', 'Kurtis', 'Maceur', 'Marketing Manager', '7955 Badeau Drive', 'San Diego', '1994-02-24', @USAId),
(66, 'lh@h.com', 'Lurette', 'Habbershon', 'Legal Assistant', '19 Russell Lane', 'London', '1954-08-29', @UKId),
(53, 'vg@g.com', 'Vinnie', 'Goodridge', 'Help Desk Operator', '52427 Pennsylvania Park', 'Dunedin', '1970-10-04', @NZId),
(56, 'jc@c.com', 'Juliana', 'Cossins', 'Quality Control Specialist', '5709 Dorton Center', 'Kyoto', '1986-09-03', @JapanId),
(33, 'ls@s.com', 'Luke', 'Sandeford', 'Accounting Assistant I', '40324 Lien Drive', 'Sable-sur-Sarthe', '1998-01-03', @FranceId)
;

-- Choose Renters: Kurtis, Lurette
-- Choose Listers: Vinnie, Juliana
-- Neither one: Luke

-- set up paypal accounts
INSERT INTO Paypals(Email, AccountHolderName)
SELECT Email, CONCAT(FIRSTNAME, ' ', LASTNAME)
FROM Users
WHERE SIN IN (63, 66);

-- set up listers
INSERT INTO Listers(PaypalEmail, UserSIN)
SELECT Email, SIN
FROM Users
WHERE SIN IN (63, 66);

-- set up creditcards
INSERT INTO Creditcards(CardNumber, ExpiryDate, AccountHolderName)
VALUES
(1234123412341234123, '2020-08-05', 'Vinnie Goodridge'),
(1234123412341234123, '2021-08-05', 'Juliana Cossins');

-- set up renters
INSERT INTO 





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

