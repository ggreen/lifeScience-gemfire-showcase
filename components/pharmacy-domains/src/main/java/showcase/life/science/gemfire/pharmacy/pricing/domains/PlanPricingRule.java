package showcase.life.science.gemfire.pharmacy.pricing.domains;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PlanPricingRule(BigDecimal awpDiscount,      // e.g. 0.15 for 15% off AWP
                              BigDecimal dispensingFee,    // e.g. $1.50
                              BigDecimal copay    // e.g.
) {
}
