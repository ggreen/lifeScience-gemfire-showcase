package spring.gemfire.showcase.pharmacy.listeners;

import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.apache.geode.cache.Operation;
import org.apache.geode.cache.query.CqEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import showcase.life.science.gemfire.pharmacy.pricing.domains.AffordableEvent;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PlanPricingRule;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PricingResult;
import spring.gemfire.showcase.pharmacy.function.GetPricing;
import spring.gemfire.showcase.pharmacy.repostories.AffordableEventRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AffordableListenerTest {

    private AffordableListener subject;
    @Mock
    private CqEvent cqEvent;
    @Mock
    private AffordableEventRepository repository;

    @Mock
    private GetPricing getPricing;

    @Mock
    private Operation ops;
    private final static PlanPricingRule planRule = JavaBeanGeneratorCreator.of(PlanPricingRule.class).create();
    private final static PricingResult pricingResults = JavaBeanGeneratorCreator.of(PricingResult.class).create();

    @BeforeEach
    void setUp() {
        subject = new AffordableListener(repository, getPricing);
    }

    @Test
    void listen() {

        when(cqEvent.getBaseOperation()).thenReturn(ops);
        when(ops.isCreate()).thenReturn(true);
        when(cqEvent.getNewValue()).thenReturn(planRule);

        when(getPricing.getPricing(any())).thenReturn(pricingResults);
        subject.addAffordableEvents(cqEvent);

        verify(repository).save(any(AffordableEvent.class));
    }
}