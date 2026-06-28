package io.github.uttmangosteen.man10Essentials.newbiekit

import io.github.uttmangosteen.man10Essentials.Main
import io.github.uttmangosteen.man10Essentials.PluginConfig
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class NewbieKitCommand(
    private val plugin: Main,
) {
    fun execute(sender: CommandSender, args: Array<out String>) {
        when (args.size) {
            2 -> when (args[1].lowercase()) {
                "register" -> registerKit(sender)
            }

            3 -> when (args[1].lowercase()) {
                "give" -> giveKit(sender, args[2])
            }
        }
    }

    fun getTabCompletions(args: Array<out String>): List<String>? {
        return when (args.size) {
            2 -> listOf("register", "give").filter { it.startsWith(args[1]) }

            3 -> {
                if (args[1] == "give") null
                else emptyList()

            }

            else -> emptyList()
        }
    }

    private fun registerKit(sender: CommandSender) {
        if (sender !is Player) return
        val inventoryData = itemStackArrayToConfigList(sender.inventory.contents)

        plugin.config.set("newbiekit-give.inv", inventoryData)
        plugin.saveConfig()
        plugin.reloadConfig()
        plugin.pluginConfig = PluginConfig.load(plugin.config)

        sender.sendMessage("§a現在のインベントリを初心者キットとして保存しました")
    }

    private fun giveKit(sender: CommandSender, targetName: String) {
        val target = Bukkit.getPlayerExact(targetName)

        if (target == null) {
            sender.sendMessage("§c指定されたプレイヤーはオンラインではありません")
            return
        }

        val savedInventoryData = plugin.config.getList("newbiekit-give.inv")

        if (savedInventoryData.isNullOrEmpty()) {
            sender.sendMessage("§c初心者キットが登録されていません")
            return
        }

        val kitItems = runCatching {
            itemStackArrayFromConfigList(savedInventoryData)
        }.getOrElse {
            sender.sendMessage("§c初心者キットの読み込みに失敗しました")
            plugin.logger.warning("初心者キットの読み込みに失敗しました: ${it.message}")
            return
        }
        giveItems(target, kitItems)
        sender.sendMessage("§a${target.name} に初心者キットを配布しました")
        target.sendMessage("§a初心者キットを受け取りました")
    }

    private fun giveItems(target: Player, kitItems: Array<ItemStack?>) {
        val targetInventory = target.inventory
        val targetContents = targetInventory.contents
        for (i in kitItems.indices) {
            if (i >= targetContents.size) break
            val kitItem = kitItems[i] ?: continue
            if (kitItem.type == Material.AIR) continue
            val targetItem = targetContents[i]
            if (targetItem == null || targetItem.type == Material.AIR) {
                targetContents[i] = kitItem.clone()
                kitItems[i] = null
            }
        }

        targetInventory.contents = targetContents
        for (kitItem in kitItems) {
            if (kitItem == null || kitItem.type == Material.AIR) continue
            val leftovers = targetInventory.addItem(kitItem.clone())
            leftovers.values.forEach { leftover ->
                target.world.dropItemNaturally(target.location, leftover)
                target.sendMessage("§cインベントリに空きがなかったため、一部のアイテムを足元にドロップしました")
            }
        }

        target.updateInventory()
    }

    private fun itemStackArrayToConfigList(items: Array<ItemStack?>): List<Map<String, Any>?> {
        return items.map { item ->
            if (item == null || item.type == Material.AIR) null
            else item.clone().serialize()
        }
    }

    private fun itemStackArrayFromConfigList(configList: List<*>): Array<ItemStack?> {
        val items = arrayOfNulls<ItemStack>(configList.size)

        for (i in configList.indices) {
            val itemData = configList[i]

            if (itemData !is Map<*, *>) {
                items[i] = null
                continue
            }

            val serializedItem = itemData.entries.associate { (key, value) -> key.toString() to value }

            items[i] = ItemStack.deserialize(serializedItem)
        }

        return items
    }
}