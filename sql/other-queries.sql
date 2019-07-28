select * from Listings;
SET @LatitudeIn = 32.715658;
SET @LongitudeIn = -117.161116;
-- radius is in km.
SET @earthRadius = 6378.1;
SET @searchDistance = 0.6;
-- Search for listings within a specified distance of a longitude and latitude --> now stored as a function in create-table-script
SELECT * FROM Listings
WHERE (2*@earthRadius
	*ASIN(
		SQRT(
			POWER(
				SIN(
					(RADIANS(@LatitudeIn) - RADIANS(Latitude))/2
				)
			,2)
			+
			COS(RADIANS(Latitude))
			*
			COS(RADIANS(@LatitudeIn))
			*
			POWER(
				SIN(
					(RADIANS(@LongitudeIn) - RADIANS(Longitude))/2
				)
			,2)
		)
	)
) < @searchDistance;


-- CHECK IF THE ENTRIES ARE free from check in date to check out date.
select datediff('2019-07-30', '2019-07-27');
SELECT Count(*) FROM Calendars WHERE ListingId = 3 AND IsAvailable = TRUE AND DayOfStay BETWEEN '2019-07-27' and subdate('2019-07-30', 1);
select last_insert_id();
SET @ListingId = (SELECT Id FROM Listings WHERE Listings.Title = 'Studio (prés du Métro)');
SET @CheckInDate = CURDATE();
Set @CheckOutDate = ADDDATE(CURDATE(), 5);
Set @NumDaysRequested = (SELECT datediff(@CheckOutDate, @CheckInDate));
Set @NumDaysAvailable = (SELECT COUNT(*) FROM Calendars WHERE ListingId = @ListingId AND IsAvailable = TRUE AND DayOfStay BETWEEN @CheckInDate and SUBDATE(@CheckOutDate, 1));
SELECT * FROM Calendars WHERE ListingId = @ListingId AND IsAvailable = TRUE AND DayOfStay BETWEEN @CheckInDate and SUBDATE(@CheckOutDate, 1);
select @CheckInDate, @CheckOutDate, @NumDaysRequested, @NumDaysAvailable;


-- Search for a listing by postal code
SELECT * FROM Listings
WHERE
-- CountryId = (SELECT Id FROM Countries WHERE CountryName = 'Canada') AND
PostalCode LIKE CONCAT(@searchterm, '%') ;



-- Get Number of days available for each listing with the given date range
SET @CheckInDate = '2019-07-29';
SET @CheckOutDate = '2019-08-03';
SELECT datediff('2019-08-03','2019-07-29') as a;
Set @NumDaysRequested = (SELECT datediff(@CheckOutDate, @CheckInDate));
SELECT @NumDaysRequested;
SELECT COUNT(*) AS DaysAvailable,ListingId FROM Calendars WHERE IsAvailable = TRUE AND DayOfStay BETWEEN @CheckInDate and SUBDATE(@CheckOutDate, 1) GROUP BY ListingId HAVING DaysAvailable = @NumDaysRequested;

-- Get all listingIds under a given price
SET @MaxPrice = 50;
SELECT Id FROM Listings WHERE BasePrice <= @MaxPrice;

-- Get all listingIds that have contain all of the given amenities
-- SET @GivenAmenities = (1, 2, 3);
-- SELECT @GivenAmenities;
-- DO COUNT TRICK
SET @GivenAmentiesSearchCount = 3;
SELECT ListingId FROM ListingsAmenities WHERE AmenityId IN (1, 2, 3) GROUP BY(ListingId) HAVING COUNT(*) = @GivenAmentiesSearchCount;