package ru.armagidon.papyrus;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;
import ru.armagidon.papyrus.commands.ParseCommand;

public class Papyrus extends JavaPlugin {

    private BukkitAudiences bukkitAudiences;


    public Papyrus() {
    }

    @Override
    public void onEnable() {
        if (bukkitAudiences == null)
            this.bukkitAudiences = BukkitAudiences.create(this);
        BukkitCommandHandler commandHandler = BukkitCommandHandler.create(this);
        commandHandler.register(new ParseCommand(bukkitAudiences));
    }

    @Override
    public void onDisable() {
        bukkitAudiences.close();
    }
}
