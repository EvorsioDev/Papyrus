package ru.armagidon.papyrus.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitLegacyDelegatedTextParser extends LegacyDelegatedTextParser<Player>
{
    public BukkitLegacyDelegatedTextParser(TextParser<Player> backing) {
        super(backing);
    }

    @Override
    protected @NotNull String convert(Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }
}
