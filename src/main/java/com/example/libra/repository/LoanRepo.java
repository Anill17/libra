package com.example.libra.repository;


import com.example.libra.model.Loan;
import com.example.libra.model.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepo extends JpaRepository<Loan, Long> {
    List<Loan> findByStatus(LoanStatus status);

    List<Loan> findByMemberIdAndStatus(Long memberId, LoanStatus status);

    // Find loans by book + status (check if a book is currently borrowed)
    List<Loan> findByBookIdAndStatus(Long bookId, LoanStatus status);

    Boolean existsByBookIdAndMemberId(Long bookId, Long memberId);

}
