package showcase.life.science.gemfire.pharmacy.pricing;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.geode.cache.execute.ResultCollector;
import org.apache.geode.cache.execute.ResultSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import showcase.life.science.gemfire.pharmacy.pricing.domains.DrugPricingInfo;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PlanPricingRule;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PricingRequest;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PricingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
//class PricingFunctionTest {
//
//    private PricingFunction subject;
//    @Mock
//    private RegionFunctionContext<PricingRequest> rfc;
//    @Mock
//    private Cache cache;
//    @Mock
//    private Region<String, DrugPricingInfo> drugRegion;
//    @Mock
//    private Region<String, PlanPricingRule> planRegion;
//    @Mock
//    private PricingRequest pricingRequest;
//    @Mock
//    private ResultSender<PricingResult> resultSender;
//
//    @BeforeEach
//    void setUp() {
//        subject = new PricingFunction();
//    }
//
//    @Test
//    void execute() {
//
//        when(rfc.getCache()).thenReturn(cache);
//        when(rfc.getDataSet()).thenReturn((Region)drugRegion);
//        when(cache.getRegion(anyString())).thenReturn((Region)planRegion);
//        when(rfc.getArguments()).thenReturn(pricingRequest);
//        when(rfc.getResultSender()).thenReturn((ResultSender) resultSender);
//
////        subject.execute(rfc);
//
////        verify(resultSender).lastResult(any());
//    }
//}