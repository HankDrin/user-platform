package org.geektimes.configuration.microprofile.config.converter;

/**
 * TODO
 *
 * @author C.HD
 * @since 2021/3/29.
 */
public class IntegerConverter extends AbstractConverter<Integer> {
    @Override
    protected Integer doConvert(String value) {
        return Integer.valueOf(value);
    }
}
