package ru.armagidon.papyrus.implementation.placeholder;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import ru.armagidon.papyrus.placeholder.Placeholder;
import ru.armagidon.papyrus.placeholder.PlaceholderContainer;
import ru.armagidon.papyrus.placeholder.PlaceholderId;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlaceholderContainerImpl implements PlaceholderContainer {

    private final Set<Placeholder> placeholders;

    private PlaceholderContainerImpl(@NotNull Set<Placeholder> placeholders) {
        Validate.notNull(placeholders);
        this.placeholders = ImmutableSet.copyOf(placeholders);
    }

    @Override
    public Optional<Placeholder> getPlaceholder(@NotNull PlaceholderId id) {
        Validate.notNull(id);
        return placeholders.parallelStream().filter(input -> input.id().equals(id)).findFirst();
    }

    @Override
    public Optional<Placeholder> getPlaceholder(@NotNull String namespace, @NotNull String id) {
        Validate.notEmpty(namespace);
        Validate.notEmpty(id);
        return getPlaceholder(PlaceholderId.of(namespace, id));
    }

    @Override
    public @NotNull Collection<Placeholder> getAllPlaceholders() {
        return placeholders;
    }

    public static final class BuilderImpl implements PlaceholderContainer.Builder {

        private final Collection<Placeholder> placeholders;

        public BuilderImpl(Collection<Placeholder> placeholders) {
            this.placeholders = placeholders;
        }

        @Override
        public @NotNull Builder registerPlaceholder(@NotNull Placeholder placeholder) {
            if (this.placeholders.contains(placeholder))
                throw new IllegalArgumentException("Attempt to register duplicate placeholder with id" + placeholder.id());
            return new BuilderImpl(Stream.concat(Stream.of(placeholder), placeholders.stream()).collect(Collectors.toUnmodifiableSet()));
        }

        @Override
        public @NotNull Builder registerPlaceholdersAll(@NotNull Collection<Placeholder> placeholders) {
            Set<Placeholder> duplicates = Stream.of(placeholders, this.placeholders).map(Set::copyOf).reduce(Sets::intersection).orElse(Set.of());
            if (!duplicates.isEmpty()) {
                throw new IllegalArgumentException("Attempt to register duplicate placeholders: " + duplicates);
            }
            return new BuilderImpl(Stream.of(placeholders, this.placeholders).flatMap(Collection::stream).collect(Collectors.toUnmodifiableSet()));
        }

        @Override
        public @NotNull Builder append(@NotNull PlaceholderContainer another) {
            return registerPlaceholdersAll(another.getAllPlaceholders());
        }

        @Override
        public @NotNull PlaceholderContainer build() {
            return new PlaceholderContainerImpl(Set.copyOf(placeholders));
        }
    }
}
