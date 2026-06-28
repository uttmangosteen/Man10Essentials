package io.github.uttmangosteen.man10Essentials

import org.bukkit.configuration.file.FileConfiguration

data class PluginConfig(
    var whitelist: Boolean,
    var newbiekit: Boolean,
    var opcheck: Boolean,
) {
    companion object {
        fun load(config: FileConfiguration): PluginConfig {
            return PluginConfig(
                whitelist = config.getBoolean("whitelist-after60sec.status", true),
                newbiekit = config.getBoolean("newbiekit-give.status", false),
                opcheck = config.getBoolean("opcheck-byluckperms.status", false),
            )
        }
    }
}