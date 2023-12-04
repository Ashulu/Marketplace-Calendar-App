# Project_5
CS180 project 5

# Sections
**[Download SQLite JDBC Driver](https://github.com/Ashulu/Project_5/tree/main#dowload-sqlite-jdbc-driver)**  
**[Database Structure](https://github.com/Ashulu/Project_5/blob/main/README.md#database-structure)**

## Dowload SQLite JDBC Driver
1. Download the driver by referring to the following [repository](https://github.com/xerial/sqlite-jdbc).  
   `https://github.com/xerial/sqlite-jdbc`
3. In the README of the repository linked above, go to [Usage](https://github.com/xerial/sqlite-jdbc#usage) section.  
   `https://github.com/xerial/sqlite-jdbc#usage`
5. You have to download the two .jar files
   1. `sqlite-jdbc-3.44.0.0.jar` (recommended to direct download from _releases_ page)
   2. `slf4j-api-1.7.36.jar`
6. Add them both to classpath. **Below are some popular IDEs and how to add .jar files to classpath**

- **Intellij**
  - Refer to the following [Article](https://www.geeksforgeeks.org/how-to-add-external-jar-file-to-an-intellij-idea-project/).  
    `https://www.geeksforgeeks.org/how-to-add-external-jar-file-to-an-intellij-idea-project/`
- **Eclipse**
  - Refer to the following [Article](https://www.janbasktraining.com/community/sql-server/how-to-import-a-jar-file-in-eclipse).  
    `https://www.janbasktraining.com/community/sql-server/how-to-import-a-jar-file-in-eclipse`
- **No IDE**
  - Refer to the following [Article](https://stackoverflow.com/questions/5112607/how-to-include-libraries-in-java-without-using-an-ide).  
    `https://stackoverflow.com/questions/5112607/how-to-include-libraries-in-java-without-using-an-ide`

## Database Structure

We used SQL to create our own .db file. Below are the tables and their respective columns in the database.
- accounts  
  `type, email, password`
- stores  
  `sellerEmail, storeName`
- calendars  
  `storeName, calendarName, calendarDescription`
- windows  
  `storeName, calendarName, appointmentTitle, startTime, endTime, maxAttendees, currentBookings`
- appointments  
  `customerEmail, sellerEmail, storeName, calendarName, startTime, endTime, booking, isApproved, isRequest, timeStamp`
  
*Note that startTime and endTime are stored as strings of length 4, to represent military time. timeStamp is stored as a string by using strftime() method in SQL*

##ActionLists

**login**

SELECT password FROM accounts WHERE email == ?
*check whether passwords match on server side*

variables needed:
email, password

return options:
-1 = email does not exist
0 = password is incorrect
1 = customer
2 = seller

-------------------------------------------------------------------------------------------------------------

**createAccount**
- check if the email is already in the database
- insert into table

SELECT COUNT email FROM accounts WHERE email == ?
INSERT INTO accounts (type, email, password) VALUES (?, ?, ?)

variable needed:
type, email, password

return options:
0 = email already exists
1 = email and password added

-------------------------------------------------------------------------------------------------------------

**viewCalendar**

SELECT calendarName, calendarDescription from calendars
*store all calendarNames in the server, then iterate through*
[calendarName, calendarDescription] * number of calendars
SELECT appointmentTitle, startTime, endTime, maxAttendees, currentBookings WHERE calendarName == iteration(i)
[appointmentTitle, startTime, endTime, maxAttendees, currentBookings] * number of calendars

variables needed:
none

return:
2 arraylist
[calendarName, calendarDescription] * number of calendars
[appointmentTitle, startTime, endTime, maxAttendees, currentBookings] * number of calendars
*They will be converted into strings. I could send the arraylist by itself if we use data output stream tho*

-------------------------------------------------------------------------------------------------------------

**requestAppointment**

- Server will return all the calendars, and the client will choose
- Server will return all the available windows to the client, and the client will choose
- Server will then update the database if possible

SELECT * FROM calendars
SELECT startTime, endTime, maxAttendees, currentBookings FROM windows WHERE (storeName == ? AND calendarName == ? AND currentBookings < maxAttendees)
SELECT sellerEmail FROM stores WHERE storeName == ?
*check if adding the bookings would exceed maxAttendees on the server*
INSERT INTO appointments (customerEmail, sellerEmail, storeName, calendarName, startTime, endTime, booking, isApproved, isRequest, timeStamp) VALUES (?, ?, ?, ?, ?, ?, ?, 0, 1, strftime('%s', 'now'))

variables needed:
input 1: storeName, calendarName
input 2: startTime, endTime, booking

return:
output 1: arraylist of stores, calendars, and calendarDescriptions
output 2: arraylist of startTime, endTime, maxAttendees, and currentBookings of the chosen calendar (input 1)
output 3: result of the action, 1 for success, 0 for failure (liekly due to bookings exceeding maxAttendees)

-------------------------------------------------------------------------------------------------------------

**cancelRequest**

- Server will return all the requests of the user, then delete based on the user's choice

SELECT storeName, calendarName, startTime, endTime, booking FROM appointments WHERE (customerEmail == ? AND isApproved == 0)
DELETE FROM appointments WHERE (storeName == ? AND calendarName == ? AND startTime == ?)

variables needed:
storeName, calendarName, startTime

return:
output 1: arraylist of [calendarName, calendarName, startTime, endTime, booking] * number of requests
output 2: 1 if deletion was successful, 0 if not (most likely will return 1)

-------------------------------------------------------------------------------------------------------------

**viewApproved**

SELECT storeName, calendarName, startTime, endTime, booking FROM appointments WHERE (customerEmail == ? AND isApproved == 1)

variables needed:
none

return:
arraylist of [calendarName, calendarName, startTime, endTime, booking] * number of approved requests

-------------------------------------------------------------------------------------------------------------

**showStatisticsCustomer**

SELECT * FROM stores;
Arraylist of [sellerEmail, storeName]

SELECT storeName, startTime, endTime, SUM(currentBookings) AS totalCustomers, MAX(currentBookings) AS windowCustomers FROM windows GROUP BY storeName;
Arraylist of [storeName, startTime, endTime, totalCustomers, windowCustomers]

variables needed:
none

return:
1. Arraylist of [sellerEmail, storeName]
2. Arraylist of [storeName, startTime, endTime, totalCustomers, windowCustomers]

-------------------------------------------------------------------------------------------------------------

**showStatisticsCustomerOrderByTotal**

SELECT * FROM stores;
Arraylist of [sellerEmail, storeName]

SELECT storeName, startTime, endTime, SUM(currentBookings) AS totalCustomers, MAX(currentBookings) AS windowCustomers FROM windows GROUP BY storeName ORDER BY totalCustomers DESC;
Arraylist of [storeName, startTime, endTime, totalCustomers, windowCustomers]

variables needed:
none

return:
1. Arraylist of [sellerEmail, storeName]
2. Arraylist of [storeName, startTime, endTime, totalCustomers, windowCustomers] - ordered

-------------------------------------------------------------------------------------------------------------

**showApproved**

SELECT customerEmail, storeName, calendarName, startTime, endTime, booking, timeStamp FROM appointments WHERE (sellerEmail == ? AND isApproved == 1)

variable needed:
none

return:
arraylist of [customerEmail, storeName, calendarName, startTime, endTime, booking, timeStamp]

-------------------------------------------------------------------------------------------------------------

**approveRequest**

SELECT customerEmail, storeName, calendarName, startTime, endTime, booking FROM appointments WHERE (sellerEmail == ? AND isApproved == 0)

if approved:
UPDATE appointments SET isApproved = 1, isRequest = 0, 


-------------------------------------------------------------------------------------------------------------
**createStore**

variables needed:
storeName

return:
0 or 1

-------------------------------------------------------------------------------------------------------------
**createCalendar**

variables needed:
1. storeName, calendarName, calendarDescription
2. startTime, endTime, maxAttendees * number of windows

return:
0 or 1

-------------------------------------------------------------------------------------------------------------
**editCalendar**

- edit name
- edit description
- edit windows (add or remove)

variables needed:
if name:
old name, new name
if description:
calendarName, description
if adding windows:
storeName, calendarName, startTime, endTime, maxAttendees
if deleting windows:
storeName, calendarName, startTime, endTime


-------------------------------------------------------------------------------------------------------------
**deleteCalendar**

variables needed:
storeName, calendarName

-------------------------------------------------------------------------------------------------------------
**statisticsSeller**

output:
sellerEmail, storeName
[customerName, numofApproved]
most popular window
sorted in number of customers


-------------------------------------------------------------------------------------------------------------
**importCalendar**

variablesNeeded:
storeName, fileName

-------------------------------------------------------------------------------------------------------------
**exportApprovedRequests**

variablesNeeded:
fileName

























