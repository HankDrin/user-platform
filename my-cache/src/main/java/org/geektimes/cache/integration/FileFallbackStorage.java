package org.geektimes.cache.integration;

import org.geektimes.cache.serialization.SerializationStrategy;
import org.geektimes.cache.serialization.SerializationStrategyProvider;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.io.*;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * File-based {@link FallbackStorage}
 *
 * @author C.HD
 * @since 1.0
 */
public class FileFallbackStorage extends AbstractFallbackStorage<Object, Object> {

    private static final File CACHE_FALLBACK_DIRECTORY = new File(".cache/fallback/");

    private final Logger logger = Logger.getLogger(getClass().getName());

    static {
        if (!CACHE_FALLBACK_DIRECTORY.exists() && !CACHE_FALLBACK_DIRECTORY.mkdirs()) {
            throw new RuntimeException(format("The fallback directory[path:%s] can't be created!"));
        }
    }

    public FileFallbackStorage() {
        super(Integer.MAX_VALUE);
    }

    File toStorageFile(Object key) {
        return new File(CACHE_FALLBACK_DIRECTORY, key.toString() + ".dat");
    }

    @Override
    public Object load(Object key) throws CacheLoaderException {
        File storageFile = toStorageFile(key);
        if (!storageFile.exists() || !storageFile.canRead()) {
            logger.warning(format("The storage file[path:%s] does not exist or can't be read, " +
                    "thus the value can't be loaded.", storageFile.getAbsolutePath()));
            return null;
        }
        Object value = null;
        try (FileInputStream inputStream = new FileInputStream(storageFile);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            value = objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.severe(format("The deserialization of value[%s] is failed, caused by :%s",
                    value, e.getMessage()));
        }
        return value;
    }

    @Override
    public void write(Cache.Entry<?, ?> entry) throws CacheWriterException {
        Object key = entry.getKey();
        Object value = entry.getValue();
        File storageFile = toStorageFile(key);
        try {
            if ((storageFile.exists() || storageFile.createNewFile()) && !storageFile.canWrite()) {
                logger.warning(format("The storage file[path:%s] can't be written, " + "thus the entry will not be stored.",
                        storageFile.getAbsolutePath()));
                return;
            }
        } catch (IOException e) {
            logger.severe(format("The serialization of value[%s] is failed, caused by :%s", value, e.getMessage()));
        }
        try (FileOutputStream outputStream = new FileOutputStream(storageFile);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        ) {
            objectOutputStream.writeObject(value);
        } catch (IOException e) {
            logger.severe(format("The serialization of value[%s] is failed, caused by :%s", value, e.getMessage()));
        }
    }

    @Override
    public void delete(Object key) throws CacheWriterException {
        File storageFile = toStorageFile(key);
        storageFile.delete();
    }
}
