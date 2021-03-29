package org.geektimes.configuration.microprofile.config.converter;

/**
 * TODO
 *
 * @author C.HD
 * @since 2021/3/29.
 */
public class LongConverter extends AbstractConverter<Long> {
    @Override
    protected Long doConvert(String value) {
        return Long.valueOf(value);
    }
}
