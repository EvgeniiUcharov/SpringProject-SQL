package ru.ucharov.springcourse.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ucharov.springcourse.dao.PersonDAO;
import ru.ucharov.springcourse.models.Book;
import ru.ucharov.springcourse.models.Person;
import jakarta.validation.Valid;
import ru.ucharov.springcourse.dao.BookDAO;

import java.util.Optional;


/**
 * @author Ucharov Evgenii
 */
@Controller
@RequestMapping("/books")
public class BookController {

    private final PersonDAO personDAO;
    private final BookDAO bookDAO;

    @Autowired
    public BookController(PersonDAO personDAO, BookDAO bookDAO) {
        this.personDAO = personDAO;
        this.bookDAO = bookDAO;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/books";
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("books", bookDAO.index());
        return "books/index";
    }

    @GetMapping("/{id_book}")
    public String show(@PathVariable("id_book") int id_book, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookDAO.show(id_book));

        Optional<Person> bookOwner = bookDAO.getBookOwner(id_book);

        if(bookOwner.isPresent()) {
            model.addAttribute("owner", bookOwner.get());
        } else {
            model.addAttribute("people", personDAO.index());
        }
        return "books/show";
    }

    @GetMapping("/new") //метод сработает, когда пользователь перейдет по адресу /books/new.
    public String newBook(@ModelAttribute("book") Book book) { //Spring создает новый пустой объект класса Book и этот объект автоматически добавляется в модель под именем "book"
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";

        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id_book}/edit")
    public String edit(Model model, @PathVariable("id_book") int id_book) {
        model.addAttribute("book", bookDAO.show(id_book));
        return "books/edit";
    }

    @PatchMapping("/{id_book}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id_book") int id_book) {
        if (bindingResult.hasErrors())
            return "books/edit";

        bookDAO.update(id_book, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id_book}")
    public String delete(@PathVariable("id_book") int id_book) {
        bookDAO.delete(id_book);
        return "redirect:/books";
    }

    @PatchMapping("/{id_book}/release")
    public String release(@PathVariable("id_book") int id_book) {
        bookDAO.release(id_book);
        return "redirect:/books/" + id_book;

        //region Description
        //@PathVariable - это аннотация в Spring, которая извлекает значение из самой
        // адресной строки (URL) и передает его в переменную метода.
//        Откуда берет: Из части пути, помеченной {id_book} в @Mapping.
//                Пример URL: В адресе example.com/books/15 значением будет 15.
//        Зачем нужна: Чтобы контроллер понимал, с каким конкретным объектом (ID) нужно работать.

//        public String release() - этот метод освобождает книгу, когда её возвращают в библиотеку:
//        bookDAO.release(id_book) — выполняет SQL-запрос UPDATE, устанавливая id_person = NULL для этой книги в базе данных (книга становится свободной).
//        redirect — обновляет страницу этой же книги, чтобы пользователь сразу увидел изменения (например, надпись «Книга свободна» вместо имени владельца).

        //endregion

    }

    @PatchMapping("/{id_book}/assign")
    public String assign(@PathVariable("id_book") int id_person, @ModelAttribute("person") Person selectedPerson) {
        bookDAO.assign(id_person, selectedPerson);
        return "redirect:/books/" + id_person;
    }


}