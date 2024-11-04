# Vacation planning system
Vacation Planning System implemented using **microservice** architecture  

## Used technologies
- Java
- SpringBoot
- REST
- React
- PostgreSQL 
- EA

### Design patterny
- Facade 
- Interceptors
- DTO
- DAO
- Cache
- Factory Method
- Dependency injection

### Architecture
The application is designed as a **microservice** application consisting of 2 service components - Account Management service and Notification service. These components are called from the ReactJS frontend component.
The backend components of the application will use a layered architecture composed of layers:
- database
- persistent - DAOs
- domain - models
- application/business - services - contain the main functions of the application
- presentation - REST API controllers
The front-end part of the application will be implemented by the React JS library.

[UML_component_diagram.pdf](https://github.com/user-attachments/files/17624676/UML_component_diagram.pdf)

<img width="653" alt="Screenshot 2024-11-04 at 23 47 25" src="https://github.com/user-attachments/assets/0d228dcd-0773-4df5-a549-22151651311b">


[UML_class_diagram.pdf](https://github.com/user-attachments/files/17624678/UML_class_diagram.pdf)

<img width="700" alt="Screenshot 2024-11-04 at 23 47 51" src="https://github.com/user-attachments/assets/329ff85a-2af6-4bd8-ae9c-13053fdf8455">

