package ru.armagidon.papyrus.placeholder.params;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ParamSerializerCollection<P>
{
    private final Map<Class<?>, ParamSerializer<P, ?>> serializerMap;

    private ParamSerializerCollection(@NotNull Map<Class<?>, ParamSerializer<P, ?>> serializerMap) {
        this.serializerMap = ImmutableMap.copyOf(serializerMap);
    }

    public @NotNull Map<Class<?>, ParamSerializer<P, ?>> getSerializerMap() {
        return serializerMap;
    }


    public static <P> @NotNull Builder<P> builder() {
        return new Builder<>();
    }


    public static final class Builder<P> {
        private final Map<Class<?>, ParamSerializer<P, ?>> serializerMap = new HashMap<>();


        public <T> @NotNull Builder<P> append(@NotNull Class<T> clazz, @NotNull ParamSerializer<P, T> serializer) {
            Validate.notNull(clazz);
            Validate.notNull(serializer);
            if (serializerMap.containsKey(clazz))
                throw new IllegalArgumentException("Type " + clazz.getTypeName() + " is already registered");
            serializerMap.put(clazz, serializer);
            return this;
        }

        public @NotNull Builder<P> appendAll(@NotNull ParamSerializerCollection<P> collection) {
            Validate.notNull(collection);
            Set<Class<?>> duplicates = Sets.intersection(serializerMap.keySet(), collection.serializerMap.keySet());
            if (!duplicates.isEmpty())
                throw new IllegalArgumentException("Attempt to register duplicate param serializer: " + duplicates);
            serializerMap.putAll(collection.serializerMap);
            return this;
        }

        public @NotNull ParamSerializerCollection<P> build() {
            return new ParamSerializerCollection<>(serializerMap);
        }

    }
}
