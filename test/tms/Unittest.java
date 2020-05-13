package tms;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tms.congestion.AveragingCongestionCalculatorTest;
import tms.intersection.IntersectionLightsTest;
import tms.intersection.IntersectionTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AveragingCongestionCalculatorTest.class,
        IntersectionTest.class,
        IntersectionLightsTest.class,
})

public class Unittest{

}