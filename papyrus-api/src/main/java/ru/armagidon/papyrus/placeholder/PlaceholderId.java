package ru.armagidon.papyrus.placeholder;

public interface PlaceholderId {
    String namespace();
    String key();

    static PlaceholderId of(String namespace, String key) {
        return new PlaceholderIdImpl(namespace, key);
    }
}
