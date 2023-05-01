package ru.armagidon.papyrus.placeholder;

import org.jetbrains.annotations.NotNull;
import ru.armagidon.papyrus.testobjects.Viewer;

import java.util.Collection;

public class TestPlaceholderContainer extends AbstractPlaceholderContainer<Viewer> {
    public TestPlaceholderContainer(@NotNull Collection<Placeholder<Viewer>> placeholders) {
        super(placeholders);
    }

    public static AbstractBuilder<Viewer> builder() {
        return new BuilderImpl();
    }

    public static final class BuilderImpl extends AbstractBuilder<Viewer> {
        @Override
        public @NotNull PlaceholderContainer<Viewer> build() {
            return new TestPlaceholderContainer(this.placeholders);
        }
    }

}
