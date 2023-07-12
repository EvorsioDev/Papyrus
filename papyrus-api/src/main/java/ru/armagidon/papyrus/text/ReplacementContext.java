package ru.armagidon.papyrus.text;

import org.jetbrains.annotations.NotNull;
import ru.armagidon.papyrus.placeholder.PlaceholderContext;

import java.util.List;

public interface ReplacementContext {

    @NotNull PlaceholderContext getPlaceholderContext();

    @NotNull List<@NotNull String> getParameters();
}
