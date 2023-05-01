package ru.armagidon.papyrus.placeholder.params;

import org.jetbrains.annotations.Nullable;

import java.util.Queue;

public interface ParamSerializer<P, T>
{

    @Nullable T parse(@Nullable P viewer, Queue<String> params);
}
