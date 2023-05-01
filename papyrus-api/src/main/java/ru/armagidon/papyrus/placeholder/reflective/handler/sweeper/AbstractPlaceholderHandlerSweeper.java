package ru.armagidon.papyrus.placeholder.reflective.handler.sweeper;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import ru.armagidon.papyrus.placeholder.AbstractPlaceholderContainer;
import ru.armagidon.papyrus.placeholder.Placeholder;
import ru.armagidon.papyrus.placeholder.PlaceholderContainer;
import ru.armagidon.papyrus.placeholder.PlaceholderId;
import ru.armagidon.papyrus.placeholder.params.ParamSerializerCollection;
import ru.armagidon.papyrus.placeholder.reflective.AbstractReflectivePlaceholder;
import ru.armagidon.papyrus.placeholder.reflective.handler.PlaceholderEntry;
import ru.armagidon.papyrus.placeholder.reflective.handler.PlaceholderHandler;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractPlaceholderHandlerSweeper<P> {


    private final ParamSerializerCollection<P> paramSerializers;

    protected AbstractPlaceholderHandlerSweeper(@NotNull ParamSerializerCollection<P> paramSerializers) {
        Validate.notNull(paramSerializers);
        this.paramSerializers = paramSerializers;
    }

    public PlaceholderContainer<P> sweep(PlaceholderHandler handler) {
        List<Placeholder<P>> placeholders = new LinkedList<>();
        for (Method method : handler.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(PlaceholderEntry.class))
                continue;
            PlaceholderEntry placeholderData = method.getDeclaredAnnotation(PlaceholderEntry.class);
            PlaceholderId id = PlaceholderId.of(placeholderData.namespace(), placeholderData.key());
            placeholders.add(placeholderFactory(id, method, handler, paramSerializers));
        }
        return containerBuilderFactory().registerPlaceholdersAll(placeholders).build();
    }

    protected abstract AbstractReflectivePlaceholder<P> placeholderFactory(PlaceholderId id,
                                                                           Method method,
                                                                           PlaceholderHandler placeholderHandler,
                                                                           ParamSerializerCollection<P> serializerCollection);
    protected abstract AbstractPlaceholderContainer.AbstractBuilder<P> containerBuilderFactory();

}
