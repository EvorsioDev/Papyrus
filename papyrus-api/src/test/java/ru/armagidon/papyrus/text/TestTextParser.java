package ru.armagidon.papyrus.text;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.armagidon.papyrus.placeholder.PlaceholderContainer;
import ru.armagidon.papyrus.placeholder.TestPlaceholderContainer;
import ru.armagidon.papyrus.testobjects.Viewer;

import java.util.List;

public class TestTextParser extends AbstractTextParser<Viewer> {

    private final TestPlaceholderContainer globalContainer;

    protected TestTextParser(@NotNull Config<Viewer, ? extends AbstractTextParser<Viewer>> config, TestPlaceholderContainer globalContainer) {
        super(config);
        this.globalContainer = globalContainer;
    }

    @Override
    protected Component convertToComponent(String input) {
        return Component.text(input);
    }

    @Override
    protected PlaceholderContainer<Viewer> getGlobalContainer() {
        return globalContainer;
    }

    @Override
    protected ReplacementContext<Viewer> replacementContext(Viewer viewer, List<?> params, String rawParams) {
        return new ReplacementContext<>() {
            @Override
            public @Nullable Viewer getPlayer() {
                return viewer;
            }

            @Override
            public @NotNull List<?> getParams() {
                return params;
            }

            @Override
            public @NotNull String getRawParams() {
                return rawParams;
            }
        };
    }

    public static TestTextParserConfig configurator() {
        return new TestTextParserConfig();
    }


    public static final class TestTextParserConfig implements Config<Viewer, TestTextParser> {

        private String separator = "_";
        private String border = "%";

        @Override
        public @NotNull String separator() {
            return separator;
        }

        @Override
        public @NotNull String border() {
            return border;
        }

        public TestTextParserConfig border(String border) {
            Validate.notEmpty(border);
            if (border.equals(separator()))
                throw new IllegalArgumentException("Border cannot match separator");
            this.border = border;
            return this;
        }


        public TestTextParserConfig separator(String separator) {
            Validate.notEmpty(border);
            if (separator.equals(border()))
                throw new IllegalArgumentException("Border cannot match separator");
            this.separator = separator;
            return this;
        }

        @Override
        public @NotNull TestTextParser build() {
            return new TestTextParser(this, null);
        }
    }
}
