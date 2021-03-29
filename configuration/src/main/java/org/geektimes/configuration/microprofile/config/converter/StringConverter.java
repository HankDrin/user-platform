package org.geektimes.configuration.microprofile.config.converter;

/**
 * TODO
 *
 * @author C.HD
 * @since 2021/3/29.
 */
public class StringConverter extends AbstractConverter<String> {
    @Override
    protected String doConvert(String value) {
        return value;
    }
}
