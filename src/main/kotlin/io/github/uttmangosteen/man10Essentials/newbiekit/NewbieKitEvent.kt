package io.github.uttmangosteen.man10Essentials.newbiekit

import net.william278.husksync.event.BukkitPreSyncEvent
import net.william278.husksync.event.BukkitSyncCompleteEvent
import io.github.uttmangosteen.man10Essentials.Main
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*

class NewbieKitEvent(val plugin: Main) : Listener {
    private val unNewbies = ArrayList<UUID?>()

    @EventHandler
    fun onPreSync(e: BukkitPreSyncEvent) {
        unNewbies.add(e.user.uuid)
    }

    @EventHandler
    fun onSyncComplete(e: BukkitSyncCompleteEvent) {
        val player: Player = Bukkit.getPlayer(e.user.uuid) ?: return
        if (!unNewbies.contains(player.uniqueId)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ohatsukit give " + player.name)
            Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                "rediseconomy:bal " + player.name + " vault give 5000"
            )
            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                player.sendMessage("§e§l国王様より初期装備が下賜された！")
                player.sendMessage("§e§lはじめてのログインです。電子マネー5000円と現金1500円をもらいました! /bank と入力すると電子マネーや銀行口座を確認したり、現金と交換できます。")
            }, 20L)
        }
        unNewbies.remove(player.uniqueId)
    }
}