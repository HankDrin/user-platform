package org.geektimes.cache.serialization;

import java.io.*;
import java.util.Iterator;
import java.util.List;

import static java.util.ServiceLoader.load;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.geektimes.cache.serialization.SerializationStrategy.DEFAULT_COMPARATOR;

/**
 * serialization strategy provider {@link SerializationStrategy}
 *
 * @author C.HD
 * @since 1.0
 */
public class SerializationStrategyProvider implements Iterable<SerializationStrategy> {

    private final List<SerializationStrategy> serializationStrategies;

    private final SerializationStrategy DEFAULT_STRATEGY = new SerializationStrategy() {
        @Override
        public <T> byte[] serialize(T object) {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
                objectOutputStream.writeObject(object);
                objectOutputStream.flush();
                return outputStream.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public <T> T deserialize(byte[] data) {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
                 ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
                return (T) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public <T> T deserialize(InputStream inputStream) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
                return (T) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public int getPriority() {
            return 0;
        }
    };

    public SerializationStrategyProvider(ClassLoader classLoader) {
        serializationStrategies = stream(load(SerializationStrategy.class, classLoader).spliterator(), false)
                                               .sorted(DEFAULT_COMPARATOR)
                                               .collect(toList());
    }

    public SerializationStrategy get() {
        if (serializationStrategies == null || serializationStrategies.isEmpty()) {
            return DEFAULT_STRATEGY;
        }
        return serializationStrategies.get(0);
    }

    @Override
    public Iterator<SerializationStrategy> iterator() {
        return serializationStrategies.iterator();
    }
}
