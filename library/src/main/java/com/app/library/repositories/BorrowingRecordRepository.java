package com.app.library.repositories;

import com.app.library.models.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
	List<BorrowingRecord> findByDueDate(LocalDate dueDate);
	List<BorrowingRecord> findByBookIdAndReturnDateIsNull(Long bookId);
}
