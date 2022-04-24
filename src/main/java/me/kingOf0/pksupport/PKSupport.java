package me.kingOf0.pksupport;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import me.clip.placeholderapi.PlaceholderAPI;
import me.kingOf0.pksupport.file.Config;
import me.kingOf0.pksupport.papi.expansion.AbilitySlot;
import org.bukkit.plugin.java.JavaPlugin;

public class PKSupport extends JavaPlugin {

    private static PKSupport instance;
    private Config config;

    @Override
    public void onEnable() {
        if (instance != null)
            throw new IllegalStateException("Plugin cannot be enabled twice!");
        instance = this;
        config = new Config();

        boolean debug = config.getBoolean("debug", false);
        if (!debug)
            getLogger().info("Logger is disabled by default. You can set 'debug' to 'true' in config to activate it.");

        Object hook = null;
        try {
            PlaceholderAPI.setPlaceholders(null, "");
            hook = new AbilitySlot().register();
        } catch (Throwable t) {
            if (debug) t.printStackTrace();
            getServer().getLogger().info("PlaceHolderAPI couldn't found. If you belive that this an error please enable debug.!");
        }

        try {
            SkriptAddon skriptAddon = Skript.registerAddon(this);
            hook = skriptAddon.loadClasses("me.kingOf0.pksupport.skript", "expansion");
        } catch (Throwable t) {
            if (debug) t.printStackTrace();
            getServer().getLogger().info("Skript couldn't found! If you belive that this an error please enable debug.");
        }
        if (hook == null) getServer().getLogger().warning("Couldn't found any hook! You have to use papi or skript!");
    }

    @Override
    public void onDisable() {

    }

    public static PKSupport getInstance() {
        return instance;
    }

    @Override
    public Config getConfig() {
        return config;
    }
}
