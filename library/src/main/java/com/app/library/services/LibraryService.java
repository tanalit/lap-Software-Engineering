package com.app.library.services;

import com.app.library.models.Book;
import com.app.library.models.Member;
import com.app.library.models.BorrowingRecord;
import com.app.library.repositories.BookRepository;
import com.app.library.repositories.MemberRepository;
import com.app.library.repositories.BorrowingRecordRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibraryService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BorrowingRecordRepository recordRepository;

    // ==================== Book Methods ====================

    // Get all books
    public Collection<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get a book by ID
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    // Add a new book
    public void addBook(Book book) {
        // Ensure we persist a new entity regardless of client-provided id
        book.setId(null);
        bookRepository.save(book);
    }

    // Update a book
    public void updateBook(Book updatedBook) {
        bookRepository.save(updatedBook);
    }

    // Delete a book by ID
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // ==================== Member Methods ====================

    // Get all members
    public Collection<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // Get a member by ID
    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    // Add a new member
    public void addMember(Member member) {
        // Ensure we persist a new entity regardless of client-provided id
        member.setId(null);
        memberRepository.save(member);
    }

    // Update a member
    public void updateMember(Member updatedMember) {
        memberRepository.save(updatedMember);
    }

    // Delete a member by ID
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    // ==================== BorrowingRecord Methods ====================

    // Get all borrowing records
    public Collection<BorrowingRecord> getAllBorrowingRecords() {
        return recordRepository.findAll();
    }

    // Borrow a book (create a new borrowing record)
    public void borrowBook(BorrowingRecord record) {
        if (record.getBorrowDate() == null) {
            record.setBorrowDate(LocalDate.now());
        }
        if (record.getDueDate() == null) {
            record.setDueDate(record.getBorrowDate().plusDays(14));
        }

        Long bId = record.getBookId();
        Book book = bookRepository.findById(bId).orElseThrow(() -> new IllegalArgumentException("Book with id " + bId + " not found"));
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies for book id " + bId);
        }

        BorrowingRecord saved = recordRepository.save(record);
        record.setId(saved.getId());

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
    }

    // Return a book (update the borrowing record with the return date)
    public void returnBook(Long recordId, LocalDate returnDate) {
        BorrowingRecord record = recordRepository.findById(recordId).orElseThrow(() -> new IllegalArgumentException("Borrowing record not found: " + recordId));
        record.setReturnDate(returnDate);
        recordRepository.save(record);

        Book book = bookRepository.findById(record.getBookId()).orElse(null);
        if (book != null) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);
        }
    }

    // Get books by genre
    public Collection<Book> getBooksByGenre(String genre) {
        if (genre == null) return getAllBooks();
        return bookRepository.findByGenre(genre);
    }

    // Get books by author and optional genre
    public Collection<Book> getBooksByAuthorAndGenre(String author, String genre) {
        if (author == null) return getAllBooks();
        if (genre == null) {
            return bookRepository.findByAuthor(author);
        } else {
            return bookRepository.findByAuthorAndGenre(author, genre);
        }
    }

    // Get books that are due on a specific date
    public Collection<Book> getBooksDueOnDate(LocalDate dueDate) {
        List<BorrowingRecord> records = recordRepository.findByDueDate(dueDate);
        return records.stream()
                .map(r -> bookRepository.findById(r.getBookId()).orElse(null))
                .filter(b -> b != null)
                .toList();
    }

    // Check earliest availability date for a book
    public LocalDate checkAvailability(Long bookId) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) return null;
        if (book.getAvailableCopies() > 0) return LocalDate.now();

        List<BorrowingRecord> active = recordRepository.findByBookIdAndReturnDateIsNull(bookId);
        return active.stream()
                .map(BorrowingRecord::getDueDate)
                .filter(d -> d != null)
                .min(LocalDate::compareTo)
                .orElse(null);
    }
}
