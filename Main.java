package pack;

import java.sql.*;
import java.util.*;

public class Main {
	public static void main(String[] args) {
		try {

			Scanner scanner = new Scanner(System.in);
			int choice = 0, studentId = 0, bookId = 0, returnBookId = 0, addBookId, removeBookId;
			String adminPassword, addBookTitle, addBookAuthor;
			boolean isStudent = false;
			boolean isAdmin = false;

// --------------- CONNECTING WITH THE MYSQL DATABASE USING XAMPP SERVER -----------------------------------------
//----------------------------------------------------------------------------------------------------------------

			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/library", "root", "");
			System.out.println("The Database is connected Successfully....\n");
			Statement statement = con.createStatement();

//--------------   CREATES THE TABLE IS THEY DO NOT EXITS.  -----------------------------------------------------
//---------------------------------------------------------------------------------------------------------------

			statement.executeUpdate(
					"CREATE TABLE IF NOT EXISTS Books (id INT PRIMARY KEY, title VARCHAR(50), author VARCHAR(50), is_available BOOLEAN)");
			statement.executeUpdate(
					"CREATE TABLE IF NOT EXISTS Students (id INT PRIMARY KEY, name VARCHAR(50), books_issued INT)");
			statement.executeUpdate(
					"CREATE TABLE IF NOT EXISTS bookissue (studentid INT PRIMARY KEY, bookid int, quantity INT)");

//--------------  PROGRAM STARTS FROM HERE -----------------------------------------------------------------------

			while (true) {

//-------------- CHECK THE CREDINTIALS OF THE USER AND GIVE ACCESS OTHERWISE REPEAT THE PROCESS  AGAIN -----------

				if (!isStudent && !isAdmin) {
					System.out.println("\n1. Admin login");
					System.out.println("2. Student login");
					System.out.println("3. Exit");
					choice = scanner.nextInt();
					scanner.nextLine();
					switch (choice) {

					case 1:
						System.out.print("\nEnter admin password: ");
						adminPassword = scanner.nextLine();
						if (adminPassword.equals("1234")) {
							isAdmin = true;
							System.out.println("\n login successful.");
						} else {
							System.out.println("\nIncorrect password");
						}
						break;
					case 2:
						System.out.print("\nEnter student ID: ");
						studentId = scanner.nextInt();
						scanner.nextLine();
						ResultSet studentResultSet = statement
								.executeQuery("SELECT * FROM Students WHERE id = " + studentId);
						if (studentResultSet.next()) {
							isStudent = true;
							System.out.println("\nlogin successful. \nWelcome " + studentResultSet.getString(2));
						} else {
							System.out.println("Student not found");
						}
						break;
					case 3:
						System.out.println(
								"\nExiting program....  \n\n -----------------Visit Again.-------------------------");
						return;
					default:
						System.out.println("\nInvalid choice");
					}
				}

//---------------  IF THE USER IS VERIFIED AS THE STUDENT THEN BELOW CODE EXECUTES ------------------------------------------

				else if (isStudent) {
					System.out.println("\n1. Borrow a book");
					System.out.println("2. Return a book");
					System.out.println("3. Logout");
					choice = scanner.nextInt();
					scanner.nextLine();
					switch (choice) {
					case 1:

						// -------------- CODE TO SHOW ALL THE AVALIABLE BOOKS IN THE LIBRARY.
						// ------------------------------------------

						System.out.println(
								"\nBelow is the list of all avaliable books in the library.\n choose the book you want..");
						ResultSet res = statement.executeQuery("Select * from Books;");
						System.out.println("\nBook Id\t| Book Name\t| Author Name\t| Quantity");
						System.out.println("---------------------------------------------------");
						while (res.next()) {
							System.out.println(res.getInt(1) + "\t|" + res.getString(2) + "\t|" + res.getString(3)
									+ "\t|" + res.getInt(4));
						}
						res.close();
						// -------------- CODE STOPS
						// --------------------------------------------------------------------------------------

						System.out.print("\nEnter book ID: ");
						bookId = scanner.nextInt();
						scanner.nextLine();
						ResultSet bookResultSet = statement.executeQuery("SELECT * FROM Books WHERE id = " + bookId);
						if (bookResultSet.next() && bookResultSet.getBoolean("is_available")) {
							statement.executeUpdate(
									"insert into bookissue values (" + studentId + "," + bookId + ",1);");
							statement.executeUpdate(
									"UPDATE Books SET is_available = is_available-1 WHERE id = " + bookId);
							statement.executeUpdate(
									"UPDATE Students SET books_issued = books_issued + 1 WHERE id = " + studentId);
							System.out.println("Book borrowed successfully");
						} else {
							System.out.println("\nBook not available");
						}

						break;

					case 2:

//	----------------------	CODE TO SHOW ALL THE ISSUED BOOKS TO USERS --------------------------------------------------------------
						System.out.println(
								"\nBelow is the list of all the books issued to you .\nselect the one you want to return.");
						ResultSet rees = statement
								.executeQuery("Select * from bookissue where studentid=" + studentId + ";");
						System.out.println("Book Id | Quantity");
						System.out.println("--------");
						while (rees.next()) {
							System.out.println(rees.getInt(2) + "\t|" + rees.getInt(3));
						}
						rees.close();

//------------------------------------------------------------------------------------------------------------------------------------

						ResultSet issuedBooksResultSet = statement
								.executeQuery("SELECT * FROM Students WHERE id = " + studentId);
						if (issuedBooksResultSet.next() && issuedBooksResultSet.getInt("books_issued") > 0) {
							System.out.print("\nEnter book ID: ");
							returnBookId = scanner.nextInt();
							scanner.nextLine();
							ResultSet returnBookResultSet = statement
									.executeQuery("SELECT * FROM Books WHERE id = " + returnBookId);
							if (returnBookResultSet.next() && !returnBookResultSet.getBoolean("is_available")) {
								statement.executeUpdate(
										"delete from bookissue where studentid=" + studentId + " and bookid=" + bookId);
								statement.executeUpdate(
										"UPDATE Books SET is_available = is_available+1 WHERE id = " + returnBookId);
								statement.executeUpdate(
										"UPDATE Students SET books_issued = books_issued - 1 WHERE id = " + studentId);
								System.out.println("Book returned successfully");
							} else {
								System.out.println("\nInvalid book ID");
							}
						} else {
							System.out.println("\nNo books issued");
						}
						break;
					case 3:
						isStudent = false;
						System.out.println("\nLogged out successfully");
						break;
					default:
						System.out.println("\nInvalid choice");
					}
				}

// -------------  IF THE USER IS VERIFIED AS ADMIN THE BELOW CODE EXECUTES.-----------------------------------------------

				else if (isAdmin) {
					System.out.println("\n1. Add a book");
					System.out.println("2. Remove a book");
					System.out.println("3. Show all books");
					System.out.println("4. Logout");
					choice = scanner.nextInt();
					scanner.nextLine();

					switch (choice) {
					case 1:
						System.out.print("\nEnter book ID: ");
						addBookId = scanner.nextInt();
						scanner.nextLine();
						System.out.print("Enter book title: ");
						addBookTitle = scanner.nextLine();
						System.out.print("Enter book author: ");
						addBookAuthor = scanner.nextLine();
						statement.executeUpdate("INSERT INTO Books (id, title, author, is_available) VALUES ("
								+ addBookId + ", '" + addBookTitle + "', '" + addBookAuthor + "', true)");
						System.out.println("\nBook added successfully");
						break;
					case 2:
						System.out.print("\nEnter book ID: ");
						removeBookId = scanner.nextInt();
						scanner.nextLine();
						statement.executeUpdate("DELETE FROM Books WHERE id = " + removeBookId);
						System.out.println("\nBook removed successfully");
						break;
					case 3:
						System.out.println(
								"\nBelow is the list of all avaliable books in the library.");
						ResultSet res = statement.executeQuery("Select * from Books;");
						System.out.println("\nBook Id\t| Book Name\t| Author Name\t| Quantity");
						System.out.println("---------------------------------------------------");
						while (res.next()) {
							System.out.println(res.getInt(1) + "\t|" + res.getString(2) + "\t|" + res.getString(3)
									+ "\t|" + res.getInt(4));
						}
						res.close();
						break;
					case 4:
						isAdmin = false;
						System.out.println("\nLogged out successfully");
						break;
					default:
						System.out.println("\nInvalid choice");
					}

				}

			}

		} catch (Exception e) {
			System.out.print("Do not connect to DB - Error:" + e);
		}
	}
}
