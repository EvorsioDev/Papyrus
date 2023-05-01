package ru.armagidon.papyrus;

import org.bukkit.entity.Player;
import ru.armagidon.papyrus.placeholder.BukkitPlaceholderContainer;
import ru.armagidon.papyrus.placeholder.handler.BukkitPlaceholderHandlerSweeper;
import ru.armagidon.papyrus.placeholder.params.DefaultParamSerializers;
import ru.armagidon.papyrus.placeholder.params.ParamSerializerCollection;
import ru.armagidon.papyrus.text.BukkitLegacyDelegatedTextParser;
import ru.armagidon.papyrus.text.BukkitTextParser;

import static ru.armagidon.papyrus.placeholder.params.BukkitParamSerializers.PLAYER;

public class PapyrusAPI {


    public static final ParamSerializerCollection<Player> DEFAULT_PARAM_SERIALIZERS =
            ParamSerializerCollection.<Player>builder()
                    .appendAll(DefaultParamSerializers.defaults())
                    .append(Player.class, PLAYER).build();


    private final BukkitPlaceholderContainer GLOBAL_PLACEHOLDER_CONTAINER = new BukkitPlaceholderContainer();

    private final BukkitTextParser GLOBAL_TEXT_PARSER = BukkitTextParser.configurator().build();

    private final BukkitPlaceholderHandlerSweeper GLOBAL_SWEEPER = new BukkitPlaceholderHandlerSweeper(DEFAULT_PARAM_SERIALIZERS);

    private final BukkitLegacyDelegatedTextParser GLOBAL_LEGACY_PARSER = new BukkitLegacyDelegatedTextParser(GLOBAL_TEXT_PARSER);


    private PapyrusAPI(){}

    private static final PapyrusAPI API = new PapyrusAPI();
    public static PapyrusAPI getApi() {
        return API;
    }

    public BukkitPlaceholderContainer getGlobalPlaceholderContainer() {
        return GLOBAL_PLACEHOLDER_CONTAINER;
    }

    public BukkitTextParser getGlobalTextParser() {
        return GLOBAL_TEXT_PARSER;
    }

    public BukkitPlaceholderHandlerSweeper getGlobalSweeper() {
        return GLOBAL_SWEEPER;
    }

    public BukkitLegacyDelegatedTextParser getGlobalLegacyParser() {
        return GLOBAL_LEGACY_PARSER;
    }
}
