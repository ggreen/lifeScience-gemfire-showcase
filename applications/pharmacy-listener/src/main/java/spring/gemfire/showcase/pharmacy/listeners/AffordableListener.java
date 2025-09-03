package spring.gemfire.showcase.pharmacy.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.geode.cache.query.CqEvent;
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery;
import org.springframework.stereotype.Component;
import showcase.life.science.gemfire.pharmacy.pricing.domains.AffordableEvent;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PlanPricingRule;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PricingRequest;
import spring.gemfire.showcase.pharmacy.function.GetPricing;
import spring.gemfire.showcase.pharmacy.repostories.AffordableEventRepository;

@Component
@Slf4j
@RequiredArgsConstructor
public class AffordableListener {

    private final AffordableEventRepository repository;
    private final GetPricing getPricing;

    @ContinuousQuery(
            name = "affordableEvents",
            query = "select * from /PlanPricingRule where planPricing.awpDiscount > 0.49")
    public void addAffordableEvents(CqEvent cqEvent) {

        log.info("Event: {}",cqEvent);

        if(!cqEvent.getBaseOperation().isCreate() && !cqEvent.getBaseOperation().isUpdate() )
            return;

        var eventValue= cqEvent.getNewValue();
        log.info("New/Update event: {}",eventValue);

        if(eventValue instanceof PlanPricingRule planPricingRule)
        {
            //use GemFire function to get price
            var price = getPricing.getPricing(
                    PricingRequest.builder()
                    .ndc(planPricingRule.getPlanPricing().getNdc())
                     .planId(planPricingRule.getPlanPricing().getPlanId()).build()
            );

            log.info("********* Saving event with pricing: {} ",price);
            // save affordable event
            var affordableEvent = AffordableEvent
                    .builder()
                    .id(planPricingRule.getId())
                    .nbc(planPricingRule.getPlanPricing().getNdc())
                    .planId(planPricingRule.getPlanPricing().getPlanId())
                    .price(price.getTotalPrice())
                    .build();

            repository.save(affordableEvent);

            log.info("******  Saved event: {} ",affordableEvent);

        }
    }
}
