package spring.gemfire.showcase.account.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import showcase.life.science.gemfire.pharmacy.pricing.domains.*;
import spring.gemfire.showcase.account.function.GetPricing;
import spring.gemfire.showcase.account.repostories.DrugPricingInfoRepository;
import spring.gemfire.showcase.account.repostories.PlanPricingRuleRepository;

import java.util.Collections;
import java.util.Set;

@RestController
@RequestMapping("pharmacy")
public class PharmacyController {

    private final DrugPricingInfoRepository drugPricingInfoRepository;
    private final PlanPricingRuleRepository planPricingRuleRepository;
    private final GetPricing getPricingFunction;

    public PharmacyController(DrugPricingInfoRepository drugPricingInfoRepository,
                              PlanPricingRuleRepository planPricingRuleRepository,
                              GetPricing getPricingFunction) {
        this.drugPricingInfoRepository = drugPricingInfoRepository;
        this.planPricingRuleRepository = planPricingRuleRepository;
        this.getPricingFunction = getPricingFunction;
    }

    @PostMapping("drug/pricing/info")
    public void saveDrugPricingInfo(@RequestBody DrugPricingInfo drugPricingInfo) {
        drugPricingInfoRepository.save(drugPricingInfo);
    }

    @PostMapping("plan/pricing")
    public void savePlanPricing(@RequestBody PlanPricing planPricing) {

        var rule = new PlanPricingRule(planPricing.getNdc() + "|" + planPricing.getPlanId(), planPricing);
        planPricingRuleRepository.save(rule);
    }

    @GetMapping("pricing")
    public PricingResult getPricing(@RequestBody PricingRequest request) {
        return getPricingFunction.get(request, Set.of(request.getNdc()));
    }
}
