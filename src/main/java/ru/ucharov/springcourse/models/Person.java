package ru.ucharov.springcourse.models;

import jakarta.validation.constraints.*;

/**
 * @author Ucharov Evgenii
 */
public class Person {
    private int id_person;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String fio_person;

    @Min(value = 1900, message = "Age should be greater than 1900")
    @Max(value = 2026, message = "Year of birth should not be in the future")
    private int year_birth;

    public Person() {

    }

    public Person(int id_person, String fio_person, int year_birth) {
        this.id_person = id_person;
        this.fio_person = fio_person;
        this.year_birth = year_birth;
    }

    public int getId_person() {
        return id_person;
    }

    public void setId_person(int id_person) {
        this.id_person = id_person;
    }

    public String getFio_person() {
        return fio_person;
    }

    public void setFio_person(String fio_person) {
        this.fio_person = fio_person;
    }

    public int getYear_birth() {
        return year_birth;
    }

    public void setYear_birth(int year_birth) {
        this.year_birth = year_birth;
    }
}