package com.backend.orderhere.config;
import com.stripe.Stripe;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;

@Configuration
public class StripeConfig {
    static {
        Stripe.apiKey = "sk_test_51O79acCO47pkDdZVXGekJHrDVXbHAcbzaZzoNZ3CbMYSkPx8TcjKuybHND5HZ5vu14iJYfIAZCeEoUSmxmOqSOCW00SM8E2HZ2";
    }
}
