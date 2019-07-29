-----------------------------------------------------------------------------
-- Create a booking

-- Given a listingId, a RenterId, A check in date, and a checkout date, add a new booking if the calendar allows it.
START TRANSACTION;
SET @ListingId = 1;
SET @RenterId = 1;
SET @ListingId = 1;
SET @CheckInDate = CURDATE();
Set @CheckOutDate = ADDDATE(CURDATE(), 5);
Set @NumDaysRequested = (SELECT datediff(@CheckOutDate, @CheckInDate));
Set @NumDaysAvailable = (SELECT COUNT(*) FROM Calendars WHERE ListingId = @ListingId AND IsAvailable = TRUE AND DayOfStay BETWEEN @CheckInDate and SUBDATE(@CheckOutDate, 1));

-- create the booking
INSERT INTO Bookings(Amount, ProcessedOn, PaypalEmail, CreditcardNumber, ListingId, RenterId)
	SELECT
	(SELECT BasePrice*8 FROM Listings WHERE Listings.Id = @ListingId),
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



-----------------------------------------------------------------------------
-- Cancel a booking

-- Given a UserId and a BookingId, cancel the booking
-- Let's say its the renter that cancelled.

-- Lets say we are removing booking Id=1 in the Java program
START TRANSACTION;
SET @BookingIdToCancel = 1;
-- Get the renter (the UserSIN attached to the provided creditcardNumber)
SET @CCNum = (SELECT CreditcardNumber FROM Bookings WHERE Id = @BookingIdToCancel);
SET @RenterUserSIN = (SELECT UserSIN FROM Renters WHERE CreditcardNumber = @CCNum);

-- Set refunded on in that booking, set the calendar for those days avaiable again.
UPDATE Bookings
SET
RefundedOn = CURDATE(),
CancelledById  = @RenterUserSIN
WHERE Id = @BookingIdToCancel;

-- SET all the days in Calendars with this bookingId as Available again, and remove the BookingId
UPDATE Calendars
SET
IsAvailable = True,
BookingId = NULL
WHERE BookingId = @BookingIdToCancel;
COMMIT;