# Library Book Issue and Return System

This project is a Java-based application that automates the book issue and return system of a library.

## Technologies Used

- Java
- SQL
- XAMPP Server
- MySQL Database
- JDBC (Java Database Connectivity)

## Features

- **Admin Login**: Allows administrators to log in with a password to manage the library system.
- **Student Login**: Allows students to log in with their ID to borrow and return books.
- **Book Management**:
  - Admins can add new books to the library.
  - Admins can remove existing books from the library.
  - Students can view available books and borrow them if available.
  - Students can return books they have borrowed.
- **Database Connectivity**: Uses JDBC to connect to a MySQL database hosted on XAMPP.

## Usage

1. **Setup Database**:
   - Ensure XAMPP server is running.
   - Create a MySQL database named `library`.
   - Execute the SQL scripts provided in the Java code to create necessary tables (`Books`, `Students`, `bookissue`).

2. **Run Application**:
   - Compile and run the `Main.java` file in your Java IDE or through the command line.
   - Follow the prompts to log in as admin or student and manage the library system.

## How to Use

- Clone the repository.
- Setup your local environment with Java, XAMPP, and MySQL.
- Customize the database connection details in the Java code if necessary.
- Run the application and interact with it via the console.

## License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.
