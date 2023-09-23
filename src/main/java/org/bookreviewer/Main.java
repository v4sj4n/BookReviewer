package org.bookreviewer;

import java.util.Scanner;


public class Main {


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String[] userChoices = {"0) Exit", "1) List books", "2) List the information of a book", "3) Add book", "4) Remove", "5) Edit book"};
        DatabaseManager db = new DatabaseManager();
        db.checkDb();


        while (true) {

            for (String choice : userChoices) {
                System.out.println(choice);
            }
            int userInput = -1;
            boolean validInput = false;

            while (!validInput) {
                try {
                    userInput = scanner.nextInt();
                    validInput = true; // Input is valid, exit the loop
                } catch (Exception e) {
                    System.out.println("Please enter a valid number 0-5: ");
                    scanner.nextLine(); // Consume invalid input
                }
            }


            if (userInput == 0) {
                break;
            }

            switch (userInput) {
                case 1 -> db.read();
                case 2 -> {
                    db.read();
                    System.out.println("Enter book's id");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    db.read(id);
                }
                case 3 -> {
                    Scanner sc = new Scanner(System.in);
                    System.out.println("Enter title: ");
                    String bTitle = sc.nextLine();

                    System.out.println("Enter author: ");
                    String bAuthor = sc.nextLine();

                    System.out.println("Enter pages: ");
                    short bPages = sc.nextShort();
                    sc.nextLine();

                    System.out.println("Enter description: ");
                    String bDescription = sc.nextLine();

                    System.out.println("Have you read it (yes/no): ");
                    boolean bRead = sc.nextLine().equalsIgnoreCase("yes");
                    Book bookToAdd = new Book(bTitle, bAuthor, bPages, bDescription, bRead);
                    db.create(bookToAdd);
                }
                case 4 -> db.delete();
                case 5 -> {
                    db.read();
                    System.out.println("Enter the id of the book you want to edit");
                    int bookId = scanner.nextInt();
                    scanner.nextLine();
                    db.editBook(bookId);
                }


                default -> System.out.println("Please enter a number from 0-5.");
            }
        }


    }
}