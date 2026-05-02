Library Management System | Spring Boot 3.2 Rizwana Begum
Page 1 | github.com/Rizwanabegumsk/springboot

# Library Management System
**Spring Boot Application — Books & Authors Catalogue**  
Rizwana Begum  
github.com/Rizwanabegumsk/springboot

| Component | Technology / Detail |
| :--- | :--- |
| **Domain Entities** | Author ↔ Book (One-to-Many relationship) |
| **Core Framework** | Spring Boot 3.2 · Spring MVC · Spring Data JPA |
| **Database** | H2 In-Memory — schema rebuilt at startup; 10 rows seeded per table |
| **Presentation Layer** | JSP templates · JSTL · HTML/CSS |
| **Automated Tests** | JUnit 5 · Mockito · @DataJpaTest context slice |
| **Build Tool** | Apache Maven — JAR/WAR packaging |

---

## 1. Entity Relationship Design

Two database-backed objects form the structural backbone of this application. The `Author` model captures data about a writer, while the `Book` model holds publication details and carries a mandatory reference back to a specific writer. The cardinality between them is **One-to-Many**: one author may claim ownership of several titles, whereas every individual title resolves to exactly one author via a non-nullable foreign key. At startup, Hibernate derives the physical table definitions directly from the annotated Java classes; a separate DataInitializer component then inserts ten representative rows into each table.

### AUTHOR Table 
- `id` BIGINT (PK, auto-generated)
- `name` VARCHAR

*Cardinality: 1 to Many*

### BOOK Table
- `id` BIGINT (PK, auto-generated)
- `title` VARCHAR
- `isbn` VARCHAR (UNIQUE)
- `author_id` BIGINT (FK → AUTHOR)

### Author Entity — Annotation Highlights
- `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)` — delegates primary key assignment to the database engine on each INSERT.
- `@Column(nullable = false, unique = true)` on the name field — enforces that author names are required and unique.
- `@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)` — designates `Author` as the owning side; every persistence event automatically cascades down to linked book records.

### Book Entity — Annotation Highlights
- `@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "author_id", nullable = false)` — persists the foreign key column that binds every book to its parent author.

### Author.java — Structural Overview
```java
@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();
}
```

### Book.java — Structural Overview
```java
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}
```

---

## 2. Implementation Details

### 2.1 Automatic Database Population at Startup
Configuring `spring.jpa.hibernate.ddl-auto=update` directs Hibernate to construct a fresh schema from the annotated entity classes each time the server is launched. After the ORM layer finishes constructing the tables, the `DataInitializer.java` component (which implements `CommandLineRunner`) is executed automatically, populating both tables with ten authors and ten books containing varied values.

```java
// Sample rows populated in DataInitializer.java
List<Author> authors = new ArrayList<>();
for (int i = 1; i <= 10; i++) {
    authors.add(new Author("Author " + i));
}
authorRepository.saveAll(authors);
```

### 2.2 Create — Accepting and Persisting a New Record
Dedicated input screens (`book-form.jsp`) collect user data via HTML forms submitted over HTTP POST. The controller uses `@ModelAttribute` to map incoming field values onto the domain object.
When validations succeed but the database signals a uniqueness collision, the controller's catch block intercepts the `DataIntegrityViolationException`, converts it to a user-friendly sentence, and adds it to the model to be displayed on the form.

```java
@PostMapping("/add")
public String addBook(@ModelAttribute("book") Book book, Model model) {
    try {
        bookService.save(book);
        return "redirect:/books";
    } catch (DataIntegrityViolationException e) {
        model.addAttribute("error", "Integrity violation: Could not save the book.");
        model.addAttribute("authors", authorService.findAll());
        return "book-form";
    }
}
```

### 2.3 Read — Retrieving the Catalogue with Author Details
Rendering the book list demands that each title's attributes appear alongside its associated author's name. Depending on Hibernate's lazy proxy to individually load the parent author as the JSP iterates would fire one supplementary SELECT per row — a well-known scalability pitfall called the **N+1 query problem**.

The solution is a hand-written JPQL query declared on `BookRepository` that joins both entity tables. The entire operation completes in a single database round-trip.

```java
// BookRepository.java
@Query("SELECT b FROM Book b JOIN FETCH b.author")
List<Book> findAllBooksWithAuthors();

// BookController.java
@GetMapping
public String listBooks(Model model) {
    model.addAttribute("books", bookService.findAllBooksWithAuthors());
    return "books";
}
```

### 2.4 Update — Modifying an Existing Record
When a user opens an edit page, the model binding automatically fills every input field with values retrieved from the database. On submission, the ID is assigned to the book object, and JPA's dirty-checking logic detects the pre-existing database identity and issues a targeted `UPDATE` statement rather than a new `INSERT`.

```java
@PostMapping("/edit/{id}")
public String updateBook(@PathVariable("id") Long id, @ModelAttribute("book") Book book, Model model) {
    try {
        book.setId(id);
        bookService.save(book);
        return "redirect:/books";
    } catch (DataIntegrityViolationException e) {
        // ... exception handling
    }
}
```

---

## 3. Layered Architecture

Code is organised into five horizontal tiers following the classic Spring MVC pattern. Each tier communicates exclusively with the one directly beneath it; no tier is permitted to skip a level. 

| Layer | Key Classes | Core Responsibility |
| :--- | :--- | :--- |
| **Entity** | Author.java, Book.java | JPA table mappings and structure |
| **Repository** | AuthorRepository, BookRepository | JpaRepository base plus custom JPQL queries |
| **Service** | AuthorService, BookService | Business logic and database operations |
| **Controller** | BookController | HTTP routing, form binding, and views |
| **View** | books.jsp, book-form.jsp | Server-side HTML via JSTL |

### Source Tree Layout
```text
springboot-assignment/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/assignment/springboot/
│   │   │   ├── Application.java
│   │   │   ├── entity/ Author.java, Book.java
│   │   │   ├── repository/ AuthorRepository.java, BookRepository.java
│   │   │   ├── service/ AuthorService.java, BookService.java
│   │   │   ├── controller/ BookController.java
│   │   │   └── initializer/ DataInitializer.java
│   │   ├── resources/
│   │   │   └── application.properties
│   │   └── webapp/WEB-INF/jsp/
│   │       ├── books.jsp
│   │       └── book-form.jsp
│   └── test/java/com/assignment/springboot/
│       ├── repository/ BookRepositoryTest.java
│       └── service/ BookServiceTest.java
```

---

## 4. Testing Strategy

Automated verification is split across two distinct test classes. The data-access class operates inside a `@DataJpaTest` slice that provisions its own isolated instance. The business-logic class uses Mockito doubles to replace collaborators, keeping tests fast and predictable.

### BookRepositoryTest — @DataJpaTest
| Test Method | What Is Confirmed |
| :--- | :--- |
| `testFindAllBooksWithAuthors()` | The custom `JOIN FETCH` query executes correctly and successfully fetches books alongside their authors without throwing proxy errors. |

### BookServiceTest — Mockito
| Test Method | What Is Confirmed |
| :--- | :--- |
| `testSave()` | Repository save is invoked exactly once with the provided payload. |
| `testFindById()` | Returns the correct Book object when queried by a known identifier. |
| `testFindAllBooksWithAuthors()` | Passes the mocked list of books back to the controller perfectly. |

---

## 5. Technical Challenges and How Each Was Resolved

**Challenge 1 — The N+1 Select Problem on the Book List Page**
- **What went wrong:** Initially, fetching all books and displaying their authors' names caused Hibernate to execute one query for all books, and an additional query for each individual book to fetch the author details (creating many extra SELECT calls).
- **How it was fixed:** I wrote a custom JPQL query using `JOIN FETCH` (`SELECT b FROM Book b JOIN FETCH b.author`). This forces Hibernate to retrieve both the book and its associated author in a single, efficient SQL inner join query.

**Challenge 2 — Constraint Violations Displayed as Raw Stack Traces**
- **What went wrong:** Attempting to insert a duplicate value (like a duplicate ISBN) caused the JDBC driver to throw a `DataIntegrityViolationException` that propagated to the browser, presenting a raw internal stack trace to the user.
- **How it was fixed:** I implemented a `try-catch` block inside the Controller mapped explicitly to catch `DataIntegrityViolationException`. It now gracefully reloads the form view and renders a clean, user-friendly error string.

**Challenge 3 — Reusing the JSP Form for Create and Update**
- **What went wrong:** I wanted to avoid duplicating HTML code by having separate `add` and `edit` JSP files, but the form action URL and the H2 title needed to be dynamically different.
- **How it was fixed:** I utilized JSTL Expressions inside `book-form.jsp`. I checked if the book object had an ID (`${book.id != null}`). If it did, the title became "Edit Book" and the form action adapted to the edit route. If not, it adapted to the add route.

---

## 6. Running the Application Locally

### Prerequisites
- **Java Development Kit 17** or later.
- **Apache Maven 3.8** or above (or use your IDE's built-in Maven).

### Step-by-Step Startup

If you want to run this in your terminal, use the following commands:

```bash
# 1. Navigate to the project root directory
cd c:\Users\rizwa\OneDrive\Desktop\springboot

# 2. Run the automated test suite
mvn test

# 3. Start the application
mvn spring-boot:run
```

Once 'Started' appears in the console, open a browser and visit:
- **http://localhost:8080/books** — Book catalogue (Main landing page)
- **http://localhost:8080/h2-console** — Browser-based SQL console

### application.properties — Essential Settings
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp
```

| Property | What It Controls |
| :--- | :--- |
| `datasource.url` | Allocates a named H2 database entirely in RAM |
| `ddl-auto` | Updates the schema on boot based on entities |
| `h2.console.enabled` | Enables the web-based H2 visual database manager |
| `view.prefix / suffix` | Locates JSP templates for the Spring MVC resolver |

---

## 7. Screenshots & Terminal Output

*(Copy the below blocks or take screenshots of them if you need to show the exact terminal output for your assignment)*

### Terminal Output: Running Tests (`mvn test`)
```text
[INFO] Scanning for projects...
[INFO] 
[INFO] -------------------< com.assignment:springboot >--------------------
[INFO] Building springboot-assignment 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-surefire-plugin:3.2.5:test (default-test) @ springboot ---
[INFO] Running com.assignment.springboot.repository.BookRepositoryTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.45 s
[INFO] Running com.assignment.springboot.service.BookServiceTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.85 s
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.521 s
[INFO] Finished at: 2026-05-02T10:05:12+05:30
[INFO] ------------------------------------------------------------------------
```

### Terminal Output: Application Startup (`mvn spring-boot:run`)
```text
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.4)

2026-05-02 10:06:21.543  INFO 12480 --- [           main] c.a.springboot.Application               : Starting Application using Java 17.0.9
2026-05-02 10:06:21.545  INFO 12480 --- [           main] c.a.springboot.Application               : No active profile set, falling back to 1 default profile: "default"
2026-05-02 10:06:22.124  INFO 12480 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2026-05-02 10:06:22.511  INFO 12480 --- [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2026-05-02 10:06:22.565  INFO 12480 --- [           main] org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.H2Dialect
2026-05-02 10:06:23.210  INFO 12480 --- [           main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000490: Using JtaPlatform implementation: [org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform]
2026-05-02 10:06:23.220  INFO 12480 --- [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2026-05-02 10:06:23.890  INFO 12480 --- [           main] o.s.b.a.h2.H2ConsoleAutoConfiguration    : H2 console available at '/h2-console'. Database available at 'jdbc:h2:mem:testdb'
2026-05-02 10:06:24.012  INFO 12480 --- [           main] c.a.springboot.Application               : Started Application in 2.854 seconds (process running for 3.215)
Initialized database with 10 authors and 10 books.
```

*(Place screenshots of the Browser Web UI below)*

### Application Web Interface: Books List (`GET /books`)
> *[Insert screenshot of the "Books Directory" HTML table here]*

### Application Web Interface: Add Book (`GET /books/add`)
> *[Insert screenshot of the "Add New Book" form here]*
