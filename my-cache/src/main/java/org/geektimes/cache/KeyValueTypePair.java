package org.geektimes.cache;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Objects;

/**
 * The type pair of key and value.
 *
 * @author C.HD
 * @since 1.0
 */
class KeyValueTypePair {

    private final Class<?> keyType;

    private final Class<?> valueType;

    public KeyValueTypePair(Class<?> keyType, Class<?> valueType) {
        this.keyType = keyType;
        this.valueType = valueType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        KeyValueTypePair that = (KeyValueTypePair) obj;
        return Objects.equals(this.keyType, that.keyType) && Objects.equals(this.valueType, that.valueType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyType, valueType);
    }

    public Class<?> getKeyType() {
        return keyType;
    }

    public Class<?> getValueType() {
        return valueType;
    }

    public static KeyValueTypePair resolve(Class<?> targetClass) {
        assertCache(targetClass);

        KeyValueTypePair pair = null;
        while (targetClass != null) {
            pair = resolveFromInterfaces(targetClass);
            if (pair != null) {
                break;
            }

            Type superType = targetClass.getGenericSuperclass();
            if (superType instanceof ParameterizedType) {
                pair = resolveFromType(superType);
            }

            if (pair != null) {
                break;
            }

            targetClass = targetClass.getSuperclass();
        }

        return pair;
    }

    private static KeyValueTypePair resolveFromInterfaces(Class<?> type) {
        KeyValueTypePair pair = null;
        for (Type superInterface : type.getGenericInterfaces()) {
            pair = resolveFromType(superInterface);
            if (pair != null) {
                break;
            }
        }
        return pair;
    }

    private static KeyValueTypePair resolveFromType(Type type) {
        KeyValueTypePair pair = null;
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            if (pType.getRawType() instanceof Class) {
                Type[] typeArguments = pType.getActualTypeArguments();
                if (typeArguments.length == 2) {
                    Class keyType = asClass(typeArguments[0]);
                    Class valueType = asClass(typeArguments[1]);
                    if (keyType != null && valueType != null) {
                        pair = new KeyValueTypePair(keyType, valueType);
                    }
                }
            }
        }
        return pair;
    }

    private static Class<?> asClass(Type typeArgument) {
        if (typeArgument instanceof Class) {
            return (Class<?>) typeArgument;
        } else if (typeArgument instanceof TypeVariable) {
            TypeVariable typeVariable = (TypeVariable) typeArgument;
            return asClass(typeVariable.getBounds()[0]);
        }
        return null;
    }

    private static void assertCache(Class<?> cacheClass) {
        if (cacheClass.isInterface()) {
            throw new IllegalArgumentException("The implementation class of Cache must not be an interface!");
        }
        if (Modifier.isAbstract(cacheClass.getModifiers())) {
            throw new IllegalArgumentException("The implementation class of Cache must not be abstract!");
        }
    }

}
