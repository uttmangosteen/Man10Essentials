package io.github.uttmangosteen.man10Essentials

import io.github.uttmangosteen.man10Essentials.ec.EcCommand
import io.github.uttmangosteen.man10Essentials.hat.HatCommand
import io.github.uttmangosteen.man10Essentials.hat.HatEvent
import io.github.uttmangosteen.man10Essentials.invsee.HuskSyncInvseeAccessor
import io.github.uttmangosteen.man10Essentials.invsee.HuskSyncV3InvseeAccessor
import io.github.uttmangosteen.man10Essentials.invsee.InvseeEvent
import io.github.uttmangosteen.man10Essentials.newbiekit.NewbieKitEvent
import io.github.uttmangosteen.man10Essentials.op.OpCommand
import io.github.uttmangosteen.man10Essentials.opcheck.OpCheckEvent
import io.github.uttmangosteen.man10Essentials.whitelist.WhitelistEvent
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    lateinit var pluginConfig: PluginConfig
    private var huskSyncAccessor: HuskSyncInvseeAccessor? = null

    override fun onEnable() {
        startupProtectionByWhitelist()

        saveDefaultConfig()
        pluginConfig = PluginConfig.load(config)

        setupHuskSync()

        registerEvents()
        registerCommands()
    }

    private fun setupHuskSync() {
        if (server.pluginManager.getPlugin("HuskSync") == null) {
            logger.warning("HuskSync が見つかりません、HuskSync連携機能は動作しません")
            return
        }

        huskSyncAccessor = HuskSyncV3InvseeAccessor(this)
        logger.info("HuskSync 連携を有効化しました")
    }

    private fun registerEvents() {
        server.pluginManager.registerEvents(HatEvent(), this)
        server.pluginManager.registerEvents(WhitelistEvent(this), this)
        server.pluginManager.registerEvents(OpCheckEvent(this), this)
        server.pluginManager.registerEvents(InvseeEvent(), this)

        if (server.pluginManager.getPlugin("HuskSync") != null) {
            server.pluginManager.registerEvents(NewbieKitEvent(this), this)
        } else {
            logger.warning("HuskSync が見つかりません、初期キット配布機能は動作しません")
        }
    }

    private fun registerCommands() {
        getCommand("ec")?.setExecutor(EcCommand())

        getCommand("hat")?.setExecutor(HatCommand())

        val opCommand = OpCommand(this, huskSyncAccessor)
        getCommand("man10essentials")?.setExecutor(opCommand)
        getCommand("man10essentials")?.tabCompleter = opCommand
    }

    private fun startupProtectionByWhitelist() {
        server.setWhitelist(true)
        logger.info("whitelist を ON にしました")
        server.scheduler.runTaskLater(this, Runnable {
            val whitelist = PluginConfig.load(config).whitelist
            server.setWhitelist(whitelist)
            if (!whitelist) logger.info("whitelist を OFF にしました")
        }, 20L * 60L)
    }
}