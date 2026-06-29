package io.github.uttmangosteen.man10Essentials.invsee

import io.github.uttmangosteen.man10Essentials.Main
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class InvseeCommand(
    private val plugin: Main,
    private val huskSyncAccessor: HuskSyncInvseeAccessor?,
) {
    fun execute(sender: CommandSender, args: Array<out String>) {
        if (sender !is Player) return
        if (args.size != 3) return

        val type = InvseeType.fromId(args[1]) ?: return
        val targetName = args[2]

        val onlineTarget = Bukkit.getPlayerExact(targetName)
        if (onlineTarget != null) {
            val storage = OnlinePlayerInvseeStorage(onlineTarget)
            openInvsee(
                viewer = sender,
                targetUuid = onlineTarget.uniqueId,
                targetName = onlineTarget.name,
                type = type,
                source = InvseeSource.ONLINE,
                storage = storage,
            )
            sender.playSound(sender.location, Sound.BLOCK_CHEST_OPEN, 1f, 1f)
            return
        }

        if (huskSyncAccessor == null) {
            sender.sendMessage("§c指定されたプレイヤーはオンラインではありません")
            sender.sendMessage("§cHuskSync が見つからないため、オフライン/別鯖ユーザーは開けません")
            return
        }

        sender.sendMessage("§eHuskSync から $targetName のデータを読み込んでいます...")

        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            val offlinePlayer = Bukkit.getOfflinePlayer(targetName)
            val targetUuid = offlinePlayer.uniqueId

            val storage = HuskSyncInvseeStorage(
                targetUuid = targetUuid,
                targetName = targetName,
                accessor = huskSyncAccessor,
            )

            if (!storage.load()) {
                Bukkit.getScheduler().runTask(plugin, Runnable {
                    sender.sendMessage("§cHuskSync から $targetName のデータを読み込めませんでした")
                })
                return@Runnable
            }

            Bukkit.getScheduler().runTask(plugin, Runnable {
                openInvsee(
                    viewer = sender,
                    targetUuid = targetUuid,
                    targetName = targetName,
                    type = type,
                    source = InvseeSource.HUSKSYNC_OFFLINE,
                    storage = storage,
                )
                sender.playSound(sender.location, Sound.BLOCK_CHEST_OPEN, 1f, 1f)
            })
        })
    }

    fun getTabCompletions(args: Array<out String>): List<String>? {
        return when (args.size) {
            2 -> InvseeType.entries.map { it.id }.filter { it.startsWith(args[1]) }
            3 -> null
            else -> emptyList()
        }
    }

    private fun openInvsee(
        viewer: Player,
        targetUuid: java.util.UUID,
        targetName: String,
        type: InvseeType,
        source: InvseeSource,
        storage: InvseeStorage,
    ) {
        val gui = InvseeGui(
            targetUuid = targetUuid,
            targetName = targetName,
            type = type,
            source = source,
            storage = storage,
        )
        val inventory = Bukkit.createInventory(
            gui,
            type.size,
            Component.text("Invsee ${type.id}:$targetName"),
        )
        gui.setInventory(inventory)

        val contents = storage.getContents(type)
        contents.forEachIndexed { index, item ->
            if (index >= inventory.size) return@forEachIndexed
            if (item == null || item.type.isAir) return@forEachIndexed
            inventory.setItem(index, item.clone())
        }

        viewer.openInventory(inventory)
    }
}