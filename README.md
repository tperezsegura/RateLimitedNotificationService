# Rate-Limited Notification Service

### Description

This repository implements a Rate-Limited Notification Service designed to send various types of notifications, such as status updates, daily news, and project invitations. The service includes rate-limiting rules (which can be dynamically loaded from an external source, such as a JSON file) to prevent recipients from receiving too many emails due to system errors or abuse. It automatically rejects requests exceeding predefined rate limits or those specifying an invalid notification type.

### Rate Limit Rules

- **Status**: Not more than 2 per minute for each recipient.  
- **News**: Not more than 1 per day for each recipient.  
- **Marketing**: Not more than 3 per hour for each recipient.  

These are just sample rules, and the system can have several rate-limiting rules.

### Project Structure
The project is organized into several packages:  
- `config`: Contains the configuration class for specifying the location of rate limit rules file based on the environment (production or test).  
- `exception`: Defines custom exceptions related to notifications and rate limiting.  
- `model`: Includes classes representing notifications and rate limit rules.  
- `service`: Contains the interface and implementation of the Notification Service.  
- `util`: Provides utility classes for reading rate limit rules from a file.  
- `Gateway`: Represents the external system responsible for sending notifications.  
- `Main`: Demonstrates the usage of the Notification Service with sample notifications.  
- `test`: Contains JUnit tests for the Notification Service.

### Configuration

The location of the rate limit rules file is specified in the RateLimitConfig class. By default, it looks for a file named rate_limit_rules.json in the src/main/resources/ directory for production and src/test/resources/ for testing. You can modify the file path in the configuration class based on your requirements.  Additionally, you have the flexibility to modify the content of the rate limit rules file itself to tailor the rate limits according to your specific needs.

### Dependencies

This project uses Maven for dependency management. The following dependencies are included:

- **JUnit Jupiter API**: Used for writing tests in JUnit 5.  
- **Mockito**: A mocking framework for creating and configuring mock objects.  
- **Jackson Databind**: A library for working with JSON data in Java.  
- **Lombok**: Reduces boilerplate code in Java classes.  
- **Awaitility**: A library for testing asynchronous code.  

### How to Use

1. Clone the repository:
    ```
    git clone https://github.com/tperezsegura/RateLimitedNotificationService.git
    ```
2. Open the project in your favorite Java IDE.
3. Run the Main class to see a demonstration of the Notification Service with sample notifications.