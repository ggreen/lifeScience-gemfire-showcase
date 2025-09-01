package showcase.life.science.gemfire.pharmacy.pricing.domains;

import lombok.Builder;

@Builder
public record PricingRequest( String ndc,String planId) {
}
