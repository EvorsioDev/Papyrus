package ru.armagidon.papyrus.placeholder;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import ru.armagidon.papyrus.text.ReplacementContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class PlaceholderContainerTest {

    @Test
    void registerPlaceholder() {
        PlaceholderId id = PlaceholderId.of("test", "key");
        TestPlaceholder placeholder1 = new TestPlaceholder(id);
        TestPlaceholder placeholder2 = new TestPlaceholder(id);

        PlaceholderContainer.Builder builder = PlaceholderContainer.builder();
        assertThrows(IllegalArgumentException.class, () -> builder.registerPlaceholder(placeholder1).registerPlaceholder(placeholder2));
    }

    @Test
    void registerPlaceholdersAll() {
        PlaceholderId id = PlaceholderId.of("test", "key");
        TestPlaceholder placeholder1 = new TestPlaceholder(id);
        TestPlaceholder placeholder2 = new TestPlaceholder(id);

        PlaceholderContainer.Builder builder = PlaceholderContainer.builder();
        assertThrows(IllegalArgumentException.class, () -> builder.registerPlaceholdersAll(List.of(placeholder1)).registerPlaceholdersAll(List.of(placeholder2)));
    }

    @Test
    void getPlaceholderById() {
        TestPlaceholder placeholder1 = new TestPlaceholder(PlaceholderId.of("test", "key1"));
        TestPlaceholder placeholder2 = new TestPlaceholder(PlaceholderId.of("test", "key2"));
        TestPlaceholder placeholder3 = new TestPlaceholder(PlaceholderId.of("test1", "key3"));
        PlaceholderContainer container = PlaceholderContainer.builder().registerPlaceholdersAll(List.of(placeholder1, placeholder2, placeholder3)).build();
        assertEquals(placeholder1, container.getPlaceholder("test","key1").orElse(null));
    }

    private record TestPlaceholder(PlaceholderId id) implements Placeholder {
        @Override
        public @NotNull CompletableFuture<Optional<Component>> parsePlaceholderContents(@NotNull ReplacementContext context) {
            return CompletableFuture.completedFuture(Optional.of(Component.text("hello")));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestPlaceholder that)) return false;

            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }
    }

}