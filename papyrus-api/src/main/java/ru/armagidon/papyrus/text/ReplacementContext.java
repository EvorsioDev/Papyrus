package ru.armagidon.papyrus.text;

import org.jetbrains.annotations.NotNull;
import ru.armagidon.papyrus.placeholder.PlaceholderContext;

import java.util.List;

public interface ReplacementContext {

    @NotNull PlaceholderContext getPlaceholderContext();

    @NotNull List<@NotNull String> getParameters();

    static ReplacementContext context(PlaceholderContext context, List<@NotNull String> parameters) {
        return new ReplacementContext() {
            @Override
            public @NotNull PlaceholderContext getPlaceholderContext() {
                return context;
            }

            @Override
            public @NotNull List<@NotNull String> getParameters() {
                return parameters;
            }
        };
    }
}
