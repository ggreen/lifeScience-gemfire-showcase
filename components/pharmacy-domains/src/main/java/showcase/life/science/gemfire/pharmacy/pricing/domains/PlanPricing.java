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
public class PlanPricing {
        private String planId; //planId
        private String ndc;
        private BigDecimal awpDiscount;      // e.g. 0.15 for 15% off AWP
        private BigDecimal dispensingFee;    // e.g. $1.50
        private BigDecimal copay;

}
