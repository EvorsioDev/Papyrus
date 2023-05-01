package ru.armagidon.papyrus.placeholder.params;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class DefaultParamSerializers
{

    public static <P> ParamSerializer<P, Integer> integerParser() {
        return (viewer, params) ->
                Integer.parseInt(Objects.requireNonNull(params.poll()));
    }

    public static <P> ParamSerializer<P, Long> longParser() {
        return (viewer, params) ->
                Long.parseLong(Objects.requireNonNull(params.poll()));
    }

    public static <P> ParamSerializer<P, Short> shortParser() {
        return (viewer, params) ->
                Short.parseShort(Objects.requireNonNull(params.poll()));
    }

    public static <P> ParamSerializer<P, Byte> byteParser() {
        return (viewer, params) ->
                Byte.parseByte(Objects.requireNonNull(params.poll()));
    }

    public static <P> ParamSerializer<P, Float> floatParser() {
        return (viewer, params) ->
                Float.parseFloat(Objects.requireNonNull(params.poll()));
    }

    public static <P> ParamSerializer<P, Double> doubleParser() {
        return (viewer, params) ->
                Double.parseDouble(Objects.requireNonNull(params.poll()));
    }

    public static <P> ParamSerializer<P, Character> charParser() {
        return (viewer, params) ->
                Optional.ofNullable(params.poll()).map(String::toCharArray)
                        .map(a -> a[0]).orElseThrow();
    }

    public static <P> ParamSerializer<P, Boolean> booleanParser() {
        return (viewer, params) ->
                Boolean.parseBoolean(params.poll());
    }

    public static <P> ParamSerializer<P, String> stringParser() {
        return (viewer, params) ->
                params.poll();
    }

    public static <P> ParamSerializer<P, UUID> uuidParser() {
        return (viewer, params) ->
                java.util.UUID.fromString(Objects.requireNonNull(params.poll()));
    }

    public static <P> ParamSerializerCollection<P> defaults() {
        return ParamSerializerCollection.<P>builder()
                .append(Integer.class, integerParser())
                .append(Long.class, longParser())
                .append(Short.class, shortParser())
                .append(Byte.class, byteParser())
                .append(Float.class, floatParser())
                .append(Double.class, doubleParser())
                .append(Character.class, charParser())
                .append(Boolean.class, booleanParser())
                .append(String.class, stringParser())
                .append(UUID.class, uuidParser())
                .build();
    }
}
