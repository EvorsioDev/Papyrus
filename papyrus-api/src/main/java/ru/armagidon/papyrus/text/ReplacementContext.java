package ru.armagidon.papyrus.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ReplacementContext<P> {
    @Nullable P getPlayer();

    @NotNull List<?> getParams();

    @NotNull String getRawParams();
}
