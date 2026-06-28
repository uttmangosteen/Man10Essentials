package io.github.uttmangosteen.man10Essentials

import org.bukkit.configuration.file.FileConfiguration

data class PluginConfig(
    var prefix: String,
    var whitelist: Boolean,
    var newbiekit: Boolean,
    var newbiekitData: String,
    var opcheck: Boolean,
) {
    companion object {
        fun load(config: FileConfiguration): PluginConfig {
            return PluginConfig(
                prefix = config.getString("prefix") ?: "§f§l[§a§lM§d§lE§f§l] ",
                whitelist = config.getBoolean("whitelist", true),
                newbiekit = config.getBoolean("newbiekit", false),
                newbiekitData = config.getString("newbiekit-data") ?: "",
                opcheck = config.getBoolean("opcheck", false),
            )
        }
    }
}