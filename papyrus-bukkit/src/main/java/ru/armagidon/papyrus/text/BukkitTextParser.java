package ru.armagidon.papyrus.text;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.armagidon.papyrus.PapyrusAPI;
import ru.armagidon.papyrus.placeholder.PlaceholderContainer;

import java.util.List;

public class BukkitTextParser extends AbstractTextParser<Player>
{
    private BukkitTextParser(BukkitTextParserConfig config) {
        super(config);
    }

    @Override
    protected Component convertToComponent(String input) {
        return Component.text(input); // TODO move to mm
    }

    @Override
    protected PlaceholderContainer<Player> getGlobalContainer() {
        return PapyrusAPI.getApi().getGlobalPlaceholderContainer();
    }

    @Override
    protected ReplacementContext<Player> replacementContext(Player viewer, List<?> params, String rawParams) {
        return new ReplacementContext<>() {
            @Override
            public @Nullable Player getPlayer() {
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

    public static BukkitTextParserConfig configurator() {
        return new BukkitTextParserConfig();
    }

    public static final class BukkitTextParserConfig implements Config<Player, BukkitTextParser> {

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

        public BukkitTextParserConfig border(String border) {
            Validate.notEmpty(border);
            if (border.equals(separator()))
                throw new IllegalArgumentException("Border cannot match separator");
            this.border = border;
            return this;
        }


        public BukkitTextParserConfig separator(String separator) {
            Validate.notEmpty(border);
            if (separator.equals(border()))
                throw new IllegalArgumentException("Border cannot match separator");
            this.separator = separator;
            return this;
        }

        @Override
        public @NotNull BukkitTextParser build() {
            return new BukkitTextParser(this);
        }
    }
}
