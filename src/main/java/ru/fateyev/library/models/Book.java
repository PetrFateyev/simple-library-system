package ru.fateyev.library.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Book {

    private int id;

    @NotEmpty(message = "Название книги не должно быть пустым")
    @Size(min = 1, max = 200, message = "Название книги должно быть от 1 до 200 символов длинной")
    private String title;

    @NotEmpty(message = "Автор не должен быть пусты")
    @Size(min = 2, max = 100, message = "Автор должен быть от 2 до 100 символов длинной")
    private String author;

    @Min(value = 1000, message = "Год должен быть от 1000 до 2023")
    @Max(value = 2023)
    private int year;

    public Book() {
    }

    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
