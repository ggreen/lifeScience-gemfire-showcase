package showcase.life.science.gemfire.pharmacy.pricing;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import showcase.life.science.gemfire.pharmacy.pricing.domains.DrugPricingInfo;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PlanPricingRule;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PricingRequest;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PricingResult;

import java.math.BigDecimal;

public class PricingFunction implements Function<PricingRequest> {

    private Logger  log = LogManager.getLogger(PricingFunction.class);

    private final Region<String, PlanPricingRule> planRegion;
    private final Region<String, DrugPricingInfo> drugRegion;

    public PricingFunction(Cache cache, Region<String,
            PlanPricingRule> planRegion,
                           Region<String, DrugPricingInfo> drugRegion) {
        this.planRegion = planRegion;
        this.drugRegion = drugRegion;
    }




    @Override
    public void execute(FunctionContext<PricingRequest> context) {

        var startTime = System.currentTimeMillis();
        var pricingRequest = context.getArguments();
        var ndc = pricingRequest.getNdc();
        var drugInfo = drugRegion.get(ndc);
        var planRule = planRegion.get(pricingRequest.toPlanRuleKey());

        if (drugInfo == null || planRule == null) {
            context.getResultSender().lastResult(
                    new PricingResult(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            var endTime = System.currentTimeMillis();
            log.info("Not Found ndc: {} planId: {}, time:{} ms",
                    pricingRequest.getNdc(),
                    pricingRequest.getPlanId(),endTime-startTime);
            return;
        }

        // Calculate total price using AWP - discount + dispensing fee
        var planPricing = planRule.getPlanPricing();
        var awpDiscount = BigDecimal.ONE.subtract(planPricing.getAwpDiscount());
        var discountedAwp = drugInfo.getAwp().multiply(awpDiscount);
        var totalPrice = discountedAwp.add(planPricing.getDispensingFee());

        // Apply member copay
        BigDecimal memberPay = planPricing.getCopay();
        BigDecimal planPay = totalPrice.subtract(memberPay);

        PricingResult result = new PricingResult(totalPrice, memberPay, planPay);
        context.getResultSender().lastResult(result);
        var endTime = System.currentTimeMillis();

        log.info("ndc: {} planId: {}, Total time:{} ms",
                pricingRequest.getNdc(),
                pricingRequest.getPlanId(),endTime-startTime);

    }

    @Override
    public String getId() {
        return "getPricing";
    }

    public PricingFunction(Cache cache) {
        this(cache,cache.getRegion("PlanPricingRule"),cache.getRegion("DrugPricingInfo"));

    }
    public PricingFunction()
    {
        this(CacheFactory.getAnyInstance());
    }
}
