package ru.armagidon.papyrus.placeholder.handler;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.armagidon.papyrus.placeholder.*;
import ru.armagidon.papyrus.placeholder.params.ParamSerializerCollection;
import ru.armagidon.papyrus.placeholder.reflective.handler.PlaceholderHandler;
import ru.armagidon.papyrus.placeholder.reflective.handler.sweeper.AbstractPlaceholderHandlerSweeper;
import ru.armagidon.papyrus.placeholder.reflective.AbstractReflectivePlaceholder;

import java.lang.reflect.Method;

public class BukkitPlaceholderHandlerSweeper extends AbstractPlaceholderHandlerSweeper<Player> {


    public BukkitPlaceholderHandlerSweeper(@NotNull ParamSerializerCollection<Player> serializers) {
        super(serializers);
    }

    @Override
    protected AbstractReflectivePlaceholder<Player> placeholderFactory(PlaceholderId id,
                                                                       Method method,
                                                                       PlaceholderHandler placeholderHandler,
                                                                       ParamSerializerCollection<Player> serializerCollection) {
        return new BukkitReflectivePlaceholder(id, method, placeholderHandler, serializerCollection);
    }

    @Override
    protected AbstractPlaceholderContainer.AbstractBuilder<Player> containerBuilderFactory() {
        return new BukkitPlaceholderContainer.BuilderImpl();
    }
}
