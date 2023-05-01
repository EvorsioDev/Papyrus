package ru.armagidon.papyrus.placeholder;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.armagidon.papyrus.placeholder.params.ParamSerializer;
import ru.armagidon.papyrus.text.ReplacementContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Placeholder<P> {
    @NotNull PlaceholderId id();

    CompletableFuture<@Nullable Component> apply(@NotNull ReplacementContext<P> context);

    CompletableFuture<@NotNull List<?>> parseParams(@Nullable P viewer, @NotNull String[] input);
}
