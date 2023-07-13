package ru.armagidon.papyrus.utils.placeholderloader;

import net.kyori.adventure.text.Component;

public class StaticLoadingPlaceholder implements LoadingPlaceholder {

    private final Component component;

    public StaticLoadingPlaceholder(Component component) {
        this.component = component;
    }

    @Override
    public Component getCurrent() {
        return component;
    }

    @Override
    public void place(Runnable update) {}


    @Override
    public void finish() {}
}
