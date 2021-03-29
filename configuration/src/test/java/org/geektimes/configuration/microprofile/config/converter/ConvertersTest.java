package org.geektimes.configuration.microprofile.config.converter;

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
public class ConvertersTest {

    private Converters converters;

    @BeforeClass
    public static void prepare() {

    }

    @Before
    public void init() {
        converters = new Converters();
    }

    @Test
    public void testResolveConvertedType() {
        Assert.assertEquals(Boolean.class, converters.resolveConvertedType(new BooleanConverter()));
    }

}