package ru.ucharov.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.ucharov.springcourse.models.Book;
import ru.ucharov.springcourse.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private JdbcTemplate jdbcTemplate;
//    JdbcTemplate — это инструмент в Spring для упрощенной работы с базой данных.
//    Основные задачи:
//    Удаляет рутину: сам открывает/закрывает соединения и обрабатывает ошибки.
//    Выполняет SQL: позволяет запускать запросы (SELECT, UPDATE, INSERT) одной строчкой кода.
//    Безопасность: защищает от взлома (SQL-инъекций) через использование знаков ?.


    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book show(int id_book) {
        return jdbcTemplate.query("SELECT * FROM book WHERE id_book=?", new Object[]{id_book}, new BeanPropertyRowMapper<>(Book.class))
        .stream().findAny().orElse(null);
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO book (title, year) VALUES (?, ?)", book.getTitle(), book.getYear());
    }

    public void update(int id_book, Book book) {
        jdbcTemplate.update("UPDATE book SET title=?, author=?, year=? WHERE id_book=?", book.getTitle(), book.getAuthor(), book.getYear(), id_book);
    }

    public void delete (int id_book) {
        jdbcTemplate.update("DELETE FROM book WHERE id_book=?", id_book);
    }

    //Join'им таблицы Book и Person  и получаем человека которому принадлежит книга с указаным id
    public Optional<Person> getBookOwner (int id_book) {
        //Выбираем все колонки таблицы Person из обьедененной таблицы
        return jdbcTemplate.query("SELECT Person.* FROM Book JOIN Person ON Book.id_person = Person.id_person " +
                "WHERE Book.id_book = ?", new Object[]{id_book}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }




    // освобождаем книгу(этот метод вызывается, когда человек возвращает книгу в библиотеку)
//    выполняет возврат книги в библиотеку на уровне базы данных:
//    UPDATE book SET id_person = NULL: Очищает поле владельца у конкретной книги.
//    Внешний ключ (id_person) зануляется, что означает: книга больше никому не принадлежит и свободна.
//    WHERE id_book = ?: Гарантирует, что изменения коснутся только одной выбранной книги по её уникальному номеру.
//            jdbcTemplate.update: Стандартный инструмент Spring для выполнения SQL-команд изменения данных.
    public void release(int id_book) {
        jdbcTemplate.update("UPDATE book SET  id_person =NULL WHERE id_book=?", id_book);
    }

    //Назаначает книгу человеку (этот метод вызывается, когда человек забирает книгу из библиотеки)
    public void assign(int id_book, Person selectedPerson) {
        jdbcTemplate.update("UPDATE  Book SET id_person=? WHERE id_book=?", selectedPerson.getId_person(), id_book);
    }
}












