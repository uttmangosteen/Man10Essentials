package io.github.uttmangosteen.man10Essentials.ec

import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class EcCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) return true
        if (!sender.hasPermission("man10essentials.ec")) return true
        sender.openInventory(sender.enderChest)
        sender.playSound(sender.location, Sound.BLOCK_ENDER_CHEST_OPEN, 1f, 1f)
        return true
    }
}