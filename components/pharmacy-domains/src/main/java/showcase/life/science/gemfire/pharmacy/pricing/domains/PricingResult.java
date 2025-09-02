package showcase.life.science.gemfire.pharmacy.pricing.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PricingResult{
    private BigDecimal totalPrice;
    private BigDecimal memberPay;
    private BigDecimal planPay;
}
