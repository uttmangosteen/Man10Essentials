package io.github.uttmangosteen.man10Essentials;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class EC implements CommandExecutor {
    private final JavaPlugin plugin;

    public EC(JavaPlugin plugin) {this.plugin = plugin;}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String alias, String[] args) {
        if (!(sender instanceof Player p)) return true;
        if (!sender.hasPermission("red.man10.ec")) return true;
        p.openInventory(p.getEnderChest());
        return true;
    }
}
