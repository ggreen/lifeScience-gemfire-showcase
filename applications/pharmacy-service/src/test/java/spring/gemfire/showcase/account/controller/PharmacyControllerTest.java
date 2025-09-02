package spring.gemfire.showcase.account.controller;

import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import showcase.life.science.gemfire.pharmacy.pricing.domains.*;
import spring.gemfire.showcase.account.function.GetPricing;
import spring.gemfire.showcase.account.repostories.DrugPricingInfoRepository;
import spring.gemfire.showcase.account.repostories.PlanPricingRuleRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PharmacyControllerTest
{
    private PharmacyController subject;
    private DrugPricingInfo drugPricingInfo = JavaBeanGeneratorCreator.of(DrugPricingInfo.class).create();
    private PlanPricing planPricing = JavaBeanGeneratorCreator.of(PlanPricing.class).create();

    @Mock
    private DrugPricingInfoRepository drugPricingInfoRepository;

    @Mock
    private PlanPricingRuleRepository planPricingRuleRepository;

    @Mock
    private GetPricing getPricingFunction;

    private PricingRequest request = JavaBeanGeneratorCreator.of(PricingRequest.class).create();

    @BeforeEach
    void setUp() {
        subject = new PharmacyController(drugPricingInfoRepository,
                planPricingRuleRepository,
                getPricingFunction);
    }

    @Test
    void saveDrugPricingInfo() {
        subject.saveDrugPricingInfo(drugPricingInfo);
        verify(drugPricingInfoRepository).save(any(DrugPricingInfo.class));
    }

    @Test
    void planPricing() {
        subject.savePlanPricing(planPricing);

        verify(planPricingRuleRepository).save(any(PlanPricingRule.class));

    }

    @Test
    void getPricing() {
        PricingResult expected = JavaBeanGeneratorCreator.of(PricingResult.class).create();

        when(getPricingFunction.getPricing(any())).thenReturn(expected);

        var actual = subject.getPricing(request.getNdc(),request.getPlanId());

        assertThat(actual).isEqualTo(expected);
    }
}