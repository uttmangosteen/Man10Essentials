package io.github.uttmangosteen.man10Essentials.hat

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.EquipmentSlot

class HatEvent : Listener {
    @EventHandler
    fun onClickHead(e: InventoryClickEvent) {
        if (e.slotType != InventoryType.SlotType.ARMOR || e.rawSlot != 5) return
        val player = e.whoClicked
        val getItem = player.itemOnCursor.type
        if (getItem == Material.AIR || getItem.equipmentSlot == EquipmentSlot.HEAD) return
        if(!player.hasPermission("man10essentials.hat"))return

        e.isCancelled = true
        val cursorItem = player.itemOnCursor
        val headItem = player.inventory.helmet
        player.setItemOnCursor(headItem)
        player.inventory.helmet = cursorItem
    }
}