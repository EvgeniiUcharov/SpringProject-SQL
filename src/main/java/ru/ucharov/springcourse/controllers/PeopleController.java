package ru.ucharov.springcourse.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ucharov.springcourse.models.Person;
import jakarta.validation.Valid;
import ru.ucharov.springcourse.services.PeopleService;
import ru.ucharov.springcourse.util.PersonValidator;


/**
 * @author Ucharov Evgenii
 */
@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PeopleService peopleService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }

    @GetMapping("/{id_person}")
    public String show(@PathVariable("id_person") int id_person, Model model) {
        model.addAttribute("person", peopleService.findOne(id_person));
        model.addAttribute("books", peopleService.getBooksByPersonId(id_person));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/new";

        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id_person}/edit")
    public String edit(Model model, @PathVariable("id_person") int id_person) {
        model.addAttribute("person", peopleService.findOne(id_person));
        return "people/edit";
    }

    @PatchMapping("/{id_person}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id_person") int id_person) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/edit";

        peopleService.update(id_person, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id_person}")
    public String delete(@PathVariable("id_person") int id_person) {
        peopleService.delete(id_person);
        return "redirect:/people";
    }
}