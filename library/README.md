# Library Application (Lab4)

This project is the Library Spring Boot application used in the Lab4 instructions.

Quick start

1. Build the project:

```bash
mvn clean install
```

2. Run the application:

```bash
mvn -f pom.xml spring-boot:run
```

API endpoints (added in this lab)

- `GET /api/books` — returns all books; optional query params `author` and `genre` to filter.
- `GET /api/books/genre?genre={genre}` — returns books matching genre.
- `GET /api/books/author/{author}` — returns books by author; optional `?genre=` filter.
- `GET /api/books/dueondate?dueDate=dd/MM/yyyy` — returns books due on the given date.
- `GET /api/bookavailabileDate?bookId={id}` — returns earliest availability date or 404 if not found.

Notes

- The application uses in-memory HashMaps; data is not persisted across restarts.
- Use Postman or `curl` to test endpoints.
