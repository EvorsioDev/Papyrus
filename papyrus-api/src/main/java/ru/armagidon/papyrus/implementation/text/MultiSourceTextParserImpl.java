package ru.armagidon.papyrus.implementation.text;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import ru.armagidon.papyrus.placeholder.PlaceholderContainer;
import ru.armagidon.papyrus.placeholder.PlaceholderContext;
import ru.armagidon.papyrus.text.MultiSourceTextParser;
import ru.armagidon.papyrus.text.TextParser;

import java.util.concurrent.CompletableFuture;

public class MultiSourceTextParserImpl implements MultiSourceTextParser
{

    private PlaceholderContainer superContainer;
    private final TextParser textParser;

    public MultiSourceTextParserImpl(TextParser textParser) {
        this.textParser = textParser;
        this.superContainer = PlaceholderContainer.builder().build();
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Component> parse(@NotNull PlaceholderContext context, @NotNull Component input) {
        return textParser.parse(context, input, superContainer);
    }

    public synchronized void registerSource(PlaceholderContainer source) {
        this.superContainer = PlaceholderContainer.builder().append(superContainer).append(source).build();
    }

    @Override
    public PlaceholderContainer getSuperSource() {
        return superContainer;
    }
}
