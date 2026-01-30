package ru.ucharov.springcourse.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ucharov.springcourse.dao.PersonDAO;
import ru.ucharov.springcourse.models.Person;
import jakarta.validation.Valid;


/**
 * @author Ucharov Evgenii
 */
@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/people";
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping("/{id_person}")
    public String show(@PathVariable("id_person") int id_person, Model model) {
        model.addAttribute("person", personDAO.show(id_person));
        model.addAttribute("books", personDAO.getBooksByPersonId(id_person));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "people/new";

        personDAO.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id_person}/edit")
    public String edit(Model model, @PathVariable("id_person") int id_person) {
        model.addAttribute("person", personDAO.show(id_person));
        return "people/edit";
    }

    @PatchMapping("/{id_person}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id_person") int id_person) {
        if (bindingResult.hasErrors())
            return "people/edit";

        personDAO.update(id_person, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id_person}")
    public String delete(@PathVariable("id_person") int id_person) {
        personDAO.delete(id_person);
        return "redirect:/people";
    }
}