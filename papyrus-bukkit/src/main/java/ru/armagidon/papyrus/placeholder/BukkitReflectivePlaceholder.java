package ru.armagidon.papyrus.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.armagidon.papyrus.placeholder.params.ParamSerializerCollection;
import ru.armagidon.papyrus.placeholder.reflective.handler.PlaceholderHandler;
import ru.armagidon.papyrus.placeholder.reflective.AbstractReflectivePlaceholder;

import java.lang.reflect.Method;

public class BukkitReflectivePlaceholder extends AbstractReflectivePlaceholder<Player> {
    public BukkitReflectivePlaceholder(@NotNull PlaceholderId id,
                                       @NotNull Method placeholderMethod,
                                       @NotNull PlaceholderHandler handler,
                                       @NotNull ParamSerializerCollection<Player> serializerCollection) {
        super(id, placeholderMethod, handler, serializerCollection);
    }

    @Override
    protected Class<Player> viewerClass() {
        return Player.class;
    }
}
