package org.bookreviewer;

import java.util.Scanner;


public class Main {


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String[] userChoices = {"0) Exit", "1) List books", "2) Add book", "3) Remove", "4) List the properties of a book", "5) Edit book"};
        Database db = new Database();
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
                case 1 -> db.readBooks();
                case 2 -> {
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

                    db.addBook(bTitle, bAuthor, bPages, bDescription, bRead);
                }
                case 3 -> db.removeBook();
                case 4 -> db.bookDescriptor();
                case 5 -> db.editBook();

                default -> System.out.println("Please enter a number from 0-5.");
            }
        }


    }
}