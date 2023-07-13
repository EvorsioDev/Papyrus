package ru.armagidon.papyrus.implementation.placeholder;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import ru.armagidon.papyrus.placeholder.Placeholder;
import ru.armagidon.papyrus.placeholder.PlaceholderContext;
import ru.armagidon.papyrus.placeholder.PlaceholderId;
import ru.armagidon.papyrus.text.ReplacementContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class LambdaPlaceholderTest {

    @Test
    public void test_protection_against_null_future() {
        Placeholder placeholder = new LambdaPlaceholder(PlaceholderId.of("test", "protection"),
                context -> null);
        ReplacementContext context = new ReplacementContext() {
            @Override
            public @NotNull PlaceholderContext getPlaceholderContext() {
                return new SimplePlaceholderContext(Map.of());
            }

            @Override
            public @NotNull List<@NotNull String> getParameters() {
                return List.of();
            }
        };
        assertThrows(NullPointerException.class, () -> placeholder.parsePlaceholderContents(context));
    }


    @Test
    public void test_protection_against_null_optional() {
        Placeholder placeholder = new LambdaPlaceholder(PlaceholderId.of("test", "protection"),
                context -> CompletableFuture.completedFuture(null));
        ReplacementContext context = new ReplacementContext() {
            @Override
            public @NotNull PlaceholderContext getPlaceholderContext() {
                return new SimplePlaceholderContext(Map.of());
            }

            @Override
            public @NotNull List<@NotNull String> getParameters() {
                return List.of();
            }
        };
        assertEquals(Optional.empty(), placeholder.parsePlaceholderContents(context).join());
    }
}