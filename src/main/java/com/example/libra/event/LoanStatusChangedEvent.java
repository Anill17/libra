package com.example.libra.event;

import com.example.libra.model.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanStatusChangedEvent {
    private Integer loanId;
    private LoanStatus oldStatus;
    private LoanStatus newStatus;
    private Long bookId;
    private Long memberId;
    private String eventType = "LOAN_STATUS_CHANGED";
    private LocalDateTime eventTimestamp = LocalDateTime.now();
}