package spring.gemfire.showcase.account.function;

import org.springframework.data.gemfire.function.annotation.Filter;
import org.springframework.data.gemfire.function.annotation.FunctionId;
import org.springframework.data.gemfire.function.annotation.OnRegion;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PricingRequest;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PricingResult;

import java.util.Set;

@OnRegion(region = "DrugPricingInfo")
public interface GetPricing {

    @FunctionId("PricingFunction")
    PricingResult get(PricingRequest request,
                      @Filter Set<String> ndc);
}
