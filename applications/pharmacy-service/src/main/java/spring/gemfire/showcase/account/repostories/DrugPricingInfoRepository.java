package spring.gemfire.showcase.account.repostories;

import org.springframework.data.domain.*;
import org.springframework.data.gemfire.repository.GemfireRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import showcase.life.science.gemfire.pharmacy.pricing.domains.DrugPricingInfo;
import spring.gemfire.showcase.account.domain.account.Account;

import java.util.List;


@Repository
public interface DrugPricingInfoRepository
extends GemfireRepository<DrugPricingInfo,String>
{
}
