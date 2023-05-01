package ru.armagidon.papyrus.text;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.armagidon.papyrus.placeholder.PlaceholderContainer;

import java.util.concurrent.CompletableFuture;

public interface TextParser<P>
{

    @NotNull CompletableFuture<@NotNull Component> parse(@Nullable P viewer, @NotNull Component input, @NotNull PlaceholderContainer<P> container);

    @NotNull CompletableFuture<@NotNull Component> parse(@Nullable P viewer, @NotNull String input, @NotNull PlaceholderContainer<P> container);

    @NotNull CompletableFuture<@NotNull Component> parse(@Nullable P viewer, @NotNull Component input);

    @NotNull CompletableFuture<@NotNull Component> parse(@Nullable P viewer, @NotNull String input);

    @Blocking
    default @NotNull Component parseSync(@Nullable P viewer, @NotNull String input) {
        return parse(viewer,input).join();
    }

    @Blocking
    default @NotNull Component parseSync(@Nullable P viewer, @NotNull String input, @NotNull PlaceholderContainer<P> container) {
        return parse(viewer,input, container).join();
    }


    @Blocking
    default @NotNull Component parseSync(@Nullable P viewer, @NotNull Component input) {
        return parse(viewer,input).join();
    }

    @Blocking
    default @NotNull Component parseSync(@Nullable P viewer, @NotNull Component input, @NotNull PlaceholderContainer<P> container) {
        return parse(viewer,input, container).join();
    }

}
