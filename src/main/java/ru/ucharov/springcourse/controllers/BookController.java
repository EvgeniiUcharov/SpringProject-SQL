package ru.ucharov.springcourse.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ucharov.springcourse.models.Book;
import ru.ucharov.springcourse.models.Person;
import jakarta.validation.Valid;
import ru.ucharov.springcourse.services.BooksService;
import ru.ucharov.springcourse.services.PeopleService;


/**
 * @author Ucharov Evgenii
 */
@Controller
@RequestMapping("/books")
public class BookController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/books";
    }

    @GetMapping()
    public String index(Model model, @RequestParam(value = "page", defaultValue = "1", required = false)  Integer page,
                        @RequestParam(value = "books_per_page", required = false)  Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {
        if(page <1) page = 1;
        if(booksPerPage == null)
            model.addAttribute("books", booksService.findAll(sortByYear));
        else
            model.addAttribute("books", booksService.findWithPagination(page -1, booksPerPage, sortByYear));
        model.addAttribute("currentPage", page);
        model.addAttribute("booksPerPage", booksPerPage);
        model.addAttribute("sortByYear", sortByYear);
        return "books/index";
    }

    @GetMapping("/{id_book}")
    public String show(@PathVariable("id_book") int id_book, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findOne(id_book));

        Person bookOwner = booksService.getBookOwner(id_book);

        if(bookOwner != null) {
            model.addAttribute("owner", bookOwner);
        } else {
            model.addAttribute("people", peopleService.findAll());
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

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id_book}/edit")
    public String edit(Model model, @PathVariable("id_book") int id_book) {
        model.addAttribute("book", booksService.findOne(id_book));
        return "books/edit";
    }

    @PatchMapping("/{id_book}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id_book") int id_book) {
        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(id_book, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id_book}")
    public String delete(@PathVariable("id_book") int id_book) {
        booksService.delete(id_book);
        return "redirect:/books";
    }

    @PatchMapping("/{id_book}/release")
    public String release(@PathVariable("id_book") int id_book) {
        booksService.release(id_book);
        return "redirect:/books/" + id_book;
    }

    @PatchMapping("/{id_book}/assign")
    public String assign(@PathVariable("id_book") int id_book, @ModelAttribute("person") Person selectedPerson) {
        booksService.assign(id_book, selectedPerson);
        return "redirect:/books/" + id_book;
    }

    @GetMapping("/search")
    public String searchPage() {
        return "books/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("query") String query) {
        model.addAttribute("books", booksService.searchByTitle(query));
        return "books/search";
    }



}