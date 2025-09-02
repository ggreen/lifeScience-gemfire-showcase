package showcase.life.science.gemfire.pharmacy.pricing.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PricingRequest{
    private String ndc;
    private String planId;

    public String toPlanRuleKey() {
        return ndc+"|"+planId;
    }
}
