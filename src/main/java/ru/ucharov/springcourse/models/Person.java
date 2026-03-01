package ru.ucharov.springcourse.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

/**
 * @author Ucharov Evgenii
 */
@Entity
@Table(name = "person")
public class Person {

    @Id
    @Column(name= "id_person")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть длиной от 2 до 100 символов")
    @Column(name = "fio_person")
    private String fioPerson;

    @Min(value = 1900, message = "Год рождения должен быть больше чем 1900")
    @Column(name = "year_birth")
    private int yearBirth;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

//    Конструктор по умолчанию нужен для Spring
    public Person() {

    }

    public Person(String fioPerson, int yearBirth) {
        this.fioPerson = fioPerson;
        this.yearBirth = yearBirth;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFioPerson() {
        return fioPerson;
    }

    public void setFioPerson(String fioPerson) {
        this.fioPerson = fioPerson;
    }

    public int getYearBirth() {
        return yearBirth;
    }

    public void setYearBirth(int yearBirth) {
        this.yearBirth = yearBirth;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}