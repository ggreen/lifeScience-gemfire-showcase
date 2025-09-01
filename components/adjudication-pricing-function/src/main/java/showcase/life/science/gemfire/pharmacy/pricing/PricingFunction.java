package showcase.life.science.gemfire.pharmacy.pricing;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.RegionFunctionContext;
import showcase.life.science.gemfire.pharmacy.pricing.domains.DrugPricingInfo;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PlanPricingRule;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PricingRequest;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PricingResult;

import java.math.BigDecimal;

public class PricingFunction implements Function<PricingRequest> {

    @Override
    public void execute(FunctionContext<PricingRequest> context) {
        var cache = context.getCache();
        RegionFunctionContext<PricingRequest> rfc = (RegionFunctionContext)context;
        Region<String, DrugPricingInfo> drugRegion = rfc.getDataSet();
        Region<String, PlanPricingRule> planRegion = cache.getRegion("PlanRegion");

        var pricingRequest = context.getArguments();
        var ndc = pricingRequest.ndc();
        var planId = pricingRequest.planId();

        var drugInfo = drugRegion.get(ndc);
        var planRule = planRegion.get(planId);

        if (drugInfo == null || planRule == null) {
            context.getResultSender().lastResult(
                    new PricingResult(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            return;
        }

        // Calculate total price using AWP - discount + dispensing fee
        var awpDiscount = BigDecimal.ONE.subtract(planRule.awpDiscount());
        var discountedAwp = drugInfo.awp().multiply(awpDiscount);
        var totalPrice = discountedAwp.add(planRule.dispensingFee());

        // Apply member copay
        BigDecimal memberPay = planRule.copay();
        BigDecimal planPay = totalPrice.subtract(memberPay);

        PricingResult result = new PricingResult(totalPrice, memberPay, planPay);
        context.getResultSender().lastResult(result);
    }

    @Override
    public String getId() {
        return "SimplePricingFunction";
    }

    @Override
    public boolean hasResult() {
        return true;
    }
}
