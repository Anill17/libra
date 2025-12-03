package com.example.libra.controller;

import com.example.libra.dto.LoanRequest;
import com.example.libra.dto.LoanResponse;
import com.example.libra.dto.StatusRequestDto;
import com.example.libra.model.LoanStatus;
import com.example.libra.repository.BookRepo;
import com.example.libra.repository.LoanRepo;
import com.example.libra.service.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/loan")
public class LoanController {

    private final LoanService loanService;

    @GetMapping("/status")
    public List<LoanResponse> retrieveByStatus(@RequestBody LoanStatus status) {
        return (loanService.findByStatus(status));
    }
    @PostMapping("/create")
    public LoanResponse CreateLoan(@RequestBody LoanRequest loan) {
        return loanService.create(loan);
    }
//testt.123
}
