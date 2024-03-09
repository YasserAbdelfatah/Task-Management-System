# Task Management System


This is a simple task management system that allows users to create, view, update and delete tasks.
The system also allows users to assign tasks to other users and mark tasks as completed.

## Technologies Used

Spring Boot 3.2.2   
Java 21     
H2 Database     
JWT Authentication  
Lombok for code generation



## Building and Running

### To build and run the project:   
- Clone the repository.           
- Import the project into your chosen IDE.
- Configure your Email settings in application.yml.
- Run the main application class to start the Spring Boot application.
- Access the application's API documentation using the provided URL ( usually http://localhost:8090/swagger-ui/index.html).
- Use the provided postman collection to test the application's endpoints (Task Management.postman_collection.json).


## The system comes with default users for signing in:

### User 1:
    Email: yasserabdelfatah@outlook.com
    Password: pass 
    Role: ADMIN

### User 2:    
    Email: normal@gmail.com
    Password: pass
    Role: USER

### * There is a job scheduled to send emails to the users with the tasks that are due today every day at midnight.