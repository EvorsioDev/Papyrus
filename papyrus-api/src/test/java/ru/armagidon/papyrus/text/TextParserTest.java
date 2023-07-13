package ru.armagidon.papyrus.text;

import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;
import ru.armagidon.papyrus.implementation.placeholder.LambdaPlaceholder;
import ru.armagidon.papyrus.implementation.placeholder.SimplePlaceholderContext;
import ru.armagidon.papyrus.implementation.text.TextParserImpl;
import ru.armagidon.papyrus.placeholder.PlaceholderContainer;
import ru.armagidon.papyrus.placeholder.PlaceholderId;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextParserTest {

    @Test
    void parse() {
        Component test = Component.text("Lorem ipsum huipsun pisdipsum %test_placeholderum% hrenolderum");
        LambdaPlaceholder placeholder = new LambdaPlaceholder(PlaceholderId.of("test", "placeholderum"),
                context -> CompletableFuture.completedFuture("PAPYRUS")
                        .thenApply(Component::text).thenApply(Optional::of));

        PlaceholderContainer container = PlaceholderContainer.builder().registerPlaceholder(placeholder).build();
        TextParser parser = TextParserImpl.config().build();
        Component component = parser.parse(SimplePlaceholderContext.builder().build(), test, container).join();

        assertEquals(Component.text("Lorem ipsum huipsun pisdipsum PAPYRUS hrenolderum").compact(), component.compact());
    }
}