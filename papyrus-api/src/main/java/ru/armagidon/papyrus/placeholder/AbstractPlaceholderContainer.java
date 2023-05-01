package ru.armagidon.papyrus.placeholder;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.ServiceConfigurationError;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractPlaceholderContainer<P> implements PlaceholderContainer<P> {

    private final Collection<Placeholder<P>> placeholders;

    public AbstractPlaceholderContainer(@NotNull Collection<Placeholder<P>> placeholders) {
        Validate.notNull(placeholders);
        this.placeholders = ConcurrentHashMap.newKeySet();
        this.placeholders.addAll(placeholders);
    }

    @Override
    public void registerPlaceholder(@NotNull Placeholder<P> placeholder) {
        Validate.notNull(placeholder);
        synchronized (this.placeholders) {
            if (!this.placeholders.add(placeholder))
                throw new IllegalArgumentException("Cannot register duplicate placeholder");
        }
    }

    @Override
    public void registerPlaceholdersAll(@NotNull PlaceholderContainer<P> placeholders) {
        synchronized (this.placeholders) {
            Set<? extends Placeholder<P>> duplicates = Sets.intersection(Set.copyOf(this.placeholders), Set.copyOf(placeholders.registeredPlaceholders()));
            if (!duplicates.isEmpty())
                throw new IllegalArgumentException("Attempt to register duplicate placeholders " + duplicates);
            this.placeholders.addAll(placeholders.registeredPlaceholders());
        }
    }

    @Override
    public @NotNull Collection<Placeholder<P>> registeredPlaceholders() {
        return ImmutableSet.copyOf(placeholders);
    }

    @Override
    public @NotNull Collection<Placeholder<P>> getAllPlaceholdersByNamespace(@NotNull String namespace) {
        synchronized (this.placeholders) {
            return placeholders.stream().filter(playerPlaceholder ->
                    playerPlaceholder.id().namespace().equals(namespace)).toList();
        }
    }

    @Override
    public @Nullable Placeholder<P> getPlaceholderById(String namespace, String name) {
        synchronized (this.placeholders) {
            for (Placeholder<P> placeholder : placeholders) {
                if (placeholder.id().namespace().equals(namespace) && placeholder.id().key().equals(name))
                    return placeholder;
            }
        }
        return null;
    }

    public static abstract class AbstractBuilder<P> implements Builder<P> {

        protected final Collection<Placeholder<P>> placeholders = new HashSet<>();


        @Override
        public @NotNull Builder<P> registerPlaceholder(@NotNull Placeholder<P> placeholder) {
            Validate.notNull(placeholder);
            this.placeholders.add(placeholder);
            return this;
        }


        @Override
        public @NotNull Builder<P> registerPlaceholdersAll(@NotNull Collection<Placeholder<P>> placeholders) {
            Validate.notNull(placeholders);
            this.placeholders.addAll(placeholders);
            return this;
        }

        @Override
        public @NotNull Builder<P> append(@NotNull PlaceholderContainer<P> another) {
            Validate.notNull(another);
            this.placeholders.addAll(another.registeredPlaceholders());
            return this;
        }

        @Override
        public abstract @NotNull PlaceholderContainer<P> build();
    }
}
