package ru.armagidon.papyrus.implementation.text;

import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import ru.armagidon.papyrus.placeholder.Placeholder;
import ru.armagidon.papyrus.placeholder.PlaceholderContainer;
import ru.armagidon.papyrus.placeholder.PlaceholderContext;
import ru.armagidon.papyrus.placeholder.PlaceholderId;
import ru.armagidon.papyrus.text.ReplacementContext;
import ru.armagidon.papyrus.text.TextParser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextParserImpl implements TextParser
{

    private final String separator;
    private final Pattern parserPattern;

    protected TextParserImpl(@NotNull Config config) {
        Validate.notNull(config);
        this.separator = Pattern.quote(config.separator());
        this.parserPattern = config.generatePattern();
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Component> parse(@NotNull PlaceholderContext context, @NotNull Component input, @NotNull PlaceholderContainer container) {
        return CompletableFuture.supplyAsync(() -> TextReplacementConfig.builder().match(parserPattern))
                .thenApply(builder -> builder.times(Integer.MAX_VALUE))
                .thenApply(replacementBuilder -> replacementBuilder.replacement((matchResult, componentBuilder) -> {
                    String matchedString = matchResult.group(1);
                    LinkedList<String> placeholderParts = Arrays.stream(matchedString.split(separator))
                            .collect(Collectors.toCollection(LinkedList::new));
                    if (placeholderParts.size() < 2) return componentBuilder;
                    String namespace = placeholderParts.poll();
                    String key = placeholderParts.poll();
                    PlaceholderId placeholderId = PlaceholderId.of(namespace, key);
                    Optional<Placeholder> placeholder = container.getPlaceholder(placeholderId);
                    if (placeholder.isEmpty())
                        return componentBuilder;

                    ReplacementContext replacementContext = new ReplacementContext() {
                        @Override
                        public @NotNull PlaceholderContext getPlaceholderContext() {
                            return context;
                        }

                        @Override
                        public @NotNull List<@NotNull String> getParameters() {
                            return List.copyOf(placeholderParts);
                        }
                    };
                    return placeholder.get().parsePlaceholderContents(replacementContext).join()
                            .orElse(componentBuilder.build());
                })).thenApply(AbstractBuilder::build).thenApply(input::replaceText);
    }

    public static Config config() {
        return new Config();
    }

    public static class Config {


        private String separator = "_";
        private String border = "%";

        @NotNull String separator() {
            return separator;
        }

        @NotNull String border() {
            return border;
        }


        public Config border(String border) {
            Validate.notEmpty(border);
            if (border.equals(separator()))
                throw new IllegalArgumentException("Border cannot match separator");
            this.border = border;
            return this;
        }


        public Config separator(String separator) {
            Validate.notEmpty(border);
            if (separator.equals(border()))
                throw new IllegalArgumentException("Border cannot match separator");
            this.separator = separator;
            return this;
        }

        public @NotNull Pattern generatePattern() {
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

        public @NotNull TextParserImpl build() {
            return new TextParserImpl(this);
        }

    }
}