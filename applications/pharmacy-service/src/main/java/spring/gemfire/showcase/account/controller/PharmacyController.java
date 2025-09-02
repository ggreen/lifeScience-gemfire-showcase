package spring.gemfire.showcase.account.controller;

import org.springframework.web.bind.annotation.*;
import showcase.life.science.gemfire.pharmacy.pricing.domains.*;
import spring.gemfire.showcase.account.function.GetPricing;
import spring.gemfire.showcase.account.repostories.DrugPricingInfoRepository;
import spring.gemfire.showcase.account.repostories.PlanPricingRuleRepository;

/**
 * REST Service for pharmacy drug data
 * @author gregory green
 */
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

    /**
     * Save the drug pricing
     * @param drugPricingInfo the drug pricing
     */
    @PostMapping("drug/pricing/info")
    public void saveDrugPricingInfo(@RequestBody DrugPricingInfo drugPricingInfo) {
        drugPricingInfoRepository.save(drugPricingInfo);
    }

    /**
     * Save the plan pricing details
     * @param planPricing the plan pricing details
     */
    @PostMapping("plan/pricing")
    public void savePlanPricing(@RequestBody PlanPricing planPricing) {

        var rule = new PlanPricingRule(planPricing.getNdc() + "|" + planPricing.getPlanId(), planPricing);
        planPricingRuleRepository.save(rule);
    }

    /**
     * Get Pricing details
     * @param ndc the drud Id
     * @param planId the planId
     * @return the pricing results
     */
    @GetMapping("pricing/{ndc}/{planId}")
    public PricingResult getPricing(@PathVariable String ndc, @PathVariable String planId) {
        return getPricingFunction.getPricing(new PricingRequest(ndc,planId));
    }
}
