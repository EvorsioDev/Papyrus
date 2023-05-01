package ru.armagidon.papyrus.placeholder.params;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

public class BukkitParamSerializers
{
    public static final ParamSerializer<Player, Player> PLAYER = (viewer, params) ->
            Bukkit.getPlayerExact(Objects.requireNonNull(params.poll()));

}
