package io.github.uttmangosteen.man10Essentials.op.status

import io.github.uttmangosteen.man10Essentials.Main
import io.github.uttmangosteen.man10Essentials.PluginConfig
import org.bukkit.command.CommandSender
import org.bukkit.configuration.ConfigurationSection

class StatusCommand(
    private val plugin: Main,
) {
    fun execute(sender: CommandSender, args: Array<out String>) {
        when (args.size) {
            1 -> sendStatusList(sender)
            3 -> {
                val statusName = args[1]
                val valueText = args[2]
                if (valueText != "true" && valueText != "false") return
                setStatus(sender, statusName, valueText.toBooleanStrict())
            }
        }
    }

    fun getTabCompletions(args: Array<out String>): List<String> {
        return when (args.size) {
            2 -> getStatusNames().filter { it.startsWith(args[1]) }
            3 -> listOf("true", "false").filter { it.startsWith(args[2]) }
            else -> emptyList()
        }
    }

    private fun sendStatusList(sender: CommandSender) {
        val statusNames = getStatusNames()
        if (statusNames.isEmpty()) return

        sender.sendMessage("§6========== Man10Essentials Status ==========")
        statusNames.forEach { name ->
            val status = plugin.config.getBoolean("$name.status")
            val statusColor = if (status) "§a" else "§c"
            sender.sendMessage("§e$name§7: $statusColor$status")
        }
        sender.sendMessage("§6===========================================")
    }

    private fun setStatus(sender: CommandSender, statusName: String, value: Boolean) {
        val section = plugin.config.getConfigurationSection(statusName)

        if (section == null || !section.contains("status")) return
        plugin.config.set("$statusName.status", value)
        plugin.saveConfig()
        plugin.reloadConfig()
        plugin.pluginConfig = PluginConfig.load(plugin.config)
        sender.sendMessage("§a$statusName.status を §e$value §aに変更しました")
    }

    private fun getStatusNames(): List<String> {
        return plugin.config.getKeys(false).filter { key ->
                val section: ConfigurationSection? = plugin.config.getConfigurationSection(key)
                section != null && section.contains("status") && section.isBoolean("status")
        }
    }
}