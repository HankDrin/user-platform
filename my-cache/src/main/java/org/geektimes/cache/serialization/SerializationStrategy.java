package org.geektimes.cache.serialization;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;

/**
 * serialization policy
 *
 * @author C.HD
 * @since 1.0
 */
public interface SerializationStrategy {

    Comparator<SerializationStrategy> DEFAULT_COMPARATOR = new SerializationStrategy.PriorityComparator();

    Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    <T> byte[] serialize(T object);

    <T> T deserialize(byte[] data);

    <T> T deserialize(InputStream inputStream);

    default <T> T deserialize(String serializeStr) {
        return deserialize(serializeStr.getBytes(DEFAULT_CHARSET));
    }

    default <T> String serializeToString(T object) {
        return new String(serialize(object), DEFAULT_CHARSET);
    }

    /**
     * Get the priority of current {@link SerializationStrategy}.
     *
     * @return the less value , the more priority.
     */
    int getPriority();

    class PriorityComparator implements Comparator<SerializationStrategy> {

        @Override
        public int compare(SerializationStrategy o1, SerializationStrategy o2) {
            return Integer.compare(o2.getPriority(), o1.getPriority());
        }
    }

}
