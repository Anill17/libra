package com.example.libra.service;

import com.example.libra.event.LoanCreatedEvent;
import com.example.libra.event.LoanStatusChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(
        topics = "loan-created", 
        groupId = "libra-library-group-v2",
        properties = {
            "spring.json.value.default.type=com.example.libra.event.LoanCreatedEvent"
        }
    )
    public void consumeLoanCreatedEvent(LoanCreatedEvent event) {
        log.info("📚 Received Loan Created Event: Loan ID={}, Book ID={}, Member ID={}, Status={}, Date={}", 
            event.getLoanId(), 
            event.getBookId(), 
            event.getMemberId(), 
            event.getStatus(),
            event.getLoanDate());
        
        // Here you can add business logic like:
        // - Send notification to member
        // - Update inventory
        // - Log to audit system
        // - Trigger other services
        log.info("💡 Processing loan created event for loan ID: {}", event.getLoanId());
    }

    @KafkaListener(
        topics = "loan-status-changed", 
        groupId = "libra-library-group-v2",
        properties = {
            "spring.json.value.default.type=com.example.libra.event.LoanStatusChangedEvent"
        }
    )
    public void consumeLoanStatusChangedEvent(LoanStatusChangedEvent event) {
        log.info("🔄 Received Loan Status Changed Event: Loan ID={}, Old Status={}, New Status={}", 
            event.getLoanId(), 
            event.getOldStatus(), 
            event.getNewStatus());
        
        // Here you can add business logic like:
        // - Send email notification
        // - Update book availability
        // - Generate reports
        log.info("💡 Processing status change event for loan ID: {}", event.getLoanId());
    }
}