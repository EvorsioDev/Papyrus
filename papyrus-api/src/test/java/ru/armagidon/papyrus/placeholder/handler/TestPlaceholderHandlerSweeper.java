package ru.armagidon.papyrus.placeholder.handler;

import org.jetbrains.annotations.NotNull;
import ru.armagidon.papyrus.placeholder.AbstractPlaceholderContainer;
import ru.armagidon.papyrus.placeholder.PlaceholderId;
import ru.armagidon.papyrus.placeholder.TestPlaceholderContainer;
import ru.armagidon.papyrus.placeholder.TestReflectivePlaceholder;
import ru.armagidon.papyrus.placeholder.params.ParamSerializerCollection;
import ru.armagidon.papyrus.placeholder.reflective.AbstractReflectivePlaceholder;
import ru.armagidon.papyrus.placeholder.reflective.handler.PlaceholderHandler;
import ru.armagidon.papyrus.placeholder.reflective.handler.sweeper.AbstractPlaceholderHandlerSweeper;
import ru.armagidon.papyrus.testobjects.Viewer;

import java.lang.reflect.Method;

public class TestPlaceholderHandlerSweeper extends AbstractPlaceholderHandlerSweeper<Viewer>
{
    public TestPlaceholderHandlerSweeper(@NotNull ParamSerializerCollection<Viewer> paramSerializers) {
        super(paramSerializers);
    }

    @Override
    protected AbstractReflectivePlaceholder<Viewer> placeholderFactory(PlaceholderId id, Method method, PlaceholderHandler placeholderHandler, ParamSerializerCollection<Viewer> serializerCollection) {
        return new TestReflectivePlaceholder(id, method, placeholderHandler, serializerCollection);
    }

    @Override
    protected AbstractPlaceholderContainer.AbstractBuilder<Viewer> containerBuilderFactory() {
        return TestPlaceholderContainer.builder();
    }
}
