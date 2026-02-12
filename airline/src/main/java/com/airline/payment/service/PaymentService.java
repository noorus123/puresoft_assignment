package com.airline.payment.service;

import com.airline.payment.event.PaymentInitiatedEvent;
import com.airline.payment.event.PaymentSuccessEvent;
import com.airline.payment.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ApplicationEventPublisher publisher;

    @Async
    @EventListener
    public void processPayment(PaymentInitiatedEvent event) throws InterruptedException {

        // simulate payment delay
        Thread.sleep(2000);

        boolean paymentSuccess = true; // you can randomize later

        if (paymentSuccess) {
            publisher.publishEvent(new PaymentSuccessEvent(event.bookingId()));
        } else {
            publisher.publishEvent(new PaymentFailedEvent(event.bookingId()));
        }
    }
}
