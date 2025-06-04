package io.github.uttmangosteen.man10Essentials.Events;

import io.github.uttmangosteen.man10Essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class CheckOP_E implements Listener {
    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        if(!p.isOp() || p.hasPermission("group.gm")) return;
        p.setOp(false);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "report 不正なOPを検知したため権限を剝奪しました 対象者:" + p.getName());
        Bukkit.getLogger().info("不正なOPを検知したため権限を剝奪しました 対象者:" + p.getName());
    }
}
