package org.geektimes.configuration.microprofile.config.converter;

/**
 * TODO
 *
 * @author C.HD
 * @since 2021/3/29.
 */
public class ShortConverter extends AbstractConverter<Short> {
    @Override
    protected Short doConvert(String value) {
        return Short.valueOf(value);
    }
}
