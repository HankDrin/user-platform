package org.geektimes.configuration.microprofile.config.converter;

/**
 * TODO
 *
 * @author C.HD
 * @since 2021/3/24.
 */
public class BooleanConverter extends AbstractConverter<Boolean> {
    @Override
    protected Boolean doConvert(String value) {
        return Boolean.parseBoolean(value);
    }
}
