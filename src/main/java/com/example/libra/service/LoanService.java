package com.example.libra.service;
import com.example.libra.dto.LoanRequest;
import com.example.libra.dto.LoanResponse;
import com.example.libra.event.LoanCreatedEvent;
import com.example.libra.model.Loan;
import com.example.libra.model.Book;
import com.example.libra.model.LoanStatus;
import com.example.libra.model.Member;
import com.example.libra.repository.BookRepo;
import com.example.libra.repository.LoanRepo;
import com.example.libra.repository.MemberRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LoanService {
    private final LoanRepo loanRepo;
    private final MemberRepo memberRepo;
    private final BookRepo bookRepo;
    private final KafkaProducerService kafkaProducerService; // Add this

    private LoanResponse toResponse(Loan l) {
        LoanResponse res = new LoanResponse();
        res.setId(Long.valueOf(l.getId()));
        res.setLoanDate(l.getLoanDate());
        res.setDueDate(l.getDueDate());
        res.setStatus(l.getStatus());
        res.setBookId(l.getBook() != null ? l.getBook().getId() : null);
        return res;
    }
    
    public List<LoanResponse> findByStatus(LoanStatus status){
        return (loanRepo.findByStatus(status)).stream().map(loan -> toResponse(loan)).collect(Collectors.toList());
    }

    public LoanResponse create(LoanRequest loanRequest) {
        Long bookId = loanRequest.getBookId();
        Long memberId = loanRequest.getMemberId();
        var book = bookRepo.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found: " + bookId));

        Loan l;
        if (loanRepo.existsByBookIdAndMemberId(bookId, memberId)) {
            throw new RuntimeException("The book is already loaned by the exact user.");
        } else {
            l = new Loan();
            Member m;
            if (memberRepo.findById(memberId).isPresent()){
                m = memberRepo.findById(memberId).get();
            }
            else {
                throw new RuntimeException("The member is not found.");
            }
            l.setMember(m);
            l.setBook(book);
            l.setLoanDate(LocalDateTime.now());
            int loanDays = loanRequest.getLoanDays();
            l.setDueDate(LocalDateTime.now().plusDays(loanDays));
            l.setStatus(LoanStatus.ACTIVE);
            Loan saved = loanRepo.save(l);
            
            // Publish Kafka event after saving
            LoanCreatedEvent event = LoanCreatedEvent.builder()
                .loanId(Long.valueOf(saved.getId()))
                .bookId(bookId)
                .memberId(memberId)
                .loanDate(saved.getLoanDate())
                .dueDate(saved.getDueDate())
                .status(saved.getStatus())
                .build();
            
            kafkaProducerService.sendLoanCreatedEvent(event);
            
            return toResponse(saved);
        }
    }
    public LoanResponse returnLoan(LoanRequest loanRequest) {
        Long loanId = loanRequest.getBookId();
        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found: " + loanId));
        loan.setStatus(LoanStatus.RETURNED);
        Loan saved = loanRepo.save(loan);
        return toResponse(saved);
    }
    public void makeBookAvailable(Long bookId) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found: " + bookId));
        book.setAvailable(true);
        bookRepo.save(book);
    }
}