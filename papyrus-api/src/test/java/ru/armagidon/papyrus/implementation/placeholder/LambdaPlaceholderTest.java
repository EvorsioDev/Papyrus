package ru.armagidon.papyrus.implementation.placeholder;

import org.junit.jupiter.api.Test;
import ru.armagidon.papyrus.placeholder.Placeholder;
import ru.armagidon.papyrus.placeholder.PlaceholderId;
import ru.armagidon.papyrus.text.ReplacementContext;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LambdaPlaceholderTest {

    @Test
    public void test_protection_against_null_future() {
        Placeholder placeholder = new LambdaPlaceholder(PlaceholderId.of("test", "protection"),
                context -> null);
        assertThrows(NullPointerException.class, () -> placeholder.parsePlaceholderContents(ReplacementContext.context(SimplePlaceholderContext.builder().build(), List.of())));
    }


    @Test
    public void test_protection_against_null_optional() {
        Placeholder placeholder = new LambdaPlaceholder(PlaceholderId.of("test", "protection"),
                context -> CompletableFuture.completedFuture(null));
        assertEquals(Optional.empty(), placeholder.parsePlaceholderContents(ReplacementContext.context(SimplePlaceholderContext.builder().build(), List.of())).join());
    }
}