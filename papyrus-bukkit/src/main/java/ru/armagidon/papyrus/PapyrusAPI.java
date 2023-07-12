package ru.armagidon.papyrus;

import org.bukkit.entity.Player;
import ru.armagidon.papyrus.implementation.text.MultiSourceTextParserImpl;
import ru.armagidon.papyrus.implementation.text.TextParserImpl;
import ru.armagidon.papyrus.placeholder.method.parameters.BukkitParamSerializers;
import ru.armagidon.papyrus.implementation.placeholder.method.parameters.DefaultParamSerializers;
import ru.armagidon.papyrus.implementation.placeholder.method.parameters.ParameterParserCollection;
import ru.armagidon.papyrus.text.MultiSourceTextParser;

public class PapyrusAPI {


    public static final ParameterParserCollection DEFAULT_PARAM_SERIALIZERS =
            ParameterParserCollection.builder()
                    .appendAll(DefaultParamSerializers.defaults())
                    .append(Player.class, BukkitParamSerializers.PLAYER_NOT_NULL).build();


    private static final PapyrusAPI API = new PapyrusAPI();

    private final MultiSourceTextParser GLOBAL_PARSER = new MultiSourceTextParserImpl(TextParserImpl.config().build());



    private PapyrusAPI(){}

    public static PapyrusAPI getApi() {
        return API;
    }

    public MultiSourceTextParser getGlobalParser() {
        return GLOBAL_PARSER;
    }
}
