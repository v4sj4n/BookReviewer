package org.bookreviewer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainGUI {
    private JButton addANewBookButton;
    private JPanel mainPane;


    private static DatabaseManager db = new DatabaseManager();

    public MainGUI() {


        addANewBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                db.checkDb();
                String bTitle = JOptionPane.showInputDialog(null, "Enter the book title");
                while (bTitle.length() < 2) {
                    JOptionPane.showMessageDialog(null, "Book title can't be less than 2 characters");
                    bTitle = JOptionPane.showInputDialog(null, "Enter the book title");
                }
                String bAuthor = JOptionPane.showInputDialog(null, "Enter the book author");
                while (bAuthor.length() < 4) {
                    JOptionPane.showMessageDialog(null, "Book author can't be less than 4 characters");
                    bAuthor = JOptionPane.showInputDialog(null, "Enter the book author");
                }
                short bPages = Short.parseShort(JOptionPane.showInputDialog(null, "Enter the book pages"));
                while (bPages < 20) {
                    JOptionPane.showMessageDialog(null, "Book pages can't be less than 20 characters");
                    bPages = Short.parseShort(JOptionPane.showInputDialog(null, "Enter the book pages"));
                }
                String bDescription = JOptionPane.showInputDialog(null, "Enter the book description");
                while (bDescription.length() < 50) {
                    JOptionPane.showMessageDialog(null, "Book description can't be less than 50 characters");
                    bDescription = JOptionPane.showInputDialog(null, "Enter the book description");
                }
                String bReadStr = JOptionPane.showInputDialog(null, "Enter yes if you read it or no if you haven't");
                while (!(bReadStr.equalsIgnoreCase("yes") || bReadStr.equalsIgnoreCase("no"))) {
                    JOptionPane.showMessageDialog(null, "Please enter yes or no:");
                    bReadStr = JOptionPane.showInputDialog(null, "Have you read the book (yes/no)");
                }
                boolean bRead = bReadStr.equalsIgnoreCase("yes");

                db.create(new Book(bTitle, bAuthor, bPages, bDescription, bRead));
            }
        });

    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("BookReviewer");

        frame.setContentPane(new MainGUI().mainPane);
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }
}
