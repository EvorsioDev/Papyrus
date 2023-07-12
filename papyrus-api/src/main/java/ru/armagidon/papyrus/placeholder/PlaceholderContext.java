package ru.armagidon.papyrus.placeholder;

public interface PlaceholderContext {

    String VIEWER_KEY = "viewer";


    <T> T getContext(String key);
}
