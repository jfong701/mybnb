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

