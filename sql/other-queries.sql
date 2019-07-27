select * from Listings;
SET @LatitudeIn = 32.715658;
SET @LongitudeIn = -117.161116;
-- radius is in km.
SET @earthRadius = 6378.1;
SET @searchDistance = 0.6;
-- Search for listings within a specified distance of a longitude and latitude
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

-- Search for a listing by postal code
SELECT * FROM Listings
WHERE
-- CountryId = (SELECT Id FROM Countries WHERE CountryName = 'Canada') AND
PostalCode LIKE CONCAT(@searchterm, '%') ;