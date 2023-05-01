package ru.armagidon.papyrus.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.builder.AbstractBuilder;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.armagidon.papyrus.placeholder.Placeholder;
import ru.armagidon.papyrus.placeholder.PlaceholderContainer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public abstract class AbstractTextParser<P> implements TextParser<P>
{

    private final String separator;
    private final Pattern parserPattern;

    protected AbstractTextParser(@NotNull Config<P, ? extends AbstractTextParser<P>> config) {
        Validate.notNull(config);
        this.separator = Pattern.quote(config.separator());
        this.parserPattern = config.generatePattern();
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Component> parse(@Nullable P viewer, @NotNull Component input, @NotNull PlaceholderContainer<P> container) {
        return CompletableFuture.supplyAsync(() -> TextReplacementConfig.builder().match(parserPattern)
                .times(Integer.MAX_VALUE))
                .thenApply(configBuilder -> configBuilder.replacement((matchResult, componentBuilder) -> {
                    String inputString = matchResult.group(1);
                    String[] splitPlaceholder = inputString.split(separator);
                    if (splitPlaceholder.length < 2) return componentBuilder;
                    String namespace = splitPlaceholder[0];
                    String name = splitPlaceholder[1];
                    String[] rawParams = Arrays.copyOfRange(splitPlaceholder, 2, splitPlaceholder.length);
                    Placeholder<P> placeholder = container.getPlaceholderById(namespace, name);
                    if (placeholder == null)
                        return componentBuilder;
                    var params = placeholder.parseParams(viewer, rawParams).join();
                    return placeholder.apply(replacementContext(viewer, params, String.join(separator, rawParams))).join();
                })).thenApply(AbstractBuilder::build).thenApply(input::replaceText);
    }

    @Override
    public @NotNull CompletableFuture<Component> parse(@Nullable P viewer, @NotNull String input, @NotNull PlaceholderContainer<P> container) {
        return parse(viewer, convertToComponent(input), container);
    }

    @Override
    public @NotNull CompletableFuture<Component> parse(@Nullable P viewer, @NotNull Component input) {
        return parse(viewer, input, getGlobalContainer());
    }

    @Override
    public @NotNull CompletableFuture<Component> parse(@Nullable P viewer, @NotNull String input) {
        return parse(viewer, convertToComponent(input));
    }

    protected abstract Component convertToComponent(String input);

    protected abstract PlaceholderContainer<P> getGlobalContainer();

    protected abstract ReplacementContext<P> replacementContext(P viewer, List<?> params, String rawParams);

    public interface Config<P, O extends AbstractTextParser<P>> {

        @NotNull String separator();

        @NotNull String border();

        default @NotNull Pattern generatePattern() {
            StringBuilder output = new StringBuilder();
            output.append(Pattern.quote(border())).append('(');
            if (border().length() == 1) {
                output.append("[^").append(border()).append("]");
            } else {
                output.append(".");
            }
            output.append("+?)").append(Pattern.quote(border()));
            return Pattern.compile(output.toString());
        }

        @NotNull O build();

    }
}