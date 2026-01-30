package ru.ucharov.springcourse.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.ucharov.springcourse.models.Book;
import ru.ucharov.springcourse.models.Person;

import java.util.List;
import java.util.Optional;

/**
 * @author Ucharov EVgenii
 */
@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id_person) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE id_person=?", new Object[]{id_person}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person (fio_person, year_birth) VALUES(?, ?)", person.getFio_person(), person.getYear_birth());
    }

    public void update(int id_person, Person updatedPerson) {
        jdbcTemplate.update("UPDATE Person SET fio_person=?, year_birth=? WHERE id_person=?", updatedPerson.getFio_person(), updatedPerson.getYear_birth(), id_person);
    }

    public void delete(int id_person) {
        jdbcTemplate.update("DELETE FROM Person WHERE id_person=?", id_person);
    }

    //Для валидации уникальности ФИО
    public Optional<Person> getPersonByFullName(String fio_person) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE fio_person=?", new Object[]{fio_person},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }
 
    //Здесь JOIN  не нужен. И так уже получили человека с помощью отдельного метода
    public List <Book> getBooksByPersonId(int id_person) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE id_person=?", new Object[]{id_person}, new BeanPropertyRowMapper<>(Book.class));

    }
}