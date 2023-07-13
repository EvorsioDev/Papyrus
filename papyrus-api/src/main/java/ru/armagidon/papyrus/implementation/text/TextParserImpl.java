package ru.armagidon.papyrus.implementation.text;

import com.google.common.base.Splitter;
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

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextParserImpl implements TextParser
{

    private final String separator;
    private final Pattern parserPattern;

    protected TextParserImpl(@NotNull Config config) {
        Validate.notNull(config);
        this.separator = config.separator();
        this.parserPattern = config.generatePattern();
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Component> parse(@NotNull PlaceholderContext context, @NotNull Component input, @NotNull PlaceholderContainer container) {
        return CompletableFuture.supplyAsync(() -> TextReplacementConfig.builder().match(parserPattern))
                .thenApply(builder -> builder.times(Integer.MAX_VALUE))
                .thenApply(replacementBuilder -> replacementBuilder.replacement((matchResult, componentBuilder) -> {
                    Queue<String> placeholderContentQueue = new LinkedList<>(Splitter.on(separator)
                            .splitToList(matchResult.group(1)));

                    if (placeholderContentQueue.size() < 2)
                        return componentBuilder;

                    PlaceholderId placeholderId = PlaceholderId.of(placeholderContentQueue.poll(), placeholderContentQueue.poll());
                    Optional<Placeholder> placeholder = container.getPlaceholder(placeholderId);

                    if (placeholder.isEmpty())
                        return componentBuilder;


                    ReplacementContext replacementContext = ReplacementContext.context(context, List.copyOf(placeholderContentQueue));

                    return placeholder.get().parsePlaceholderContents(replacementContext).join()
                            .orElse(componentBuilder.build());
                })).thenApply(AbstractBuilder::build)
                .thenApply(input::replaceText)
                .thenApply(Component::compact);
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