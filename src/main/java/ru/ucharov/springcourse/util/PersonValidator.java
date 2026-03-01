package ru.ucharov.springcourse.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ucharov.springcourse.models.Person;
import ru.ucharov.springcourse.services.PeopleService;


@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports (Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        if(peopleService.getPersonByFio_person(person.getFioPerson()).isPresent() && peopleService.getPersonByFio_person(person.getFioPerson()).get().getId() != person.getId() )
            errors.rejectValue("fioPerson", "", "Человек с таким ФИО уже существует");
    }
}