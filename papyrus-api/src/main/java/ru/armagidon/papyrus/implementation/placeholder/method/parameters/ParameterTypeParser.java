package ru.armagidon.papyrus.implementation.placeholder.method.parameters;

import org.jetbrains.annotations.NotNull;
import ru.armagidon.papyrus.placeholder.PlaceholderContext;

import java.util.Queue;

public interface ParameterTypeParser<T>
{
    @NotNull ParameterParseResult<T> parse(@NotNull PlaceholderContext context, Queue<String> parameterQueue);
}
