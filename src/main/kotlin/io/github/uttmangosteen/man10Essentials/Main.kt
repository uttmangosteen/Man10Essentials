package io.github.uttmangosteen.man10Essentials

import io.github.uttmangosteen.man10Essentials.ec.EcCommand
import io.github.uttmangosteen.man10Essentials.hat.HatCommand
import io.github.uttmangosteen.man10Essentials.hat.HatEvent
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    lateinit var pluginConfig: PluginConfig
        private set

    override fun onEnable() {
        saveDefaultConfig()
        pluginConfig = PluginConfig.load(config)

        registerEvents()
        registerCommands()
    }

    private fun registerEvents() {
        server.pluginManager.registerEvents(HatEvent(), this)
    }

    private fun registerCommands() {
        getCommand("ec")?.setExecutor(EcCommand())
        getCommand("hat")?.setExecutor(HatCommand())
    }
}
