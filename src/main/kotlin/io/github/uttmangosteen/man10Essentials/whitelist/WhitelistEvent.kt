package io.github.uttmangosteen.man10Essentials.whitelist

import io.github.uttmangosteen.man10Essentials.Main
import io.github.uttmangosteen.man10Essentials.PluginConfig
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.server.ServerCommandEvent

class WhitelistEvent(
    private val plugin: Main,
) : Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerCommand(e: PlayerCommandPreprocessEvent) {
        handleCommand(e.message.removePrefix("/"))
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onServerCommand(e: ServerCommandEvent) {
        handleCommand(e.command)
    }

    private fun handleCommand(command: String) {
        val args = command.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
        if (args.size < 2) return

        val commandName = args[0].lowercase()
        val subCommand = args[1].lowercase()

        if (commandName != "whitelist" && commandName != "minecraft:whitelist") return

        when (subCommand) {
            "on", "off" -> syncWhitelistStatusLater()
        }
    }

    private fun syncWhitelistStatusLater() {
        Bukkit.getScheduler().runTask(plugin, Runnable {
            val status = plugin.server.hasWhitelist()

            plugin.config.set("whitelist-after60sec.status", status)
            plugin.saveConfig()
            plugin.reloadConfig()
            plugin.pluginConfig = PluginConfig.load(plugin.config)
        })
    }
}