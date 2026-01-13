package com.example.libra.service;

import com.example.libra.event.LoanCreatedEvent;
import com.example.libra.event.LoanStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
 
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    // Topic names
    private static final String LOAN_CREATED_TOPIC = "loan-created";
    private static final String LOAN_STATUS_CHANGED_TOPIC = "loan-status-changed";

    public void sendLoanCreatedEvent(LoanCreatedEvent event) {
        try {
            CompletableFuture<SendResult<String, Object>> future = 
                kafkaTemplate.send(LOAN_CREATED_TOPIC, event);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("✅ Sent loan created event=[{}] with offset=[{}]", 
                        event, result.getRecordMetadata().offset());
                } else {
                    log.error("❌ Unable to send loan created event=[{}] due to : {}", 
                        event, ex.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("Error sending loan created event", e);
        }
    }

    public void sendLoanStatusChangedEvent(LoanStatusChangedEvent event) {
        try {
            CompletableFuture<SendResult<String, Object>> future = 
                kafkaTemplate.send(LOAN_STATUS_CHANGED_TOPIC, event);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("✅ Sent loan status changed event=[{}] with offset=[{}]", 
                        event, result.getRecordMetadata().offset());
                } else {
                    log.error("❌ Unable to send loan status changed event=[{}] due to : {}", 
                        event, ex.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("Error sending loan status changed event", e);
        }
    }
}