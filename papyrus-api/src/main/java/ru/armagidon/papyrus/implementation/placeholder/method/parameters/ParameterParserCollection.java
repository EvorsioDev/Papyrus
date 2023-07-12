package ru.armagidon.papyrus.implementation.placeholder.method.parameters;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ParameterParserCollection
{
    private final Map<Type, ParameterTypeParser<?>> parserMap;

    private ParameterParserCollection(@NotNull Map<Type, ParameterTypeParser<?>> parserMap) {
        this.parserMap = ImmutableMap.copyOf(parserMap);
    }

    public @Nullable <T> ParameterTypeParser<T> getParser(Type type) {
        return (ParameterTypeParser<T>) parserMap.get(type);
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private final Map<Type, ParameterTypeParser<?>> serializerMap = new HashMap<>();


        public <T> @NotNull Builder append(@NotNull Class<T> clazz, @NotNull ParameterTypeParser<T> serializer) {
            Validate.notNull(clazz);
            Validate.notNull(serializer);
            if (serializerMap.containsKey(clazz))
                throw new IllegalArgumentException("Type " + clazz.getTypeName() + " is already registered");
            serializerMap.put(clazz, serializer);
            return this;
        }

        public <T> @NotNull Builder append(@NotNull TypeToken<T> clazz, @NotNull ParameterTypeParser<T> serializer) {
            Validate.notNull(clazz);
            Validate.notNull(serializer);
            if (serializerMap.containsKey(clazz.getType()))
                throw new IllegalArgumentException("Type " + clazz.getType().getTypeName() + " is already registered");
            serializerMap.put(clazz.getType(), serializer);
            return this;
        }

        public @NotNull Builder appendAll(@NotNull ParameterParserCollection collection) {
            Validate.notNull(collection);
            Set<Type> duplicates = Sets.intersection(serializerMap.keySet(), collection.parserMap.keySet());
            if (!duplicates.isEmpty())
                throw new IllegalArgumentException("Attempt to register duplicate parameter parser: " + duplicates);
            serializerMap.putAll(collection.parserMap);
            return this;
        }

        public @NotNull ParameterParserCollection build() {
            return new ParameterParserCollection(serializerMap);
        }

    }
}
