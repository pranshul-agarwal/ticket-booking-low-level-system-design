https://www.lowleveldesign.io/OOD/DesignBookMyShow

# Low Level System Design - Ticket Booking Application Like BookMyShow, TicketMaster, etc. 

### Problem Statement
[Check here](problem-statement.md)

### Video Explanation
[https://www.youtube.com/playlist?list=PL564gOx0bCLpAL7yMJqOuK3_hBuLkyRhn](https://www.youtube.com/playlist?list=PL564gOx0bCLpAL7yMJqOuK3_hBuLkyRhn)

### Further Improvements
* Seat type: Silver, Gold, Diamond
    * Different pricing for different types of seats.
* Validating payment in payment success flow. 
* Checking if show creation is allowed. 
    * This mainly include implementing method `checkIfShowCreationAllowed` in `ShowService`.
* Handling different types of movies like 3D, 7D, etc.

### Concurrency
We can use transactions in SQL databases to avoid any clashes. For example, if we are using SQL server we can utilize Transaction Isolation Levels to lock the rows before we update them. Note: within a transaction, if we read rows we get a write-lock on them so that they can’t be updated by anyone else. Here is the sample code:

SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;

BEGIN TRANSACTION;

    -- Suppose we intend to reserve three seats (IDs: 54, 55, 56) for ShowID=99 
    Select * From ShowSeat where ShowID=99 && ShowSeatID in (54, 55, 56) && isReserved=0 
 
    -- if the number of rows returned by the above statement is NOT three, we can return failure to the user.
    update ShowSeat table...
    update Booking table ...

COMMIT TRANSACTION;
‘Serializable’ is the highest isolation level and guarantees safety from Dirty, Nonrepeatable, and Phantoms reads.

Once the above database transaction is successful, we can safely assume that the reservation has been marked successfully and no two customers will be able to reserve the same seat.

