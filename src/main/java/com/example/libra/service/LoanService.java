package com.example.libra.service;
import com.example.libra.dto.LoanRequest;
import com.example.libra.dto.LoanResponse;
import com.example.libra.dto.MemberResponse;
import com.example.libra.model.Loan;
import com.example.libra.model.LoanStatus;
import com.example.libra.model.Member;
import com.example.libra.repository.LoanRepo;
import com.example.libra.repository.MemberRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LoanService {
    private final LoanRepo loanRepo;
    private final MemberRepo memberRepo;

    private LoanResponse toResponse(Loan l) {
        LoanResponse res = new LoanResponse();
        res.setId(Long.valueOf(l.getId()));
        res.setLoanDate(l.getLoanDate());
        res.setDueDate(l.getDueDate());
        res.setStatus(l.getStatus());
        res.setBookId(l.getBook().getId());
        return res;
    }
    public List<LoanResponse> findByStatus(LoanStatus status){
        return (loanRepo.findByStatus(status)).stream().map(loan -> toResponse(loan)).collect(Collectors.toList());
    }

    public LoanResponse create(LoanRequest loanRequest) {
        //private Long bookId
        //private Long memberId; ------> loan request
        //private Integer loanDays;

        Long bookId = loanRequest.getBookId();
        Long memberId = loanRequest.getMemberId();

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
            l.setLoanDate(LocalDateTime.now());
            int loanDays = loanRequest.getLoanDays();
            l.setDueDate(LocalDateTime.now().plusDays(loanDays));
            l.setStatus(LoanStatus.ACTIVE);
            Loan saved = loanRepo.save(l);


        }
        return toResponse(l);


    }

}
