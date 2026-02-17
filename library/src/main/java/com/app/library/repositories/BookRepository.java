package com.app.library.repositories;

import com.app.library.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findByGenre(String genre);
	List<Book> findByAuthor(String author);
	List<Book> findByAuthorAndGenre(String author, String genre);
}
