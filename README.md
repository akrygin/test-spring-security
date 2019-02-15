# Test task for Team online
# About
This application provides special "Hello World" page, but access have only registered user. Any anonymous user may register by creating a user account from the login page. 
# Application contains
There are 3 pages in application:
* Login - default page. All non-authorized users will be automatically navigate from any URL inside application to Login page.
* Registration - page, where anonymous user can create new account for itself. After succesfull registration user automatically login and navigate to Welcome page.
* Welcome - or "Hello World" secured page, can be viewed only by authorized users.
<p>Also authorized user able to check in-memory Database - H2 useng url http://localhost:8080/h2 but this just for testing purposes.
  
# Some notes
Registration form contains fields validation:
Password should be at list from 8 to 20 symbols, has minimum 1 upper case symbol, minimum 1 special symbol, minimum 1 numeric symbol.

# How to run
Build project and run SpringSecurityApplication.java class. Go to http://localhost:8080
