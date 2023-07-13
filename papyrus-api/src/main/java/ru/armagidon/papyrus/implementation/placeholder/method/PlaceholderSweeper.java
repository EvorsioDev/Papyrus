package ru.armagidon.papyrus.implementation.placeholder.method;

import org.apache.commons.lang.Validate;
import ru.armagidon.papyrus.implementation.placeholder.method.parameters.ParameterParserCollection;
import ru.armagidon.papyrus.placeholder.PlaceholderContainer;
import ru.armagidon.papyrus.placeholder.PlaceholderId;

import java.lang.reflect.Method;

public class PlaceholderSweeper {
    public PlaceholderContainer sweep(PlaceholderSweeperTarget target, ParameterParserCollection parsers) {
        Validate.notNull(target);
        Validate.notNull(parsers);
        if (!target.getClass().isAnnotationPresent(PlaceholderNamespace.class))
            throw new IllegalArgumentException("Target class must declare namespace");
        String namespace = target.getClass().getAnnotation(PlaceholderNamespace.class).value();
        Validate.notEmpty(namespace);
        PlaceholderContainer.Builder builder = PlaceholderContainer.builder();
        for (Method method : target.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(PlaceholderKey.class)) continue;
            PlaceholderKey placeholderKey = method.getAnnotation(PlaceholderKey.class);
            Validate.notEmpty(placeholderKey.value());
            PlaceholderId id = PlaceholderId.of(namespace, placeholderKey.value());
            try {
                MethodPlaceholder placeholder = new MethodPlaceholder(id, method, target, parsers);
                builder = builder.registerPlaceholder(placeholder);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return builder.build();
    }
}
