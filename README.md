# Final Diploma Project — Java Course

## Your Travel Companion

Your Travel Companion is a web application designed to help users find travel companions.  
The system allows users to search for, create, or delete trips, as well as join or leave them.  
Users can also edit their profile information (phone, address, and email — email change is available only for users registered via the form).

An admin role with moderation capabilities is available.

---

## User Roles and Permissions

### Regular User:
- Register via email or Google
- Confirm email via code (check Spam folder)
- Edit profile (address, phone number, email)
- Create, view, search, and delete trips  
  (only the author or admin can delete a trip)
- Join or leave trips
- If you are the trip author, you can remove companions from your trip

Note: Only users registered through the form can change their email.  
Email changes require confirmation via code.

### Admin:
In addition to all regular user privileges, the admin can:
- Block or unblock users
- Delete users or trips
- Cannot delete or block their own account

Test Admin Credentials:  
Email: admin@gmail.com  
Password: admin

---

## Database Structure

- users — stores user information
- trips — stores trip data
- companions — stores trip join requests
- countries — list of available countries (manually added via the database)

---

## Technologies Used

- Java 21
- Spring Boot 3 (MVC, Security, Data JPA)
- MySQL 8
- Thymeleaf
- OAuth2 (Google login)
- HTML/CSS (without external frameworks)
- Maven

---

## System Messages

Informative system responses include:
- Invalid login or password
- Blocked account — "Your profile has been blocked."
- Successful logout
- Incorrect email confirmation code

---

## Deployment

The project was deployed using Google Cloud Platform (GCP).  
It uses Cloud Run to host the backend and Cloud SQL for the MySQL database.

Technologies and tools used:
- GitHub — source code repository
- Docker — application containerization
- Cloud Run — hosting the Docker container as a scalable web service
- Cloud SQL — database instance created manually via the GCP console
- OAuth2 — authentication via Google, configured in the Google Developer Console with correct redirect URIs

Live version:  
https://your-travel-companion-249068273473.us-central1.run.app

---

## Planned Features

- Uploading profile pictures (avatars)
- Advanced profile editing
- Messaging/chat between travel companions or globally between users
- Trip and user reviews and rating system
- Refactor the project and add unit and integration tests