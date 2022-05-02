# eCommerce Application

- Coded in Java using Spring Boot, Hibernate ORM, and the H2 database. 
- Used a combination of usernames and passwords for authentication, as well as JSON Web Tokens (JWT) to handle the authorization.
- Wrote tests including sanity, regression and negative tests, and met an acceptable code coverage level (60%) as well.
- Indexed logs to Splunk in order to monitor a system.

## Features: 
- CreateUser, user login, get user profile
- Add and remove items to cart
- Submit cart to order
- View purchase history

## Logs
* CreateUser and order request successes and failures
* Order requests successes and failures
* Exceptions  

## Splunk: 
- Set up one alert for the system
- Created dashboard for success and failure numbers per minute of any one create user and order

## DevOps: 
- Configuration and automation of the CI/CD pipeline.
- Managed build and deployment of the application with Jenkins.
