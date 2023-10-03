package org.bookreviewer;

public class Book {
    String title;
    String author;
    short pages;
    String description;
    boolean read;

    public Book(String title, String author, short pages, String description, boolean read) {
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.description = description;
        this.read = read;
    }

    @Override
    public String toString() {
        return this.title + " -  " + this.author + "\tNo pages: " + this.pages;
    }
}
