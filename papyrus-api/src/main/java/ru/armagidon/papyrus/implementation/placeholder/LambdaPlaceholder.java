package ru.armagidon.papyrus.implementation.placeholder;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import ru.armagidon.papyrus.placeholder.Placeholder;
import ru.armagidon.papyrus.placeholder.PlaceholderId;
import ru.armagidon.papyrus.text.ReplacementContext;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.logging.Logger;

public class LambdaPlaceholder implements Placeholder
{

    private static final Logger LOGGER = Logger.getLogger(LambdaPlaceholder.class.getName());
    private final PlaceholderId placeholderId;
    private final Function<ReplacementContext, CompletableFuture<Optional<Component>>> parseFunction;

    public LambdaPlaceholder(PlaceholderId placeholderId, Function<ReplacementContext, CompletableFuture<Optional<Component>>> parseFunction) {
        Validate.notNull(placeholderId);
        Validate.notNull(parseFunction);
        this.placeholderId = placeholderId;
        this.parseFunction = parseFunction.andThen(Objects::requireNonNull)
                .andThen(future -> future.thenApply(result -> {
                    if (result == null) {
                        LOGGER.severe("Lambda placeholder returns null instead of optional");
                        return Optional.empty();
                    }
                    return result;
                }));
    }

    @Override
    public @NotNull PlaceholderId id() {
        return placeholderId;
    }

    @Override
    public @NotNull CompletableFuture<Optional<Component>> parsePlaceholderContents(@NotNull ReplacementContext context) {
        Validate.notNull(context);
        return parseFunction.apply(context);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Placeholder that)) return false;

        return placeholderId.equals(that.id());
    }

    @Override
    public int hashCode() {
        return placeholderId.hashCode();
    }
}
