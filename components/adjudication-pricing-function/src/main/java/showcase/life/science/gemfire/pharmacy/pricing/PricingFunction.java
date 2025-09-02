package showcase.life.science.gemfire.pharmacy.pricing;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import showcase.life.science.gemfire.pharmacy.pricing.domains.DrugPricingInfo;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PlanPricingRule;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PricingRequest;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PricingResult;

import java.math.BigDecimal;

public class PricingFunction implements Function<Object[]> {

    private Logger  log = LogManager.getLogger(PricingFunction.class);

    @Override
    public void execute(FunctionContext<Object[]> context) {

        var startTime = System.currentTimeMillis();
        var cache = context.getCache();
        RegionFunctionContext<PricingRequest> rfc = (RegionFunctionContext)context;
        Region<String, DrugPricingInfo> drugRegion = rfc.getDataSet();
        Region<String, PlanPricingRule> planRegion = cache.getRegion("PlanRegion");

        var pricingRequest = (PricingRequest)context.getArguments()[0];
        var ndc = pricingRequest.getNdc();
        var planId = pricingRequest.getPlanId();

        var drugInfo = drugRegion.get(ndc);
        var planRule = planRegion.get(planId);

        if (drugInfo == null || planRule == null) {
            context.getResultSender().lastResult(
                    new PricingResult(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
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

        log.info("Total time:{} ms",endTime-startTime);

    }

    @Override
    public String getId() {
        return "PricingFunction";
    }

    @Override
    public boolean hasResult() {
        return true;
    }
}
