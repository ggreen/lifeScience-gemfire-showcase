package spring.gemfire.showcase.account.repostories;

import org.springframework.data.gemfire.repository.GemfireRepository;
import showcase.life.science.gemfire.pharmacy.pricing.domains.AffordableEvent;

public interface AffordableEventRepository extends GemfireRepository<AffordableEvent,String> {
}
