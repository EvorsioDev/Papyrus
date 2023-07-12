package ru.armagidon.papyrus.placeholder;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

public interface PlaceholderId {
    String namespace();
    String key();

    static PlaceholderId of(String namespace, String key) {
        return new PlaceholderIdImpl(namespace, key);
    }

    record PlaceholderIdImpl(@NotNull String namespace, @NotNull String key) implements PlaceholderId {
        public PlaceholderIdImpl {
            Validate.notEmpty(namespace);
            Validate.notEmpty(key);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PlaceholderId that)) return false;

            if (!namespace.equals(that.namespace())) return false;
            return key.equals(that.key());
        }

        @Override
        public int hashCode() {
            int result = namespace.hashCode();
            result = 31 * result + key.hashCode();
            return result;
        }
    }

}
