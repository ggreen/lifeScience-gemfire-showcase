package showcase.life.science.gemfire.pharmacy.pricing.domains;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PricingResult(BigDecimal totalPrice,
                            BigDecimal memberPay,
                            BigDecimal planPay) {
}
