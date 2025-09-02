package showcase.life.science.gemfire.pharmacy.pricing.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public  class AffordableEvent{

    private String id;
    private String nbc;
    private String planId;
    private BigDecimal price;
}

