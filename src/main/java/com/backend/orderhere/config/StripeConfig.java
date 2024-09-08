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
        SsmClient ssmClient = SsmClient.builder().region(Region.AP_SOUTHEAST_2).build();

        GetParameterRequest parameterRequest = GetParameterRequest.builder().name("/config/orderhere-monolithic/development/stripe/api-key").withDecryption(true).build();

        GetParameterResponse parameterResponse = ssmClient.getParameter(parameterRequest);

        String stripeApiKey = parameterResponse.parameter().value();

        System.out.println("Stripe API Key: " + stripeApiKey);

        if (stripeApiKey == null || stripeApiKey.isEmpty()) {
            Stripe.apiKey = "placeholder";
            // throw new IllegalArgumentException("Stripe API Key not found in Parameter Store");
        }

        Stripe.apiKey = stripeApiKey;
    }
}
