package org.geektimes.rest.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.geektimes.rest.util.URLUtils.resolveVariables;
import static org.junit.Assert.assertEquals;

/**
 * TODO
 *
 * @author C.HD
 * @since 2021/4/8.
 */
public class URLUtilsTest {

    @Test
    public void testResolveVariables() {

        Map<String, Object> templateValues = new HashMap<>();

        templateValues.put("a", 1);
        templateValues.put("b", 2);
        templateValues.put("c", 3);

        String value = resolveVariables("/{a}/{b}/{c}", templateValues, true);
        assertEquals("/1/2/3", value);

        value = resolveVariables("/{a}/{b}/{c}", emptyMap(), true);
        assertEquals("/{a}/{b}/{c}", value);

        value = resolveVariables("/{a}/{b}/{d}/{c}", templateValues, true);
        assertEquals("/1/2/{d}/3", value);

    }
}