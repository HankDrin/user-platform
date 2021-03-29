package org.geektimes.configuration.microprofile.config;

import org.eclipse.microprofile.config.Config;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * TODO
 *
 * @author C.HD
 * @since 2021/3/25.
 */
public class DefaultConfigTest {

    private Config config;

    @BeforeClass
    public static void prepare() {

    }

    @Before
    public void init() {
        config = new DefaultConfigBuilder(this.getClass()
                                              .getClassLoader()).addDefaultSources()
                                                                .addDiscoveredSources()
                                                                .addDiscoveredConverters()
                                                                .build();
    }

    @Test
    public void testGetValue() {
        Assert.assertEquals("user-platform-test", config.getConfigValue("application.name").getValue());
    }
}