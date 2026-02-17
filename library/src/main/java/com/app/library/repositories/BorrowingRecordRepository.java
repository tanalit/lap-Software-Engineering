package com.app.library.repositories;

import com.app.library.models.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {

}
