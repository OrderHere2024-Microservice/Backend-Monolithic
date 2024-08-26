package com.backend.orderhere.service;

import com.backend.orderhere.dto.payment.PaymentCreateDto;
import com.backend.orderhere.dto.payment.PaymentPostDto;
import com.backend.orderhere.dto.payment.PaymentResultDto;
import com.backend.orderhere.mapper.PaymentMapper;
import com.backend.orderhere.model.Order;
import com.backend.orderhere.model.Payment;
import com.backend.orderhere.model.enums.OrderStatus;
import com.backend.orderhere.model.enums.PaymentStatus;
import com.backend.orderhere.repository.OrderRepository;
import com.backend.orderhere.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;


    @Transactional
    public PaymentCreateDto createPayment(PaymentPostDto paymentPostDto) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(paymentPostDto.getAmount().multiply(new BigDecimal("100")).longValue())
                .setCurrency(paymentPostDto.getCurrency())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        System.out.println("trying to create paymentIntent");
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        System.out.println(paymentIntent.getClientSecret());
        Payment payment = new Payment();
        payment.setPaymentMethod(paymentIntent.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.unpaid);
        payment.setStripePaymentId(paymentIntent.getId());
        payment.setOrder(orderRepository.findByOrderId(paymentPostDto.getOrderId()));
        payment.setCurrency(paymentPostDto.getCurrency());
        payment.setAmount(paymentPostDto.getAmount());
        return new PaymentCreateDto(
                paymentRepository.save(payment).getPaymentId(),
                paymentIntent.getClientSecret()
        );
    }

    @Transactional
    public void getPaymentResult(PaymentResultDto paymentResultDto) throws StripeException {
        Payment payment = paymentRepository.getByPaymentId(paymentResultDto.getPaymentId());
        if (paymentResultDto.getResult().equals("success")) {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(payment.getStripePaymentId());
            payment.setPaymentMethod(paymentIntent.getPaymentMethod());
            payment.setPaymentStatus(PaymentStatus.paid);
            paymentRepository.save(payment);
            Order order = payment.getOrder();
            order.setOrderStatus(OrderStatus.preparing);
            orderRepository.save(order);
        }
        else {
            payment.setPaymentStatus(PaymentStatus.failed);
            paymentRepository.save(payment);
        }
    }
}
