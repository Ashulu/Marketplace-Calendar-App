# Project_5
CS180 project 5

# Get started
**[Download SQLite JDBC Driver](https://github.com/Ashulu/Project_5/tree/main#dowload-sqlite-jdbc-driver)**

## Dowload SQLite JDBC Driver
1. Download the driver by referring to the following [repository](https://github.com/xerial/sqlite-jdbc). `https://github.com/xerial/sqlite-jdbc`
2. In the README of the repository linked above, go to [Usage](https://github.com/xerial/sqlite-jdbc#usage) section. (`https://github.com/xerial/sqlite-jdbc#usage`)
3. You have to download the two .jar files
   1. `sqlite-jdbc-3.44.0.0.jar` (recommended to direct download from _releases_ page)
   2. `slf4j-api-1.7.36.jar`
5. Add them both to classpath. **Below are some popular IDEs and how to add .jar files to classpath**

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
- accounts `type, email, password`
- stores `sellerEmail, storeName`
- calendars `storeName, calendarName, calendarDescription`
- windows `storeName, calendarName, appointmentTitle, startTime, endTime, maxAttendees, currentBookings`
- appointments `customerEmail, sellerEmail, storeName, calendarName, startTime, endTime, booking, isApproved, isRequest, timeStamp`
  
*Note that startTime and endTime are stored as strings of length 4, representing military time. timeStamp is stored as a string by using strftime()*
