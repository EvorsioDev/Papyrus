package ru.armagidon.papyrus.text;

import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;
import ru.armagidon.papyrus.implementation.placeholder.LambdaPlaceholder;
import ru.armagidon.papyrus.implementation.placeholder.SimplePlaceholderContext;
import ru.armagidon.papyrus.implementation.text.MultiSourceTextParserImpl;
import ru.armagidon.papyrus.implementation.text.TextParserImpl;
import ru.armagidon.papyrus.placeholder.PlaceholderContainer;
import ru.armagidon.papyrus.placeholder.PlaceholderId;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class MultiSourceTextParserTest {

    @Test
    void parse() {
        Component test = Component.text("Lorem %test_placeholderum2% ipsum huipsun %mf% pisdipsum %test_placeholderum% hrenolderum");
        MultiSourceTextParser parser = new MultiSourceTextParserImpl(TextParserImpl.config().build());
        {
            LambdaPlaceholder placeholder1 = new LambdaPlaceholder(PlaceholderId.of("test", "placeholderum"),
                    context -> CompletableFuture.completedFuture("PAPYRUS")
                            .thenApply(Component::text).thenApply(Optional::of));

            PlaceholderContainer container1 = PlaceholderContainer.builder().registerPlaceholder(placeholder1).build();
            parser.registerSource(container1);
        }
        {
            LambdaPlaceholder placeholder2 = new LambdaPlaceholder(PlaceholderId.of("test", "placeholderum2"),
                    context -> CompletableFuture.completedFuture("PAPYRUS")
                            .thenApply(Component::text).thenApply(Optional::of));

            PlaceholderContainer container2 = PlaceholderContainer.builder().registerPlaceholder(placeholder2).build();
            parser.registerSource(container2);
        }

        Component out = parser.parse(SimplePlaceholderContext.builder().build(), test).join();
        assertEquals(Component.text("Lorem PAPYRUS ipsum huipsun %mf% pisdipsum PAPYRUS hrenolderum"), out);
    }
}