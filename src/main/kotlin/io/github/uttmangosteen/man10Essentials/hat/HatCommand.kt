package io.github.uttmangosteen.man10Essentials.hat


import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HatCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) return true
        if (!sender.hasPermission("man10essentials.hat")) return true

        val senderInv = sender.inventory

        val mainHandItem = senderInv.itemInMainHand
        val helmetItem = senderInv.helmet

        senderInv.helmet = mainHandItem
        senderInv.setItemInMainHand(helmetItem)

        sender.updateInventory()
        sender.sendMessage("§aメインハンドと頭のアイテムを交換")
        sender.playSound(sender.location, Sound.ITEM_ARMOR_EQUIP_GENERIC, 1f, 1f)
        return true
    }
}