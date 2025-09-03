package spring.gemfire.showcase.pharmacy.function;

import org.springframework.stereotype.Service;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PricingRequest;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PricingResult;

@Service
@FunctionalInterface
public interface GetPricing {

    PricingResult getPricing(PricingRequest request);
}
