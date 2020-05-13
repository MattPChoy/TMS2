package tms.network;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    ConstructorTest.class,
    YellowTimeTest.class,
    CreateIntersectionTest.class,
    ConnectIntersectionsTest.class,
    AddLightsTest.class,
    AddSpeedSignTest.class,
    SetSpeedLimitTest.class,
    ChangeLightDurationTest.class,
    GetConnectionTest.class,
    AddSensorTest.class,
    FindIntersection.class,
    MakeTwoWay.class,
    EqualsTest.class,
    ToStringTest.class
})

public class TestNetwork {

}
