package io.github.uttmangosteen.man10Essentials.invsee

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class InvseeCommand {
    fun execute(sender: CommandSender, args: Array<out String>) {
        if (sender !is Player) return
        if (args.size != 3) return

        val type = InvseeType.fromId(args[1]) ?: return
        val targetName = args[2]

        val target = Bukkit.getPlayerExact(targetName)
        if (target == null) {
            sender.sendMessage("§c指定されたプレイヤーはオンラインではありません")
            return
        }

        openInvsee(
            viewer = sender,
            target = target,
            type = type,
            storage = OnlinePlayerInvseeStorage(target),
        )
        sender.playSound(sender.location, Sound.BLOCK_CHEST_OPEN, 1f, 1f)
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
        target: Player,
        type: InvseeType,
        storage: InvseeStorage,
    ) {
        val gui = InvseeGui(
            targetUuid = target.uniqueId,
            targetName = target.name,
            type = type,
            storage = storage,
        )
        val inventory = Bukkit.createInventory(
            gui,
            type.size,
            Component.text("Invsee ${type.id}:${target.name}"),
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