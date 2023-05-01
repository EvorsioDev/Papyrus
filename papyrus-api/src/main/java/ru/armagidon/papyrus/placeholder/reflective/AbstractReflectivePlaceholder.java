package ru.armagidon.papyrus.placeholder.reflective;

import com.google.common.primitives.Primitives;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.armagidon.papyrus.placeholder.Placeholder;
import ru.armagidon.papyrus.placeholder.PlaceholderId;
import ru.armagidon.papyrus.placeholder.params.ParamSerializerCollection;
import ru.armagidon.papyrus.placeholder.reflective.handler.PlaceholderEntry;
import ru.armagidon.papyrus.placeholder.reflective.handler.PlaceholderHandler;
import ru.armagidon.papyrus.placeholder.reflective.handler.PlaceholderViewer;
import ru.armagidon.papyrus.placeholder.params.ParamSerializer;
import ru.armagidon.papyrus.text.ReplacementContext;
import ru.armagidon.papyrus.text.TextParserException;

import java.lang.invoke.LambdaMetafactory;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractReflectivePlaceholder<P> implements Placeholder<P>
{
    private final PlaceholderId id;
    private final Method placeholderMethod;
    private final PlaceholderHandler handler;
    private final ParamSerializerCollection<P> serializerCollection;

    public AbstractReflectivePlaceholder(@NotNull PlaceholderId id,
                                         @NotNull Method placeholderMethod,
                                         @NotNull PlaceholderHandler handler,
                                         @NotNull ParamSerializerCollection<P> serializerCollection) {
        validateInputMethod(placeholderMethod);
        Validate.notNull(serializerCollection);
        Validate.notNull(id);
        Validate.notNull(handler);
        this.handler = handler;
        this.id = id;
        this.placeholderMethod = placeholderMethod;
        this.serializerCollection = serializerCollection;
    }

    @Override
    public @NotNull PlaceholderId id() {
        return id;
    }

    @Override
    public CompletableFuture<@Nullable Component> apply(@NotNull ReplacementContext<P> context) {
        try {
            placeholderMethod.setAccessible(true);
            CompletableFuture<?> placeholder = (CompletableFuture<?>) placeholderMethod.invoke(handler, context.getParams().toArray());
            return placeholder.thenApply(object -> {
                if (object == null) return null;
                if (object instanceof Integer number) {
                    return Component.text(number);
                } else if (object instanceof Float number) {
                    return Component.text(number);
                } else if (object instanceof Double number) {
                    return Component.text(number);
                } else if (object instanceof Long number) {
                    return Component.text(number);
                } else if (object instanceof Short number) {
                    return Component.text(number);
                } else if (object instanceof Byte number) {
                    return Component.text(number);
                } else if (object instanceof Character character) {
                    return Component.text(character);
                } else if (object instanceof Boolean bool){
                    return Component.text(bool);
                } else if (object instanceof String string){
                    return Component.text(string);
                } else {
                    return (Component) object;
                }
            });
        } catch (IllegalAccessException | InvocationTargetException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public CompletableFuture<List<?>> parseParams(@Nullable P viewer, String[] input) {
        return CompletableFuture.completedFuture(Arrays.stream(input).collect(Collectors.toCollection(LinkedList::new)))
                .thenApplyAsync(queue -> Arrays.stream(placeholderMethod.getParameters()).map(parameter -> {
                    if (parameter.isAnnotationPresent(PlaceholderViewer.class)) {
                        if (!parameter.getType().isAssignableFrom(viewerClass()))
                            throw new TextParserException("PlaceholderViewer can only be attached on parameters of type " + viewerClass());
                        return viewer;
                    } else {
                        return Optional.ofNullable(serializerCollection.getSerializerMap().get(parameter.getType()))
                                .map(serializer -> serializer.parse(viewer, queue))
                                .orElseThrow(() -> new TextParserException("Serializer for type " + parameter.getType() + " is not found"));
                    }
                }).toList());
    }

    private void validateInputMethod(Method inputMethod) {
        Validate.notNull(inputMethod);
        if (!inputMethod.isAnnotationPresent(PlaceholderEntry.class))
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractReflectivePlaceholder<?> that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                '}';
    }

    protected abstract Class<P> viewerClass();

}
