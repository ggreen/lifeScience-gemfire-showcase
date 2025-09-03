package spring.gemfire.showcase.pharmacy.repostories;

import org.springframework.data.gemfire.repository.GemfireRepository;
import org.springframework.stereotype.Repository;
import showcase.life.science.gemfire.pharmacy.pricing.domains.DrugPricingInfo;


@Repository
public interface DrugPricingInfoRepository
extends GemfireRepository<DrugPricingInfo,String>
{
}
