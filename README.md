# TYSMSProject

## Run project 


Run in VSCode 

Visit http://localhost:8081/login
Use password from TYStudent.txt

## UI

[1] Inital Welcome page asking for the TY student to input a username and password. If not correct, standard error message is shown.

[2] If this username and password matches in the file (TYStudent.txt) then a second page is shown asking for 6 digit code, 
which they get from the email they put in for their user in the TYStudent.txt. If incorrect standard error message is shown.

[3] if 6-digit code is correct, then the user is presented with a third html file (students will implement this).


**Server**

[1] Initial page will submit username and password, this will be checked against database/file - returns success or fail.

[2] if login is success then the email associated with the username and password will receive a 6-digit code.

[3] Second page will submit 6-digit code - will need to ensure that this code is associated with the user in the session, and that the code is correct.


**File**

[1] For demo we will have one simple table all text.

FirstName(30), LastName(30), Username(30), Password(30), Email(30), SixDigitPassCode(6), isActive(true)


Things Students could consider improving:

1. Timeouts for 6 digit code
2. Number of retries for username/password
3. More informative error messages
4. Thinking about hacking opportunites (db not encrypted etc)
5. Other kinds of MFA
6. What makes a user inactive (three incorrect login tries)
