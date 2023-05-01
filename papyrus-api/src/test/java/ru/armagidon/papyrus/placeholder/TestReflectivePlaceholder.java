package ru.armagidon.papyrus.placeholder;

import org.jetbrains.annotations.NotNull;
import ru.armagidon.papyrus.placeholder.params.ParamSerializerCollection;
import ru.armagidon.papyrus.placeholder.reflective.AbstractReflectivePlaceholder;
import ru.armagidon.papyrus.placeholder.reflective.handler.PlaceholderHandler;
import ru.armagidon.papyrus.testobjects.Viewer;

import java.lang.reflect.Method;

public class TestReflectivePlaceholder extends AbstractReflectivePlaceholder<Viewer>
{
    public TestReflectivePlaceholder(@NotNull PlaceholderId id, @NotNull Method placeholderMethod, @NotNull PlaceholderHandler handler, @NotNull ParamSerializerCollection<Viewer> serializerCollection) {
        super(id, placeholderMethod, handler, serializerCollection);
    }

    @Override
    protected Class<Viewer> viewerClass() {
        return Viewer.class;
    }
}
