package ru.armagidon.papyrus.implementation.placeholder.method.parameters;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class DefaultParamSerializers
{

    public static ParameterTypeParser<Integer> integerParser() {
        return numberParser(Integer::parseInt);
    }

    public static ParameterTypeParser<Long> longParser() {
        return numberParser(Long::parseLong);
    }

    public static ParameterTypeParser<Short> shortParser() {
        return numberParser(Short::parseShort);
    }

    public static ParameterTypeParser<Byte> byteParser() {
        return numberParser(Byte::parseByte);
    }

    public static ParameterTypeParser<Float> floatParser() {
        return numberParser(Float::parseFloat);
    }

    public static ParameterTypeParser<Double> doubleParser() {
        return numberParser(Double::parseDouble);
    }

    public static ParameterTypeParser<Character> charParser() {
        return (viewer, parameters) -> {
            String input = parameters.poll();
            if (input == null)
                return ParameterParseResult.fail(new ParameterParseException("Parameter was not given", null));
            else if (input.length() == 0) {
                return ParameterParseResult.fail(new ParameterParseException("Empty string", input));
            } else if (input.length() > 1) {
                return ParameterParseResult.fail(new ParameterParseException("Input is not a character: {input}", input));
            } else {
                return ParameterParseResult.success(input.toCharArray()[0]);
            }
        };
    }

    public static ParameterTypeParser<Boolean> booleanParser() {
        return (viewer, parameters) -> {
            String input = parameters.poll();
            if (input == null)
                return ParameterParseResult.fail(new ParameterParseException("Parameter was not given", null));
            return ParameterParseResult.success(Boolean.parseBoolean(input));
        };
    }

    public static ParameterTypeParser<String> stringParser() {
        return (viewer, parameters) -> Optional.ofNullable(parameters.poll())
                .map(ParameterParseResult::success)
                .orElse(ParameterParseResult.fail(new ParameterParseException("Parameter was not given", null)));
    }

    public static ParameterTypeParser<UUID> uuidParser() {
        return (viewer, parameters) -> {
            String input = parameters.poll();
            if (input == null)
                return ParameterParseResult.fail(new ParameterParseException("Parameter was not given", null));
            try {
                return ParameterParseResult.success(UUID.fromString(input));
            } catch (Exception e) {
                return ParameterParseResult.fail(new ParameterParseException(e.getMessage(), input));
            }
        };
    }

    public static ParameterParserCollection defaults() {
        return ParameterParserCollection.builder()
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

    private static <N extends Number> ParameterTypeParser<N> numberParser(Function<String, N> mapper) {
        return (viewer, parameters) -> {
            String input = parameters.poll();
            if (input == null)
                return ParameterParseResult.fail(new ParameterParseException("Parameter was not given", null));
            try {
                return ParameterParseResult.success(mapper.apply(input));
            } catch (NumberFormatException nfe) {
                return ParameterParseResult.fail(new ParameterParseException(nfe.getMessage(), input));
            }
        };
    }
}
