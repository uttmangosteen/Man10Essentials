package io.github.uttmangosteen.man10Essentials;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class OhatsukitCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public OhatsukitCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("red.man10.ohatsukit")) return true;
        if (args.length != 1) return false;
        if (args[0].equalsIgnoreCase("on")) {
            plugin.getConfig().set("ohatsukit", true);
            plugin.saveConfig();
            sender.sendMessage("初期装備を配布します");
        } else if (args[0].equalsIgnoreCase("off")) {
            plugin.getConfig().set("ohatsukit", false);
            plugin.saveConfig();
            sender.sendMessage("初期装備は配られません");
        }
        return true;
    }
}
