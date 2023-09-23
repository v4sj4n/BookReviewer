package org.bookreviewer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;


public class Database {
    public void checkDb() {
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:bookie.db");
            Statement statement = connection.createStatement();
            String createTableSQL = "CREATE TABLE IF NOT EXISTS book (" + "ID INTEGER PRIMARY KEY AUTOINCREMENT," + "title TEXT NOT NULL," + "author TEXT NOT NULL," + "pages INTEGER NOT NULL," + "description TEXT," + "read INTEGER DEFAULT 0" + ")";

            statement.executeUpdate(createTableSQL);

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        System.out.println();
    }


    public void readBooks() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:bookie.db");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM book");
            if (!rs.isBeforeFirst()) {
                System.out.println("No books.");
            } else {
                while (rs.next()) {
                    System.out.println(rs.getString("id") + ") " + rs.getString("title"));
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        System.out.println();
    }

    public void addBook(String title, String author, short pages, String description, boolean read) {
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:bookie.db");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO book(title, author, pages, description, read) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setShort(3, pages);
            preparedStatement.setString(4, description);
            preparedStatement.setInt(5, read ? 1 : 0);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

    }

    public void removeBook() {
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:bookie.db");
            readBooks();
            System.out.println("Enter book's id");
            Scanner scanner = new Scanner(System.in);
            int id = scanner.nextInt();
            Statement statement = connection.createStatement();

            statement.executeUpdate("DELETE FROM book WHERE ID = " + id);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

    }

    public void bookDescriptor() {
        Connection connection = null;
        try {

            connection = DriverManager.getConnection("jdbc:sqlite:bookie.db");
            readBooks();
            System.out.println("Enter book's id");
            Scanner scanner = new Scanner(System.in);
            int id = scanner.nextInt();
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
                formatText(description, 80);
                System.out.println((read == 1) ? "I have read it" : "I want to read it\n");


            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void editBook() {
        Connection connection = null;

        try {

            connection = DriverManager.getConnection("jdbc:sqlite:bookie.db");

            Scanner sc = new Scanner(System.in);
            readBooks();

            System.out.println("Enter the id of the book you want to edit");
            int bookId = sc.nextInt();

            Statement statement = connection.createStatement();


            String[] choices = {"0)Exit", "1) Title", "2) Author", "3) Pages", "4) Description", "5) Read Status ", "6) Everything"};
            System.out.println("What do you want to edit:");
            for (String choice : choices) {
                System.out.println(choice);
            }
            int usChoice = sc.nextInt();

            if (usChoice == 0) {
                return;
            }
            ResultSet rs = statement.executeQuery("SELECT * FROM book WHERE ID = " + bookId);

            switch (usChoice) {
                case 1 -> {
                    Scanner titleScanner = new Scanner(System.in);
                    System.out.println("Current value of title is " + rs.getString("title") + ", how do you want the book to be titled: ");
                    String newTitle = titleScanner.nextLine();
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE BOOK SET title = ? WHERE ID = ?");
                    preparedStatement.setString(1, newTitle);
                    preparedStatement.setInt(2, bookId);
                    preparedStatement.executeUpdate();
                }
                case 2 -> {
                    Scanner authorScanner = new Scanner(System.in);
                    System.out.println("Current value of title is " + rs.getString("author") + ", how do you want the book author to be: ");
                    String newAuthor = authorScanner.nextLine();
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE BOOK SET author = ? WHERE ID = ?");
                    preparedStatement.setString(1, newAuthor);
                    preparedStatement.setInt(2, bookId);
                    preparedStatement.executeUpdate();
                }
                case 3 -> {
                    Scanner pagesScanner = new Scanner(System.in);
                    System.out.println("Current value of pages is " + rs.getInt("pages") + ", how many pages does the book have: ");
                    int newPages = pagesScanner.nextInt();
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE BOOK SET pages = ? WHERE ID = ?");
                    preparedStatement.setString(1, Integer.toString(newPages));
                    preparedStatement.setInt(2, bookId);
                    preparedStatement.executeUpdate();
                }
                case 4 -> {
                    Scanner descriptionScanner = new Scanner(System.in);
                    System.out.println("Current value of description is " + rs.getString("description").substring(0, Math.min(rs.getString("description").length(), 50)) + "... enter your desired description: ");
                    String newDescription = descriptionScanner.nextLine();
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE BOOK SET description = ? WHERE ID = ?");
                    preparedStatement.setString(1, newDescription);
                    preparedStatement.setInt(2, bookId);
                    preparedStatement.executeUpdate();
                }
                case 5 -> {
                    Scanner readScanner = new Scanner(System.in);
                    String readOrNo = (rs.getInt("read") == 1 ) ? "yes" : "no";
                    System.out.println("Current value of read is " +  readOrNo + " enter yes or no if you have read it");
                    String newRead = readScanner.nextLine();
                    while(!(newRead.equalsIgnoreCase("yes") || newRead.equalsIgnoreCase("no"))){
                        System.out.println("Current value of read is " +  readOrNo + " enter yes or no if you have read it");
                        newRead = readScanner.nextLine();
                    }
                    int newReadInt = newRead.equalsIgnoreCase("yes") ? 1 : 0;
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE BOOK SET description = ? WHERE ID = ?");
                    preparedStatement.setInt(1, newReadInt);
                    preparedStatement.setInt(2, bookId);
                    preparedStatement.executeUpdate();

                }
                case 6 ->{
                    // editing everything code
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }


    }

    private void formatText(String text, int width) {
        int currentLineLength = 0;
        String[] words = text.split(" ");

        for (String word : words) {
            if (currentLineLength + word.length() + 1 <= width) {
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