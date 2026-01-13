package com.example.libra.event;

import com.example.libra.model.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanCreatedEvent {
    private Long loanId;
    private Long bookId;
    private Long memberId;
    private LocalDateTime loanDate;
    private LocalDateTime dueDate;
    private LoanStatus status;
    private String eventType = "LOAN_CREATED";
    private LocalDateTime eventTimestamp = LocalDateTime.now();

}
