package showcase.life.science.gemfire.pharmacy.pricing.domains;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class PricingRequestTest {

    @Test
    void toPlanRuleKey() {
        var ndc = "123";

        var planId = "ABC";

        Assertions.assertEquals(new PricingRequest(ndc,planId).toPlanRuleKey(),"123|ABC");
    }
}