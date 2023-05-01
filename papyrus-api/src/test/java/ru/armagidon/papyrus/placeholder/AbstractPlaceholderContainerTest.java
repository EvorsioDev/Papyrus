package ru.armagidon.papyrus.placeholder;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import ru.armagidon.papyrus.testobjects.Viewer;
import ru.armagidon.papyrus.text.ReplacementContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class AbstractPlaceholderContainerTest {

    @Test
    void registerPlaceholder() {
        @NotNull PlaceholderContainer<Viewer> container = TestPlaceholderContainer.builder().build();
        PlaceholderId id = new PlaceholderIdImpl("test", "key");
        TestPlaceholder placeholder1 = new TestPlaceholder(id);
        TestPlaceholder placeholder2 = new TestPlaceholder(id);
        assertDoesNotThrow(() -> container.registerPlaceholder(placeholder1));
        assertThrows(IllegalArgumentException.class, () -> container.registerPlaceholder(placeholder2));
    }

    @Test
    void registerPlaceholdersAll() {
        PlaceholderId id = new PlaceholderIdImpl("test", "key");
        TestPlaceholder placeholder1 = new TestPlaceholder(id);
        TestPlaceholder placeholder2 = new TestPlaceholder(id);
        TestPlaceholderContainer container1 = new TestPlaceholderContainer(List.of(placeholder1));
        TestPlaceholderContainer container2 = new TestPlaceholderContainer(List.of(placeholder2));
        assertThrows(IllegalArgumentException.class, () -> container1.registerPlaceholdersAll(container2));
    }

    @Test
    void getAllPlaceholdersByNamespace() {
        TestPlaceholder placeholder1 = new TestPlaceholder(new PlaceholderIdImpl("test", "key1"));
        TestPlaceholder placeholder2 = new TestPlaceholder(new PlaceholderIdImpl("test", "key2"));
        TestPlaceholder placeholder3 = new TestPlaceholder(new PlaceholderIdImpl("test1", "key3"));
        TestPlaceholderContainer container = new TestPlaceholderContainer(List.of(placeholder1, placeholder2, placeholder3));
        List<TestPlaceholder> expected = List.of(placeholder1, placeholder2);
        var out = container.getAllPlaceholdersByNamespace("test");
        assertIterableEquals(expected, out);
    }

    @Test
    void getPlaceholderById() {
        TestPlaceholder placeholder1 = new TestPlaceholder(new PlaceholderIdImpl("test", "key1"));
        TestPlaceholder placeholder2 = new TestPlaceholder(new PlaceholderIdImpl("test", "key2"));
        TestPlaceholder placeholder3 = new TestPlaceholder(new PlaceholderIdImpl("test1", "key3"));
        TestPlaceholderContainer container = new TestPlaceholderContainer(List.of(placeholder1, placeholder2, placeholder3));
        assertNotNull(container.getPlaceholderById("test","key1"));
        assertEquals(placeholder1, container.getPlaceholderById("test","key1"));
    }

    private record TestPlaceholder(PlaceholderId id) implements Placeholder<Viewer> {
        @Override
        public CompletableFuture<@Nullable Component> apply(@NotNull ReplacementContext<Viewer> context) {
            return CompletableFuture.completedFuture(Component.text("hello"));
        }

        @Override
        public CompletableFuture<List<?>> parseParams(@Nullable Viewer viewer, String[] input) {
            return CompletableFuture.completedFuture(List.of());
        }
    }
}