package org.bookreviewer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;


public class DatabaseManager {
    private Connection connection = null;
    private final String connString = "jdbc:sqlite:bookie.db";
    static Scanner scanner = new Scanner(System.in);

    public void checkDb() {
        try {
            connection = DriverManager.getConnection(connString);
            Statement statement = connection.createStatement();
            String createTableSQL = "CREATE TABLE IF NOT EXISTS book (" + "ID INTEGER PRIMARY KEY AUTOINCREMENT," + "title TEXT NOT NULL," + "author TEXT NOT NULL," + "pages INTEGER NOT NULL," + "description TEXT," + "read INTEGER DEFAULT 0" + ")";

            statement.executeUpdate(createTableSQL);

        } catch (SQLException e) {

            System.err.println(e.getMessage());
        }
    }


    public void read() {
        try {
            connection = DriverManager.getConnection(connString);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM book");
            if (!rs.isBeforeFirst()) {
                System.out.println("No books.\n");
            } else {
                while (rs.next()) {
                    System.out.println(rs.getString("id") + ") " + rs.getString("title"));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    public void read(int id) {
        try {
            connection = DriverManager.getConnection(connString);
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM book WHERE ID = " + id);
            if (!rs.isBeforeFirst()) {
                System.out.println("No books.");
            } else {

                String title = rs.getString("title");
                String author = rs.getString("author");
                short pages = Short.parseShort(rs.getString("pages"));
                String description = rs.getString("description");
                int read = Integer.parseInt(rs.getString("read"));

                System.out.println(title + " by " + author + " has " + pages + " pages.");
                formatText(description);
                System.out.println((read == 1) ? "I have read it\n" : "I want to read it\n");


            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    public void create(Book book) {
        try {
            connection = DriverManager.getConnection(connString);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO book(title, author, pages, description, read) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, book.title);
            preparedStatement.setString(2, book.author);
            preparedStatement.setShort(3, book.pages);
            preparedStatement.setString(4, book.description);
            preparedStatement.setInt(5, book.read ? 1 : 0);
            preparedStatement.executeUpdate();

            System.out.println("Book added successfully\n");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    public void delete() {
        try {
            connection = DriverManager.getConnection(connString);
            read();
            System.out.println("Enter book's id");
            int id = scanner.nextInt();
            Statement statement = connection.createStatement();

            statement.executeUpdate("DELETE FROM book WHERE ID = " + id);
            System.out.println("Book deleted successfully\n");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    public void editBook(int bookId) {

        try {
            connection = DriverManager.getConnection(connString);
            Statement statement = connection.createStatement();


            String[] choices = {"0)Exit", "1) Title", "2) Author", "3) Pages", "4) Description", "5) Read Status "};

            System.out.println("What do you want to edit:");
            for (String choice : choices) {
                System.out.println(choice);
            }
            int usChoice = scanner.nextInt();
            scanner.nextLine();

            if (usChoice == 0) {
                return;
            }

            ResultSet rs = statement.executeQuery("SELECT * FROM book WHERE ID = " + bookId);
            switch (usChoice) {
                case 1 -> {
                    System.out.println("Current value of title is >>" + rs.getString("title") + "<<, how do you want the book to be titled: ");
                    String newTitle = scanner.nextLine();
                    Statement titleStatement = connection.createStatement();
                    titleStatement.executeUpdate("UPDATE BOOK SET title = '" + newTitle + "' WHERE ID = " + bookId);
                    System.out.println("Title updated successfully\n");
                }
                case 2 -> {
                    System.out.println("Current value of title is >>" + rs.getString("author") + "<<, how do you want the book author to be: ");
                    String newAuthor = scanner.nextLine();
                    Statement authorStatement = connection.createStatement();
                    authorStatement.executeUpdate("UPDATE BOOK SET author = '" + newAuthor + "' WHERE ID = " + bookId);
                    System.out.println("Author updated successfully\n");

                }
                case 3 -> {
                    System.out.println("Current value of pages is >>" + rs.getInt("pages") + "<<, how many pages does the book have: ");
                    short newPages = Short.parseShort(scanner.nextLine());
                    Statement pagesStatement = connection.createStatement();
                    pagesStatement.executeUpdate("UPDATE BOOK SET pages = " + newPages + " WHERE ID = " + bookId);
                    System.out.println("Pages updated successfully\n");
                }
                case 4 -> {
                    System.out.println("Current value of description is >>" + rs.getString("description").substring(0, Math.min(rs.getString("description").length(), 50)) + ((rs.getString("description").length() > 80) ? "..." : "") + "<< enter your desired description: ");
                    String newDescription = scanner.nextLine();
                    Statement pagesStatement = connection.createStatement();
                    pagesStatement.executeUpdate("UPDATE BOOK SET description = '" + newDescription + "' WHERE ID = " + bookId);
                    System.out.println("Description updated successfully\n");
                }
                case 5 -> {
                    System.out.println("Current value of read is >>" + ((rs.getInt("read") == 1) ? "yes" : "no") + "<< enter yes or no if you have read it");
                    String newRead = scanner.nextLine();
                    while (!(newRead.equalsIgnoreCase("yes") || newRead.equalsIgnoreCase("no"))) {
                        System.out.println("Please enter yes / no");
                        newRead = scanner.nextLine();
                    }
                    Statement pagesStatement = connection.createStatement();
                    pagesStatement.executeUpdate("UPDATE BOOK SET read = " + (newRead.equalsIgnoreCase("yes") ? 1 : 0) + " WHERE ID = " + bookId);
                    System.out.println("Read status updated successfully\n");
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }


    }

    private void formatText(String text) {
        int currentLineLength = 0;
        String[] words = text.split(" ");

        for (String word : words) {
            if (currentLineLength + word.length() + 1 <= 80) {
                if (currentLineLength > 0) {
                    System.out.print(" ");
                    currentLineLength++;
                }
                System.out.print(word);
                currentLineLength += word.length();
            } else {
                System.out.println();
                System.out.print(word);
                currentLineLength = word.length();
            }
        }
        System.out.println();
    }

}