package ru.armagidon.papyrus.commands;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.BukkitCommandActor;
import ru.armagidon.papyrus.PapyrusAPI;
import ru.armagidon.papyrus.implementation.placeholder.SimplePlaceholderContext;

public class ParseCommand {


    private final BukkitAudiences bukkitAudiences;

    public ParseCommand(BukkitAudiences bukkitAudiences) {
        this.bukkitAudiences = bukkitAudiences;
    }

    @Command("papyrus parse")
    public void onParse(BukkitCommandActor commandActor, Player as, String input) {
        PapyrusAPI.getApi().getGlobalParser().parse(SimplePlaceholderContext.builder().viewer(as).build(),
                Component.text(input)).thenAccept(component ->
                bukkitAudiences.sender(commandActor.getSender()).sendMessage(component));
    }
}
