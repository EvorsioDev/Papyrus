package ru.armagidon.papyrus.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class BukkitPlaceholderContainer extends AbstractPlaceholderContainer<Player> {
    public BukkitPlaceholderContainer(@NotNull Collection<Placeholder<Player>> placeholders) {
        super(placeholders);
    }

    public BukkitPlaceholderContainer() {
        this(List.of());
    }

    public static Builder<Player> builder() {
        return new BuilderImpl();
    }

    public static final class BuilderImpl extends AbstractBuilder<Player> {
        @Override
        public @NotNull PlaceholderContainer<Player> build() {
            return new BukkitPlaceholderContainer(this.placeholders);
        }
    }
}
