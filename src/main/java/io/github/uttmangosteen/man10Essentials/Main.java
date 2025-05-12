package io.github.uttmangosteen.man10Essentials;

import io.github.uttmangosteen.man10Essentials.Commands.EC_C;
import io.github.uttmangosteen.man10Essentials.Commands.MHat_C;
import io.github.uttmangosteen.man10Essentials.Commands.Ohatsukit_C;
import io.github.uttmangosteen.man10Essentials.Events.Mhat_E;
import io.github.uttmangosteen.man10Essentials.Events.Ohatsukit_E;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {
    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();

        Global.enabled_give_ohatsukit = getConfig().getBoolean("ohatsukit.mode", false);

        Objects.requireNonNull(getCommand("ec")).setExecutor(new EC_C());
        Objects.requireNonNull(getCommand("mhat")).setExecutor(new MHat_C());
        Objects.requireNonNull(getCommand("ohatsukit")).setExecutor(new Ohatsukit_C(this));

        Bukkit.getPluginManager().registerEvents(new Mhat_E(), this);
        getServer().getPluginManager().registerEvents(new Ohatsukit_E(), this);
    }
}
