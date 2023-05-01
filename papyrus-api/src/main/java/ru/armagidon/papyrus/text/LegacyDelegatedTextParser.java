package ru.armagidon.papyrus.text;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.armagidon.papyrus.placeholder.PlaceholderContainer;

import java.util.concurrent.CompletableFuture;

public abstract class LegacyDelegatedTextParser<P> implements TextParser<P>
{

    private final TextParser<P> backing;

    public LegacyDelegatedTextParser(@NotNull TextParser<P> backing) {
        Validate.notNull(backing);
        this.backing = backing;
    }

    @Override
    public @NotNull CompletableFuture<Component> parse(@Nullable P viewer, @NotNull Component input, @NotNull PlaceholderContainer<P> container) {
        return backing.parse(viewer, input, container);
    }

    @Override
    public @NotNull CompletableFuture<Component> parse(@Nullable P viewer, @NotNull String input, @NotNull PlaceholderContainer<P> container) {
        return backing.parse(viewer, input, container);
    }

    @Override
    public @NotNull CompletableFuture<Component> parse(@Nullable P viewer, @NotNull Component input) {
        return backing.parse(viewer, input);
    }

    @Override
    public @NotNull CompletableFuture<Component> parse(@Nullable P viewer, @NotNull String input) {
        return backing.parse(viewer, input);
    }

    @Blocking
    public @NotNull String parseLegacy(P viewer, String input) {
        return parse(viewer, input).thenApply(this::convert).join();
    }

    protected abstract @NotNull String convert(Component component);
}
