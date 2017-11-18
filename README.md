# Introduction
Web App for CRUD operations over product and category items from MySQL DB

# Pre-requisite
Need MYSQL server to store the data of the application.

# How to run
This app has embedded jetty server. In order to run this website execute below maven command
mvn jetty:run or use maven plugin from the IDE, i am using Intellij

It will start the embedded jetty server on default port 8080

# How to Use
- Access the website using http://localhost:8080/spring4/
- will see read only products
- Click on Edit Products on order to add new products
- Hit new and fill the values then save
- you can delete or edit any product

# Technologies used
- Spring Core
- Spring MVC
- Spring Schedule
- MYSQL
- Maven
- Html/JSP
- Mockito
- Junit
- Embedded jetty Server
- MockMvc
- Logs4j


# TODO
- Java Doc
- Form Level Validation
- Message Resourcing
- More testing coverage
- UI improvements
- Adding Security for Login
- ...
