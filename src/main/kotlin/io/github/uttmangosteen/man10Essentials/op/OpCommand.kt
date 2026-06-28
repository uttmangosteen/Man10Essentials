package io.github.uttmangosteen.man10Essentials.op

import io.github.uttmangosteen.man10Essentials.Main
import io.github.uttmangosteen.man10Essentials.newbiekit.NewbieKitCommand
import io.github.uttmangosteen.man10Essentials.op.status.StatusCommand
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class OpCommand(
    plugin: Main,
) : CommandExecutor, TabCompleter {

    private val statusCommand = StatusCommand(plugin)
    private val newbieKitCommand = NewbieKitCommand(plugin)

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (!sender.hasPermission("man10essentials.op")) return true
        if (args.isEmpty()) return false

        when (args[0].lowercase()) {
            "status" -> statusCommand.execute(sender, args)
            "newbiekit" -> newbieKitCommand.execute(sender, args)
            // "invsee" ->
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String>? {
        if (!sender.hasPermission("man10essentials.op")) return emptyList()

        return when (args.size) {
            1 -> listOf(
                "status",
                "newbiekit",
                "invsee",
            ).filter { it.startsWith(args[0]) }

            else -> when (args[0]) {
                "status" -> statusCommand.getTabCompletions(args)
                "newbiekit" -> newbieKitCommand.getTabCompletions(args)
                else -> emptyList()
            }
        }
    }
}