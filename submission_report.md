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

Two database-backed objects form the structural backbone of this application. The `Author` model captures data about a writer, while the `Book` model holds publication details and carries a mandatory reference back to a specific writer. The cardinality between them is **One-to-Many**: one author may claim ownership of several titles, whereas every individual title resolves to exactly one author via a non-nullable foreign key. At startup, Hibernate derives the physical table definitions directly from the annotated Java classes; a separate `data.sql` script then inserts ten representative rows into each table.

### AUTHOR Table 
- `id` BIGINT (PK, auto-generated)
- `name` VARCHAR
- `nationality` VARCHAR

*Cardinality: 1 to Many*

### BOOK Table
- `id` BIGINT (PK, auto-generated)
- `title` VARCHAR
- `genre` VARCHAR
- `published_year` INT
- `isbn` VARCHAR (UNIQUE)
- `author_id` BIGINT (FK → AUTHOR)

### Author Entity — Annotation Highlights
- `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)` — delegates primary key assignment to the database engine on each INSERT.
- `@NotBlank` on name — ensures author names are required.
- `@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)` — designates `Author` as the side where changes cascade.

### Book Entity — Annotation Highlights
- `@ManyToOne @JoinColumn(name = "author_id", nullable = false)` — persists the foreign key column that binds every book to its parent author.
- `@Column(unique = true)` on `isbn` — prevents duplicate ISBN entries.

### Author.java — Structural Overview
```java
@Entity
@Table(name = "author")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    private String nationality;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Book> books;
}
```

### Book.java — Structural Overview
```java
@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String genre;
    @Column(unique = true)
    @NotBlank
    private String isbn;
    private int publishedYear;
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}
```

---

## 2. Implementation Details

### 2.1 Automatic Database Population at Startup
Configuring `spring.jpa.hibernate.ddl-auto=create-drop` directs Hibernate to construct a fresh schema from the annotated entity classes each time the server is launched. After the ORM layer finishes constructing the tables, the `data.sql` script is executed automatically, populating both tables with ten authors and ten books.

```sql
-- Sample rows populated in data.sql
INSERT INTO author (name, nationality) VALUES ('George Orwell', 'British');
INSERT INTO book (title, genre, published_year, isbn, author_id) VALUES ('1984', 'Dystopian', 1949, '978-0451524935', 1);
```

### 2.2 Create — Accepting and Persisting a New Record
Dedicated input screens (`add.jsp`) collect user data via HTML forms submitted over HTTP POST. The controller uses `@ModelAttribute` to map incoming field values onto the domain object.

```java
@PostMapping("/add")
public String addBook(@ModelAttribute("book") Book book, @RequestParam("authorId") Long authorId, RedirectAttributes ra) {
    try {
        book.setAuthor(authorService.getAuthorById(authorId));
        bookService.saveBook(book);
        ra.addFlashAttribute("successMessage", "New book saved!");
    } catch (Exception ex) {
        ra.addFlashAttribute("errorMessage", ex.getMessage());
    }
    return "redirect:/books";
}
```

### 2.3 Read — Retrieving the Catalogue with Author Details
Rendering the book list demands that each title's attributes appear alongside its associated author's name. Depending on Hibernate's lazy proxy to individually load the parent author as the JSP iterates would fire one supplementary SELECT per row — a well-known scalability pitfall called the **N+1 query problem**.

The solution is a hand-written JPQL query declared on `BookRepository` that joins both entity tables and projects the results into a `BookWithAuthorDTO`.

```java
// BookRepository.java
@Query("SELECT new com.library.entity.BookWithAuthorDTO("
        + "b.id, b.title, b.genre, b.publishedYear, b.isbn,"
        + "a.id, a.name, a.nationality)"
        + " FROM Book b INNER JOIN b.author a")
List<BookWithAuthorDTO> fetchBooksWithWriterInfo();

// BookController.java
@GetMapping
public String showBooks(Model model) {
    model.addAttribute("catalogue", bookService.getAllBooksWithAuthorDetails());
    return "books/list";
}
```

### 2.4 Update — Modifying an Existing Record
When a user opens an edit page, the model binding automatically fills every input field with values retrieved from the database. On submission, the service updates the record and JPA issues a targeted `UPDATE` statement.

---

## 3. Layered Architecture

Code is organised into five horizontal tiers following the classic Spring MVC pattern.

| Layer | Key Classes | Core Responsibility |
| :--- | :--- | :--- |
| **Entity** | Author.java, Book.java | JPA table mappings and structure |
| **Repository** | AuthorRepository, BookRepository | JpaRepository base plus custom JPQL queries |
| **Service** | AuthorService, BookService | Business logic and database operations |
| **Controller** | AuthorController, BookController | HTTP routing, form binding, and views |
| **View** | 6 JSP templates | Server-side HTML via JSTL |

### Source Tree Layout
```text
library-management/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/library/
│   │   │   ├── LibraryApplication.java
│   │   │   ├── entity/ Author.java, Book.java, BookWithAuthorDTO.java
│   │   │   ├── repository/ AuthorRepository.java, BookRepository.java
│   │   │   ├── service/ AuthorService.java, BookService.java
│   │   │   └── controller/ AuthorController.java, BookController.java
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   └── data.sql
│   │   └── webapp/WEB-INF/views/
│   │       ├── books/ list.jsp, add.jsp, edit.jsp
│   │       └── authors/ list.jsp, add.jsp
│   └── test/java/com/library/
│       ├── repository/ BookRepositoryTest.java
│       └── service/ BookServiceTest.java
```

---

## 4. Testing Strategy

Automated verification is split across two distinct test classes.

### BookRepositoryTest — @DataJpaTest
| Test Method | What Is Confirmed |
| :--- | :--- |
| `testFetchBooksWithWriterInfo()` | Confirms the custom DTO projection query works and correctly joins Author data. |

### BookServiceTest — Mockito
| Test Method | What Is Confirmed |
| :--- | :--- |
| `testGetAllBooksWithAuthorDetails()` | Ensures the service correctly interacts with the repository and returns data. |

---

## 5. Technical Challenges and How Each Was Resolved

**Challenge 1 — Jakarta EE Namespace Migration**
- **What went wrong:** Upgrading to Spring Boot 3 required using `jakarta.*` packages instead of `javax.*`, which affected JSP tag libraries and dependencies.
- **How it was fixed:** Updated `pom.xml` with the correct Jakarta-compliant JSTL and Tomcat Jasper dependencies.

**Challenge 2 — The N+1 Select Problem**
- **What went wrong:** Standard lazy loading of authors for every book in a list caused multiple database calls.
- **How it was fixed:** Implemented a DTO projection with an `INNER JOIN` in JPQL to fetch all required data in one trip.

---

## 6. Running the Application Locally

### Step-by-Step Startup
```bash
# 1. Start the application
mvn spring-boot:run
```
Visit: **http://localhost:8080/books**

---

## 7. Screenshots & Terminal Output

### Terminal Output: Running Tests (`mvn test`)
```text
[INFO] BUILD SUCCESS
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

### Terminal Output: Application Startup (`mvn spring-boot:run`)
```text
2026-05-02 10:06:24.012  INFO 12480 --- [main] c.library.LibraryApplication : Started LibraryApplication
```

---

## 8. Source Code Repository

**GitHub URL:** [https://github.com/Rizwanabegumsk/springboot](https://github.com/Rizwanabegumsk/springboot)
