# Проект 2: Веб-приложение "Библиотека" (SQL → Hibernate/JPA)


Учебный проект “Библиотека”, в котором один и тот же функционал реализован **в двух вариантах**:

- **Вариант 1 (первый проект)** — работа с БД через **чистые SQL‑запросы и DAO**.
- **Вариант 2 (ветка `refactor/spring-hibernate`)** — тот же функционал переписан на **Spring Data JPA + Hibernate**.

Текущая ветка содержит рефакторинг на Hibernate/JPA, а также дополнительные возможности: **поиск книг по названию**, **пагинация и сортировка списка книг**, улучшенный UI на Thymeleaf.

## Эволюция проекта

- **Исходный вариант (SQL / DAO)**:
  - Ручные SQL‑запросы (`SELECT`, `INSERT`, `UPDATE`, `DELETE`) в DAO‑классах.
  - Ручное управление маппингом ResultSet → объекты.
  - Основной акцент — понимание JDBC и работы с SQL.

- **Рефакторинг (ветка `refactor/spring-hibernate`)**:
  - Введены JPA‑сущности `Person` и `Book` (`@Entity`).
  - Добавлены репозитории `BooksRepository`, `PeopleRepository` (на базе `JpaRepository`).
  - Введён сервисный слой `BooksService`, `PeopleService` с транзакциями (`@Transactional`).
  - Конфигурация вынесена в `SpringConfig` (DataSource, EntityManagerFactory, TransactionManager, Thymeleaf).
  - DAO‑уровень на чистом SQL заменён на Hibernate/JPA.

### Новые возможности во втором варианте

- **Поиск книг по названию**:
  - Контроллер: `POST /books/search`
  - Репозиторий: метод `findByTitleStartingWith(String title)`
  - Сервис: `BooksService.searchByTitle(query)`
  - Шаблон: `WEB-INF/views/books/search.html` с красивым интерфейсом поиска и отображением статуса книги (свободна / у кого‑то).

- **Пагинация и сортировка списка книг**:
  - Контроллер: `GET /books?page=&books_per_page=&sort_by_year=`
  - Сервис: методы `findAll(boolean sortByYear)` и `findWithPagination(page, booksPerPage, sortByYear)` на основе `PageRequest` и `Sort`.
  - Репозиторий: использование `findAll(Pageable)` и сортировки по полю `year`.
  - Шаблон: `WEB-INF/views/books/index.html` с кнопками “Назад/Вперёд” и сохранением параметров фильтрации/сортировки.

- **Дополнительная логика в домене**:
  - У книги есть поля `owner`, `takenAt`, `expired` (просрочена ли выдача).
  - В `PeopleService.getBooksByPersonId(...)`:
    - Ленивые коллекции инициализируются через `Hibernate.initialize(...)`.
    - Для каждой книги считается, не превышен ли срок выдачи; при просрочке выставляется флаг `expired`.

## Текущий стек (ветка refactor/spring-hibernate)

- **Java**: 21  
- **Spring Framework**: 6.x (Spring MVC, Spring ORM)  
- **Spring Data JPA**  
- **Hibernate ORM**  
- **Thymeleaf**  
- **База данных**: PostgreSQL  
- **Деплой**: WAR на Apache Tomcat 11  

## Функциональность (актуальная реализация)

- **Люди (`/people`)**:
  - список / создание / редактирование / удаление
  - страница человека со списком взятых им книг
  - проверка уникальности ФИО через `PersonValidator` и `PeopleService.getPersonByFio_person(...)`

- **Книги (`/books`)**:
  - список / создание / редактирование / удаление
  - страница книги: если свободна — можно назначить владельца; если занята — можно “освободить”
  - поиск по названию (`/books/search`)
  - пагинация и сортировка по году издания (`page`, `books_per_page`, `sort_by_year`)
  - отображение владельца и статуса книги

- **Валидация форм**:
  - Jakarta Validation (`@Valid`, аннотации на полях сущностей)
  - собственный валидатор `PersonValidator` для проверки уникальности читателя

## Структура проекта (ветка refactor/spring-hibernate)

- `src/main/java/ru/ucharov/springcourse/controllers` — MVC контроллеры
- `src/main/java/ru/ucharov/springcourse/services` — сервисный слой
- `src/main/java/ru/ucharov/springcourse/repositories` — Spring Data репозитории
- `src/main/java/ru/ucharov/springcourse/models` — JPA сущности
- `src/main/java/ru/ucharov/springcourse/config` — конфигурация Spring (`SpringConfig`)
- `src/main/webapp/WEB-INF/views` — Thymeleaf шаблоны
- `src/main/resources/hibernate.properties` — параметры подключения к БД и Hibernate

## База данных

### Таблицы (логическая модель)

- `person`:
  - `id` (PK)
  - `fio` (UNIQUE)
  - `year_of_birth`
- `book`:
  - `id` (PK)
  - `title`
  - `author`
  - `year`
  - `person_id` (FK → `person.id`, может быть `NULL`)

## Настройка подключения (hibernate.properties)

Файл: `src/main/resources/hibernate.properties`

Пример:

```properties
driver=org.postgresql.Driver
url=jdbc:postgresql://localhost:5432/project1
username=postgres
password=postgres

hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
hibernate.show_sql=true
```

## Сборка и запуск

### Требования

- Установлены **JDK 21**, **Maven 3.9+**
- Установлен и настроен **PostgreSQL**
- Установлен **Tomcat 11** (или через конфигурацию сервера в IntelliJ IDEA)

### Сборка WAR

```bash
mvn clean package
```

WAR появится в `target/` (например, `target/spring-hibernate.war`).

### Деплой на Tomcat

- Задеплойте WAR в Tomcat (через IntelliJ или копированием в `tomcat/webapps`)
- Перезапустите сервер
- Откройте приложение:
  - `http://localhost:8080/people`
  - `http://localhost:8080/books`

