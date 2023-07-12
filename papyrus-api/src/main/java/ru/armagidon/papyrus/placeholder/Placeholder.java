package ru.armagidon.papyrus.placeholder;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import ru.armagidon.papyrus.text.ReplacementContext;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface Placeholder {

    @NotNull PlaceholderId id();

    @NotNull CompletableFuture<Optional<Component>> parsePlaceholderContents(@NotNull ReplacementContext context);

}
