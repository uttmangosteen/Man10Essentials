package io.github.uttmangosteen.man10Essentials;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {
    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        Objects.requireNonNull(getCommand("ec")).setExecutor(new EC(this));
        Objects.requireNonNull(getCommand("ohatsukit")).setExecutor(new OhatsukitCommand(this));
        getServer().getPluginManager().registerEvents(new CheckNewPlayer(this), this);
    }
}
