package tms;

import org.junit.Test;
import org.junit.Assert;

/**
 * Checks which version of the JDK is being used to build and run applications.
 * You should run this as a test in IntelliJ as soon as you create the project
 * for your assignment. It requires you to have added JUnit 4 as a library used
 * by your project.
 * @given
 */
public class JdkTest {
    /**
     * Checks the version of Java being used to run this test.
     * Test fails if it is not Java 13.
     * @given
     */
    @Test
    public void version() {
        // If you fail this test then you will probably have major issues
        // during the semester. Please see a tutor to fix this.
        String version = System.getProperty("java.version");
        Assert.assertEquals("13", version.split("\\.")[0]);
    }
}
