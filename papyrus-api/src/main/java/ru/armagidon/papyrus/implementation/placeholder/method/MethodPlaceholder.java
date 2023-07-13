package ru.armagidon.papyrus.implementation.placeholder.method;

import com.google.common.primitives.Primitives;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import ru.armagidon.papyrus.placeholder.Placeholder;
import ru.armagidon.papyrus.placeholder.PlaceholderContext;
import ru.armagidon.papyrus.placeholder.PlaceholderId;
import ru.armagidon.papyrus.implementation.placeholder.method.parameters.ParameterParseResult;
import ru.armagidon.papyrus.implementation.placeholder.method.parameters.ParameterParserCollection;
import ru.armagidon.papyrus.implementation.placeholder.method.parameters.ParameterTypeParser;
import ru.armagidon.papyrus.text.ReplacementContext;
import ru.armagidon.papyrus.text.TextParserException;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class MethodPlaceholder implements Placeholder {

    private final PlaceholderId id;
    private final Method method;
    private final PlaceholderSweeperTarget handler;
    private final ParameterParserCollection parameterParsers;

    public MethodPlaceholder(PlaceholderId id, Method method, PlaceholderSweeperTarget handler, ParameterParserCollection parameterParsers) {
        Validate.notNull(id);
        Validate.notNull(method);
        Validate.notNull(handler);
        Validate.notNull(parameterParsers);
        validateInputMethod(method);
        this.id = id;
        this.method = method;
        this.handler = handler;
        this.parameterParsers = parameterParsers;
    }

    @Override
    public @NotNull PlaceholderId id() {
        return id;
    }

    @Override
    public @NotNull CompletableFuture<Optional<Component>> parsePlaceholderContents(@NotNull ReplacementContext context) {
        return CompletableFuture
                .runAsync(() -> method.setAccessible(true))
                .thenApply(v -> packParameters(context))
                .thenCompose(this::invokeSneakily)
                .thenApply(this::transfromToComponent)
                .thenApply(Optional::ofNullable)
                .exceptionally(error -> {
                    error.printStackTrace();
                    return Optional.empty();
                });
    }

    private void validateInputMethod(Method inputMethod) {
        Validate.notNull(inputMethod);
        if (!inputMethod.isAnnotationPresent(PlaceholderKey.class))
            throw new IllegalArgumentException("Method " + inputMethod.getName() + " is not annotated");
        Type returnType = inputMethod.getGenericReturnType();

        if (!CompletableFuture.class.isAssignableFrom(inputMethod.getReturnType()))
            throw new IllegalArgumentException("Return type must be of completable future");
        if (!(returnType instanceof ParameterizedType parameterizedType))
            throw new IllegalArgumentException("Raw types are not supported");
        if (parameterizedType.getActualTypeArguments().length != 1)
            throw new IllegalArgumentException("Return type must have at least one parameter");
        Class<?> outputType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        if (!Primitives.isWrapperType(outputType) && !outputType.equals(String.class) && !outputType.equals(Component.class))
            throw new IllegalArgumentException("Output type " + outputType.getTypeName() + " is unsupported");
    }

    private Component transfromToComponent(Object result) {
        if (result == null)
            return null;
        else if (result instanceof Integer number) {
            return Component.text(number);
        } else if (result instanceof Float number) {
            return Component.text(number);
        } else if (result instanceof Double number) {
            return Component.text(number);
        } else if (result instanceof Long number) {
            return Component.text(number);
        } else if (result instanceof Short number) {
            return Component.text(number);
        } else if (result instanceof Byte number) {
            return Component.text(number);
        } else if (result instanceof Character character) {
            return Component.text(character);
        } else if (result instanceof Boolean bool){
            return Component.text(bool);
        } else if (result instanceof String string){
            return Component.text(string);
        } else if (result instanceof Component component){
            return component;
        } else {
            throw new TextParserException("Unsupported return type for method " + method.getName() + ". Result: " + result);
        }
    }

    private CompletableFuture<?> invokeSneakily(Object[] args) {
        try {
            return (CompletableFuture<?>) method.invoke(handler, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] packParameters(ReplacementContext context) {
        Queue<String> input = new LinkedList<>(context.getParameters());
        Object[] args = new Object[method.getParameterCount()];
        for (int i = 0; i < method.getParameters().length; i++) {
            Parameter parameter = method.getParameters()[i];
            if (parameter.isAnnotationPresent(Context.class)) {
                if (!parameter.getType().equals(PlaceholderContext.class))
                    throw new TextParserException("Only PlaceholderContext can have @Context annotation");
                args[i] = context.getPlaceholderContext();
            } else {
                ParameterTypeParser<?> parser = parameterParsers.getParser(parameter.getParameterizedType());
                if (parser == null)
                    throw new TextParserException("Unsupported type: " + parameter.getParameterizedType().getTypeName());
                ParameterParseResult<?> result = parser.parse(context.getPlaceholderContext(), input);
                if (result.getResult() != null && result.getError() == null) {
                    args[i] = result.getResult();
                } else if (result.getResult() == null && result.getError() != null) {
                    throw result.getError();
                } else {
                    throw new RuntimeException("Impossible behavior");
                }
            }
        }
        return args;
    }
}
