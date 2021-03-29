package org.geektimes.configuration.microprofile.config.converter;

/**
 * TODO
 *
 * @author C.HD
 * @since 2021/3/29.
 */
public class DoubleConverter extends AbstractConverter<Double> {
    @Override
    protected Double doConvert(String value) {
        return Double.valueOf(value);
    }
}
