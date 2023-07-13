package ru.armagidon.papyrus;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;
import ru.armagidon.papyrus.commands.ParseCommand;
import ru.armagidon.papyrus.implementation.placeholder.method.parameters.DefaultParamSerializers;
import ru.armagidon.papyrus.implementation.placeholder.method.parameters.ParameterParserCollection;
import ru.armagidon.papyrus.placeholder.method.parameters.BukkitParamSerializers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

public class Papyrus extends JavaPlugin {

    private BukkitAudiences bukkitAudiences;


    public Papyrus() {
    }

    @Override
    public void onEnable() {
        injectBukkitParsers();
        if (bukkitAudiences == null)
            this.bukkitAudiences = BukkitAudiences.create(this);
        BukkitCommandHandler commandHandler = BukkitCommandHandler.create(this);
        commandHandler.register(new ParseCommand(bukkitAudiences));
    }

    @Override
    public void onDisable() {
        bukkitAudiences.close();
    }

    private void injectBukkitParsers() {
        try {
            Field DEFAULT_PARSERS_F = PapyrusAPI.class.getDeclaredField("DEFAULT_PARAM_SERIALIZERS");
            DEFAULT_PARSERS_F.setAccessible(true);
            ParameterParserCollection defaultParsers = ParameterParserCollection.builder()
                    .appendAll(DefaultParamSerializers.defaults())
                    .append(Player.class, BukkitParamSerializers.PLAYER_NOT_NULL)
                    .build();
            DEFAULT_PARSERS_F.set(null, defaultParsers);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            StringWriter writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            getLogger().severe(writer.toString());
        }
    }
}
