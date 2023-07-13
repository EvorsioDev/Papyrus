package ru.armagidon.papyrus.utils.placeholderloader;

import net.kyori.adventure.text.Component;

public interface LoadingPlaceholder
{
    Component getCurrent();

    void place(Runnable update);

    void finish();
}
