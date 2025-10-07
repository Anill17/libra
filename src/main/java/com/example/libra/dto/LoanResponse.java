package com.example.libra.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.libra.model.LoanStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LoanResponse {

    private Long id;
    private Long bookId;
    private Long memberId;

    private LocalDateTime loanDate = LocalDateTime.now();

    private LocalDateTime dueDate = LocalDateTime.now();

    private LoanStatus status;    // ACTIVE / RETURNED / OVERDUE
}
