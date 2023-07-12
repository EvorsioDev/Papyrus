package ru.armagidon.papyrus.placeholder;

import org.jetbrains.annotations.NotNull;
import ru.armagidon.papyrus.implementation.placeholder.PlaceholderContainerImpl;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface PlaceholderContainer
{
    Optional<Placeholder> getPlaceholder(@NotNull PlaceholderId id);

    Optional<Placeholder> getPlaceholder(@NotNull String namespace, @NotNull String id);

    @NotNull Collection<Placeholder> getAllPlaceholders();

    static Builder builder() {
        return new PlaceholderContainerImpl.BuilderImpl(Set.of());
    }

    interface Builder {
        @NotNull Builder registerPlaceholder(@NotNull Placeholder placeholder);

        @NotNull Builder registerPlaceholdersAll(@NotNull Collection<Placeholder> placeholders);

        @NotNull Builder append(@NotNull PlaceholderContainer another);

        @NotNull PlaceholderContainer build();
    }

}
