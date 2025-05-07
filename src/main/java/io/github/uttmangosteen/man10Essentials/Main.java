package io.github.uttmangosteen.man10Essentials;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("ec")).setExecutor(new EC(this));
        Objects.requireNonNull(getCommand("ohatsukit")).setExecutor(new OhatsuKit(this));
    }
}
