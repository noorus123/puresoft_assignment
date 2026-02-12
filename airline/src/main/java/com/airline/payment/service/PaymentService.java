package com.airline.payment.service;

import com.airline.payment.event.PaymentInitiatedEvent;
import com.airline.payment.event.PaymentSuccessEvent;
import com.airline.payment.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ApplicationEventPublisher publisher;

    @Async
    @EventListener
    public void processPayment(PaymentInitiatedEvent event) throws InterruptedException {

        Random random = new Random();
        boolean isSuccess = random.nextInt(100) < 70;

        Thread.sleep(2000); //simulates real-world payment gateway latency

        if (isSuccess) {
            publisher.publishEvent(new PaymentSuccessEvent(event.bookingId()));
        } else {
            publisher.publishEvent(new PaymentFailedEvent(event.bookingId()));
        }
    }
}
