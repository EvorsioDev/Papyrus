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
        PlaceholderContainer.Builder builder = PlaceholderContainer.builder();
        for (Method method : target.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(PlaceholderEntry.class)) continue;
            PlaceholderEntry placeholderEntry = method.getAnnotation(PlaceholderEntry.class);
            PlaceholderId id = PlaceholderId.of(placeholderEntry.namespace(), placeholderEntry.key());
            try {
                MethodPlaceholder placeholder = new MethodPlaceholder(id, method, target, parsers);
                builder = builder.registerPlaceholder(placeholder);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return builder.build();
    }
}
