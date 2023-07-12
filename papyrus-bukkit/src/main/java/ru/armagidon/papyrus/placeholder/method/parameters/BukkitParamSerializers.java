package ru.armagidon.papyrus.placeholder.method.parameters;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.armagidon.papyrus.implementation.placeholder.method.parameters.ParameterParseException;
import ru.armagidon.papyrus.implementation.placeholder.method.parameters.ParameterParseResult;
import ru.armagidon.papyrus.implementation.placeholder.method.parameters.ParameterTypeParser;

import java.util.Objects;
import java.util.Optional;

public class BukkitParamSerializers
{
    public static final ParameterTypeParser<Optional<Player>> PLAYER = (viewer, parameters) -> {
        String input = parameters.poll();
        if (input == null)
            return ParameterParseResult.fail(new ParameterParseException("Parameter was not given", null));

        return ParameterParseResult.success(Optional.ofNullable(Bukkit.getPlayerExact(Objects.requireNonNull(parameters.poll()))));
    };


    public static final ParameterTypeParser<Player> PLAYER_NOT_NULL = (viewer, parameters) -> {
        String input = parameters.poll();
        if (input == null)
            return ParameterParseResult.fail(new ParameterParseException("Parameter was not given", null));
        Player player = Bukkit.getPlayerExact(input);
        if (player == null)
            return ParameterParseResult.fail(new ParameterParseException("Player is offline", input));
        return ParameterParseResult.success(player);
    };

}
