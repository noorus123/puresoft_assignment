package com.airline.payment.service;

import com.airline.payment.event.PaymentInitiatedEvent;
import com.airline.payment.event.PaymentSuccessEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void shouldPublishPaymentSuccessEvent() throws Exception {

        PaymentInitiatedEvent event = new PaymentInitiatedEvent(1L);

        paymentService.processPayment(event);

        verify(publisher)
                .publishEvent(new PaymentSuccessEvent(1L));
    }
}
