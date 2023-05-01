package ru.armagidon.papyrus.placeholder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface PlaceholderContainer<P>
{
    void registerPlaceholder(@NotNull Placeholder<P> placeholder);

    void registerPlaceholdersAll(@NotNull PlaceholderContainer<P> placeholders);

    @NotNull Collection<Placeholder<P>> registeredPlaceholders();

    @NotNull Collection<Placeholder<P>> getAllPlaceholdersByNamespace(@NotNull String namespace);

    @Nullable Placeholder<P> getPlaceholderById(String namespace, String name);

    default @Nullable Placeholder<P> getPlaceholderById(PlaceholderId id) {
        return getPlaceholderById(id.namespace(), id.key());
    }

    interface Builder<P> {
        @NotNull Builder<P> registerPlaceholder(@NotNull Placeholder<P> placeholder);

        @NotNull Builder<P> registerPlaceholdersAll(@NotNull Collection<Placeholder<P>> placeholders);

        @NotNull Builder<P> append(@NotNull PlaceholderContainer<P> another);

        @NotNull PlaceholderContainer<P> build();
    }

}
