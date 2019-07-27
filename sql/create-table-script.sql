DROP DATABASE IF EXISTS mybnbdev;
CREATE DATABASE mybnbdev;

USE mybnbdev;

DROP TABLE IF EXISTS Countries CASCADE;
CREATE TABLE Countries (
    Id INTEGER NOT NULL AUTO_INCREMENT,
    CountryName VARCHAR(500) NOT NULL,

    PRIMARY KEY(Id)
);

DROP TABLE IF EXISTS Users CASCADE;
CREATE TABLE Users (
    UserSIN INTEGER NOT NULL,
    Email VARCHAR(320) NOT NULL,
    FirstName VARCHAR(1000) NOT NULL,
    LastName VARCHAR(1000) NOT NULL,
    Occupation VARCHAR(1000) NOT NULL,
    Address VARCHAR(1000) NOT NULL,
    City VARCHAR(1000) NOT NULL,
    DOB DATE NOT NULL,
    CreatedAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    LastLoggedInAt DATETIME,
    CountryId INTEGER NOT NULL,
--     ListerId INTEGER,
--     RenterId INTEGER,

    PRIMARY KEY (UserSIN),
    FOREIGN KEY(CountryId) REFERENCES Countries(Id) ON DELETE CASCADE,
--     FOREIGN KEY(ListerId) REFERENCES Listers(Id) ON DELETE CASCADE,
--     FOREIGN KEY(RenterId) REFERENCES Renters(Id) ON DELETE CASCADE,

    -- ensure unique email entries -> using this as a 'login' for now
    UNIQUE(Email),
    -- Some basic validation on the form of an email
    CHECK(EMAIL LIKE '%@%.%')
    -- Ensure User is 18 years old through trigger
);

DROP TABLE IF EXISTS Paypals CASCADE;
CREATE TABLE Paypals (
    Email VARCHAR(320) NOT NULL,
    AccountHolderName VARCHAR(500) NOT NULL,

    PRIMARY KEY(Email)
);

DROP TABLE IF EXISTS Creditcards CASCADE;
CREATE TABLE Creditcards (
    CardNumber VARCHAR(19) NOT NULL,
    ExpiryDate DATE NOT NULL,
    AccountHolderName VARCHAR(500) NOT NULL,

    PRIMARY KEY(CardNumber)
    -- expiry date check is done in Triggers
);


DROP TABLE IF EXISTS Listers CASCADE;
CREATE TABLE Listers (
    Id INT NOT NULL AUTO_INCREMENT,
    PaypalEmail VARCHAR(320) NULL,
    UserSIN INTEGER NOT NULL,
    
    PRIMARY KEY(Id),
    FOREIGN KEY(PaypalEmail) REFERENCES Paypals(Email) ON DELETE CASCADE,
    FOREIGN KEY(UserSIN) REFERENCES Users(UserSIN) ON DELETE CASCADE,
    UNIQUE(UserSIN)
);

DROP TABLE IF EXISTS Renters CASCADE;
CREATE TABLE Renters (
    Id INT NOT NULL AUTO_INCREMENT,
    CreditcardNumber VARCHAR(19) NULL,
    UserSIN INTEGER NOT NULL,

    PRIMARY KEY(Id),
    FOREIGN KEY(CreditcardNumber) REFERENCES Creditcards(CardNumber) ON DELETE CASCADE,
    FOREIGN KEY(UserSIN) REFERENCES Users(UserSIN) ON DELETE CASCADE,
    UNIQUE(UserSIN)
);

DROP TABLE IF EXISTS Roomtypes CASCADE;
CREATE TABLE Roomtypes (
	Id INTEGER NOT NULL auto_increment,
    RoomtypeName VARCHAR(150) NOT NULL,
    RoomtypeDescription VARCHAR(1000) NOT NULL, 
    
    PRIMARY KEY(Id),
    UNIQUE(RoomtypeName)
);

DROP TABLE IF EXISTS Listings CASCADE;
CREATE TABLE Listings (
    Id INTEGER NOT NULL auto_increment,
    Title VARCHAR(50) NOT NULL, -- this is the actual airbnb limit
    ListingDescription VARCHAR(1000) NOT NULL,
    BasePrice DECIMAL(19,4) NOT NULL,
    Latitude DECIMAL(9, 6) NOT NULL,
    Longitude DECIMAL(9, 6) NOT NULL,
    City VARCHAR(1000) NOT NULL,
    PostalCode VARCHAR(100) NOT NULL,
    Address VARCHAR(1000) NOT NULL,
    CheckInTime TIME NOT NULL,
    CheckOutTime TIME NOT NULL,
    MaxNumGuests SMALLINT UNSIGNED NOT NULL,
    CountryId INTEGER NOT NULL,
    RoomTypeId INTEGER NOT NULL,
    ListerId INTEGER NOT NULL,

    PRIMARY KEY(Id),
    FOREIGN KEY (CountryId) REFERENCES Countries(Id) ON DELETE CASCADE,
    FOREIGN KEY (RoomTypeId) REFERENCES Roomtypes(Id) ON DELETE CASCADE,
    FOREIGN KEY (ListerId) REFERENCES Listers(Id) ON DELETE CASCADE,

    -- Ensure Latitude and Longitude Values are within valid range
    CHECK(Latitude >= -90 AND Latitude <= 90),
    CHECK(Longitude >= -180 AND Longitude <= 180),

    -- Ensure CheckInTime is after CheckOutTime
    CHECK(CheckInTime >= CheckOutTime),
    
    -- Ensure num guests is positive
    CHECK(MaxNumGuests > 0 AND MaxNumGuests < 100)
);

DROP TABLE IF EXISTS Payments CASCADE;
CREATE TABLE Payments (
    Id INTEGER NOT NULL auto_increment,
    Amount DECIMAL(19, 4) NOT NULL,
    ProcessedOn DATETIME NOT NULL,
    RefundedOn DATETIME,
    PaypalEmail VARCHAR(320) NOT NULL,
    CreditcardNumber VARCHAR(16) NOT NULL,

    PRIMARY KEY(Id),
    FOREIGN KEY (PaypalEmail) REFERENCES Paypals(Email) ON DELETE CASCADE,
    FOREIGN KEY (CreditcardNumber) REFERENCES Creditcards(CardNumber) ON DELETE CASCADE,

    -- ensure transaction amounts are not negative.
    CHECK(Amount >= 0)
);

DROP TABLE IF EXISTS Bookings CASCADE;
CREATE TABLE Bookings (
    Id INTEGER NOT NULL auto_increment,
	Amount DECIMAL(19, 4) NOT NULL,
    ProcessedOn DATETIME NOT NULL,
    RefundedOn DATETIME,
    PaypalEmail VARCHAR(320) NOT NULL,
    CreditcardNumber VARCHAR(19) NOT NULL,
	CancelledById INTEGER,
    ListingId INTEGER NOT NULL,
    RenterId INTEGER NOT NULL,

    PRIMARY KEY(Id),
	FOREIGN KEY (PaypalEmail) REFERENCES Paypals(Email) ON DELETE CASCADE,
    FOREIGN KEY (CreditcardNumber) REFERENCES Creditcards(CardNumber) ON DELETE CASCADE,
    FOREIGN KEY (CancelledById) REFERENCES Users(UserSIN) ON DELETE CASCADE,
    FOREIGN KEY (ListingId) REFERENCES Listings(Id) ON DELETE CASCADE,
	FOREIGN KEY (RenterId) REFERENCES Renters(Id) ON DELETE CASCADE,
    
    
	-- ensure transaction amounts are not negative.
    CHECK(Amount >= 0)
);

DROP TABLE IF EXISTS Calendars CASCADE;
CREATE TABLE Calendars (
    Id INTEGER NOT NULL auto_increment,
    DayOfStay DATE NOT NULL,
    Price DECIMAL(19, 4) NOT NULL,
    IsAvailable BIT NOT NULL DEFAULT TRUE,
    ListingId INTEGER NOT NULL,
    BookingId INTEGER,

    PRIMARY KEY(Id),
    FOREIGN KEY (ListingId) REFERENCES Listings(Id) ON DELETE CASCADE,
    FOREIGN KEY (BookingId) REFERENCES Bookings(Id) ON DELETE CASCADE,

    -- ensure prices are positive
    CHECK(Price >= 0)
);

DROP TABLE IF EXISTS Amenitycategories CASCADE;
CREATE TABLE Amenitycategories (
    Id INTEGER NOT NULL auto_increment,
    CategoryName VARCHAR(100) NOT NULL,

    PRIMARY KEY(Id),
    UNIQUE(CategoryName)
);

DROP TABLE IF EXISTS Amenities CASCADE;
CREATE TABLE Amenities (
    Id INTEGER NOT NULL auto_increment,
    AmenityName VARCHAR(100) NOT NULL,
    AmenityDescription VARCHAR(250),
    AmenitycategoryId INTEGER NOT NULL,

    PRIMARY KEY(Id),
    FOREIGN KEY (AmenitycategoryId) REFERENCES Amenitycategories(Id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS ListingsAmenities CASCADE;
CREATE TABLE ListingsAmenities (
    Id INTEGER NOT NULL auto_increment,
    ListingId INTEGER NOT NULL,
    AmenityId INTEGER NOT NULL,

    PRIMARY KEY(Id),
    FOREIGN KEY (ListingId) REFERENCES Listings(Id) ON DELETE CASCADE,
    FOREIGN KEY (AmenityId) REFERENCES Amenities(Id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS Listingcomments CASCADE;
CREATE TABLE Listingcomments (
    Id INTEGER NOT NULL auto_increment,
    CommentMessage VARCHAR(6000) NOT NULL,
    CommentedOn DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ListingId INTEGER NOT NULL,
    RenterId INTEGER NOT NULL,

    PRIMARY KEY(Id),
    FOREIGN KEY (ListingId) REFERENCES Listings(Id) ON DELETE CASCADE,
    FOREIGN KEY (RenterId) REFERENCES Renters(Id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS Listingratings CASCADE;
CREATE TABLE Listingratings (
    Id INTEGER NOT NULL auto_increment,
    Rating SMALLINT UNSIGNED NOT NULL,
    RatedOn DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ListingId INTEGER NOT NULL,
    RenterId INTEGER NOT NULL,

    PRIMARY KEY(Id),
    FOREIGN KEY (ListingId) REFERENCES Listings(Id) ON DELETE CASCADE,
    FOREIGN KEY (RenterId) REFERENCES Renters(Id) ON DELETE CASCADE,

    CHECK (Rating >=1 AND Rating <= 5)
);

DROP TABLE IF EXISTS Rentercomments CASCADE;
CREATE TABLE Rentercomments (
    Id INTEGER NOT NULL auto_increment,
    CommentMessage VARCHAR(6000) NOT NULL,
    CommentedOn DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    RenterId INTEGER NOT NULL,
    ListerId INTEGER NOT NULL,

    PRIMARY KEY(Id),
    FOREIGN KEY (RenterId) REFERENCES Renters(Id) ON DELETE CASCADE,
    FOREIGN KEY (ListerId) REFERENCES Listers(Id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS Renterratings CASCADE;
CREATE TABLE Renterratings (
    Id INTEGER NOT NULL auto_increment,
    Rating SMALLINT UNSIGNED NOT NULL,
    RatedOn DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    RenterId INTEGER NOT NULL,
    ListerId INTEGER NOT NULL,

    PRIMARY KEY(Id),
    FOREIGN KEY (RenterId) REFERENCES Renters(Id) ON DELETE CASCADE,
    FOREIGN KEY (ListerId) REFERENCES Listers(Id) ON DELETE CASCADE,

    CHECK (Rating >=1 AND Rating <= 5)
);

-- TRIGGER 'CHECKS', Mostly time-based checks are here, since
-- MYSQL CHECK in the CREATE TABLE no longer works with
-- non-deterministic functions such as CURDATE() as of mysql 8.0.16
-- as a result, a lot of the time-based checks are done here

-- Checking if Users are at least 18 to be added to Users
DELIMITER |
CREATE TRIGGER dob_check BEFORE INSERT ON Users
    FOR EACH ROW
    BEGIN
        IF TIMESTAMPDIFF(YEAR, NEW.DOB, CURDATE()) <= 18 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: Cannot Create User, User is under 18';
        END IF;
    END
|
DELIMITER ;

-- Checking if Credit Card used is not expired yet
DELIMITER |
CREATE TRIGGER cc_expiry_check BEFORE INSERT ON Creditcards
    FOR EACH ROW
    BEGIN
        IF TIMESTAMPDIFF(MONTH, CURDATE(), NEW.ExpiryDate) < 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: Credit card is already expired';
        END IF;
    END
|
DELIMITER ;

-- ACTUAL TRIGGERS
-- When a listing is created, Set it as available for the next 3 months for calendar entries.
-- TODO WIP : ONLY creates one entry at the moment, will figure out how to make it loop, once it is tested on one entry
SET @x = 0;
DELIMITER |
CREATE TRIGGER generate_calendar_entries AFTER INSERT ON Listings
	FOR EACH ROW
    BEGIN
		WHILE @x <= 90 DO
			INSERT INTO Calendars(DayOfStay, Price, IsAvailable, ListingId, BookingId) VALUES(ADDDATE(CURDATE(), @x), NEW.BasePrice, TRUE, NEW.Id, NULL);
            SET @x = @x + 1;
		END WHILE;
        SET @x = 0;
    END
|
DELIMITER ;

DELIMITER |
CREATE FUNCTION Haversine(LatitudeIn DECIMAL(9,6), LongitudeIn DECIMAL(9,6), LatitudeIn2 DECIMAL(9,6), LongitudeIn2 DECIMAL(9,6)) RETURNS DECIMAL(9,6) DETERMINISTIC
BEGIN
RETURN (2*6378.1
	*ASIN(
		SQRT(
			POWER(
				SIN(
					(RADIANS(LatitudeIn) - RADIANS(LatitudeIn2))/2
				)
			,2)
			+
			COS(RADIANS(LatitudeIn2))
			*
			COS(RADIANS(LatitudeIn))
			*
			POWER(
				SIN(
					(RADIANS(LongitudeIn) - RADIANS(LongitudeIn2))/2
				)
			,2)
		)
	)
);
END
|
DELIMITER ;