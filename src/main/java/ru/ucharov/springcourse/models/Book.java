package ru.ucharov.springcourse.models;

import jakarta.validation.constraints.*;


public class Book {
    private int id_book;

    @NotEmpty(message = "Название книги не должно быть пустым")
    @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов")
    private String title;

    @NotEmpty(message = "Автор не должен быть пустым")
    private String author;

    @Min(value = 1500, message = "Год должен быть больше 1500")
    @Max(value = 2026, message = "Год выпуска не может быть в будущем")
    private int year;

    public Book() {

    }

    public Book (int id_book, String title, String author, int year) {
        this.id_book = id_book;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public int getId_book() {
        return id_book;
    }
    public void setId_book(int id_book) {
        this.id_book = id_book;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {this.title = title;}
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {this.author = author;}
    public int getYear() {
        return year;
    }
    public void setYear(int year) {this.year = year;}




}
