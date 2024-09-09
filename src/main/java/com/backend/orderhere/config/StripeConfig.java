package com.backend.orderhere.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

@Configuration
public class StripeConfig {

    @PostConstruct
    public void init() {

        String stripeApiKey = System.getenv("STRIPE_API_KEY");

        if (stripeApiKey == null || stripeApiKey.isEmpty()) {
            SsmClient ssmClient = SsmClient.builder()
                    .region(Region.AP_SOUTHEAST_2)
                    .build();

            GetParameterRequest parameterRequest = GetParameterRequest.builder()
                    .name("/config/orderhere-monolithic/development/stripe/api-key")
                    .withDecryption(true)
                    .build();

            GetParameterResponse parameterResponse = ssmClient.getParameter(parameterRequest);

            stripeApiKey = parameterResponse.parameter().value();
        }

        if (stripeApiKey == null || stripeApiKey.isEmpty()) {
            throw new IllegalArgumentException("Stripe API Key not found in Parameter Store or environment variable");
        }

        Stripe.apiKey = stripeApiKey;
    }
}
