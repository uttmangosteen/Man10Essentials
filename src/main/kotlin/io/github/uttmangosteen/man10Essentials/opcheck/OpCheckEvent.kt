package io.github.uttmangosteen.man10Essentials.opcheck

import io.github.uttmangosteen.man10Essentials.Main
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class OpCheckEvent(
    private val plugin: Main,
) : Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if (!plugin.pluginConfig.opcheck) return

        val player = e.player
        if (!player.isOp) return
        if (player.hasPermission("group.gm")) return

        player.isOp = false

        val message = "不正なOPを検知したため権限を剝奪しました 対象者:${player.name}"

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "report $message")
        plugin.logger.info(message)
    }
}