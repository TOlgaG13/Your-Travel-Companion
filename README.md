Final Diploma Project — Java Course
"Your Travel Companion" is a web application designed to help users find travel companions.
The system allows users to search for, create, or delete trips, as well as join or leave them.
Users can also edit their profile information 
(phone, address, and email – email change is available only for users registered via the form).

An admin role with moderation capabilities is available.

User Roles and Permissions
Regular User:
-Register via email or Google;
-Email confirmation required (check the Spam folder for the code);
-Edit profile (address, phone number, email);
-Create, view, search, and delete trips (only the author or admin can delete a trip);
-Join or leave trips;
-If you are the trip author, you can remove a companion from your trip.

Note: Only users registered through the form can change their email. Email changes require confirmation via code.

Admin:
In addition to all regular user privileges, the admin has access to an Admin Panel with the ability to:
-Block or unblock users;
-Delete users or trips;
-Cannot delete or block their own account.

Test Admin Credentials:
Email: admin@gmail.com
Password: admin

Database Structure:
users — stores user information
trips — stores trip data
companions — stores trip join requests
countries — list of available countries (manually populated via the database)

Technologies Used:
-Java 21
-Spring Boot 3 (MVC, Security, Data JPA)
-MySQL 8
-Thymeleaf
-OAuth2 (Google login)
-HTML/CSS (no external frameworks)
-Maven

System Messages:
-Informational feedback for users:
-Invalid login or password;
-Blocked account access attempt — "Your profile has been blocked."
-Successful logout;
-Incorrect email confirmation code.

Planned Features:
-Uploading profile pictures (avatars);
-Advanced profile editing;
-Messaging/chat between travel companions (or globally between users);
-User/trip reviews and rating system.
It is also planned to refactor the project and add unit and integration tests.