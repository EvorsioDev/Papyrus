package ru.armagidon.papyrus.implementation.placeholder;

import ru.armagidon.papyrus.placeholder.PlaceholderContext;

import java.util.HashMap;
import java.util.Map;

public class SimplePlaceholderContext implements PlaceholderContext
{

    private final Map<String, Object> contextMap;

    public SimplePlaceholderContext(Map<String, Object> contextMap) {
        this.contextMap = contextMap;
    }

    @Override
    public <T> T getContext(String key) {
        return (T) contextMap.get(key);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Map<String, Object> contextMap = new HashMap<>();

        public Builder set(String key, Object value) {
            contextMap.put(key, value);
            return this;
        }

        public Builder viewer(Object viewer) {
            contextMap.put(VIEWER_KEY, viewer);
            return this;
        }

        public PlaceholderContext build() {
            return new SimplePlaceholderContext(contextMap);
        }
    }
}
