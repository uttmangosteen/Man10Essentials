package io.github.uttmangosteen.man10Essentials;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CheckNewPlayer implements Listener {
    private final JavaPlugin plugin;
    public CheckNewPlayer(JavaPlugin plugin) {this.plugin = plugin;}

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!plugin.getConfig().getBoolean("ohatsukit", false)) return;
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kit set " + player.getName() + " newbie");
            player.sendMessage("§e§l国王様より初期装備が下賜された！");
        }
    }
}
