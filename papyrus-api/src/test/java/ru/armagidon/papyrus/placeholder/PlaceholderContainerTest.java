package ru.armagidon.papyrus.placeholder;

import org.junit.jupiter.api.Test;
import ru.armagidon.papyrus.implementation.placeholder.LambdaPlaceholder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlaceholderContainerTest {


    private Placeholder createPlaceholder(PlaceholderId id) {
        return new LambdaPlaceholder(id, context -> CompletableFuture.completedFuture(Optional.empty()));
    }

    @Test
    void registerPlaceholder() {
        PlaceholderId id = PlaceholderId.of("test", "key");
        Placeholder placeholder1 = createPlaceholder(id);
        Placeholder placeholder2 = createPlaceholder(id);

        PlaceholderContainer.Builder builder = PlaceholderContainer.builder();
        assertThrows(IllegalArgumentException.class, () -> builder.registerPlaceholder(placeholder1)
                .registerPlaceholder(placeholder2));
    }

    @Test
    void registerPlaceholdersAll() {
        PlaceholderId id = PlaceholderId.of("test", "key");
        Placeholder placeholder1 = createPlaceholder(id);
        Placeholder placeholder2 = createPlaceholder(id);

        PlaceholderContainer.Builder builder = PlaceholderContainer.builder();
        assertThrows(IllegalArgumentException.class, () -> builder.registerPlaceholdersAll(List.of(placeholder1))
                .registerPlaceholdersAll(List.of(placeholder2)));
    }

    @Test
    void getPlaceholderById() {
        Placeholder placeholder1 = createPlaceholder(PlaceholderId.of("test", "key1"));
        Placeholder placeholder2 = createPlaceholder(PlaceholderId.of("test", "key2"));
        Placeholder placeholder3 = createPlaceholder(PlaceholderId.of("test1", "key3"));
        PlaceholderContainer container = PlaceholderContainer.builder().registerPlaceholdersAll(List.of(placeholder1, placeholder2, placeholder3)).build();
        assertEquals(placeholder1, container.getPlaceholder("test","key1").orElse(null));
    }

}