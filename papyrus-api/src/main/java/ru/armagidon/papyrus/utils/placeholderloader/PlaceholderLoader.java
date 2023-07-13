package ru.armagidon.papyrus.utils.placeholderloader;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang.Validate;
import ru.armagidon.papyrus.implementation.placeholder.LambdaPlaceholder;
import ru.armagidon.papyrus.placeholder.Placeholder;
import ru.armagidon.papyrus.placeholder.PlaceholderContainer;
import ru.armagidon.papyrus.placeholder.PlaceholderContext;
import ru.armagidon.papyrus.text.TextParser;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PlaceholderLoader {

    private final TextParser textParser;
    private final LoadingPlaceholder loadingPlaceholder;

    public PlaceholderLoader(TextParser textParser, LoadingPlaceholder loadingPlaceholder) {
        Validate.notNull(textParser);
        Validate.notNull(loadingPlaceholder);
        this.textParser = textParser;
        this.loadingPlaceholder = loadingPlaceholder;
    }

    public CompletableFuture<Component> load(PlaceholderContext context, Component input, PlaceholderContainer source, Consumer<Component> updateFunction) {
        PlaceholderContainer loadingSource;
        {
            PlaceholderContainer.Builder loadingSourceBuilder = PlaceholderContainer.builder();
            for (Placeholder placeholder : source.getAllPlaceholders()) {
                LambdaPlaceholder proxy = new LambdaPlaceholder(placeholder.id(), replacementContext ->
                        CompletableFuture.completedFuture(loadingPlaceholder.getCurrent()).thenApply(Optional::of));
                loadingSourceBuilder = loadingSourceBuilder.registerPlaceholder(proxy);
            }
            loadingSource = loadingSourceBuilder.build();
        }
        return textParser.parse(context, input, loadingSource)
                .whenComplete((component, throwable) -> loadingPlaceholder.place(() -> textParser.parse(context, input, loadingSource).thenAccept(updateFunction).join()))
                .whenComplete((component, throwable) -> textParser.parse(context, input, loadingSource).thenAccept(updateFunction).thenRun(loadingPlaceholder::finish));
    }
}
