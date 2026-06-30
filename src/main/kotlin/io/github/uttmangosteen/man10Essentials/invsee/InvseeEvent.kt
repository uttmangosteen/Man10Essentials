package io.github.uttmangosteen.man10Essentials.invsee

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemStack

class InvseeEvent : Listener {
    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        val viewer = e.whoClicked as? Player ?: return
        val gui = e.view.topInventory.holder as? InvseeGui ?: return

        if (e.clickedInventory != e.view.topInventory) {
            if (e.click.isShiftClick) e.isCancelled = true
            return
        }

        e.isCancelled = true

        if (e.click != ClickType.SHIFT_LEFT) return

        val guiSlot = e.rawSlot
        if (guiSlot < 0 || guiSlot >= e.view.topInventory.size) return
        if (gui.type == InvseeType.ARMOR && guiSlot > 4) return

        swapWithTarget(viewer, gui, guiSlot, e)
    }

    @EventHandler
    fun onDrag(e: InventoryDragEvent) {
        if (e.view.topInventory.holder !is InvseeGui) return

        val topSize = e.view.topInventory.size
        val dragToTop = e.rawSlots.any { it < topSize }
        if (!dragToTop) return

        e.isCancelled = true
    }

    private fun swapWithTarget(
        viewer: Player,
        gui: InvseeGui,
        guiSlot: Int,
        e: InventoryClickEvent,
    ) {
        val slotItem = e.view.topInventory.getItem(guiSlot)
        val actualItem = gui.storage.getItem(gui.type, guiSlot)
        val cursorItem = viewer.itemOnCursor.takeUnless { it.type.isAir }

        if (!isSameItem(slotItem, actualItem)) {
            e.view.topInventory.setItem(guiSlot, actualItem?.clone())
            gui.storage.refresh()
            viewer.updateInventory()
            viewer.sendMessage("§c対象のアイテムが更新されていたため、操作をキャンセルしました")
            return
        }

        if (actualItem == null && cursorItem == null) return

        if (!gui.storage.setItem(gui.type, guiSlot, cursorItem?.clone())) {
            viewer.sendMessage("§c操作に失敗しました")
            return
        }

        viewer.setItemOnCursor(actualItem?.clone())
        e.view.topInventory.setItem(guiSlot, cursorItem?.clone())

        gui.storage.refresh()
        viewer.updateInventory()
        viewer.sendMessage("§aアイテムを交換しました")
    }

    private fun isSameItem(
        first: ItemStack?,
        second: ItemStack?,
    ): Boolean {
        if (first == null || first.type.isAir) {
            return second == null || second.type.isAir
        }

        if (second == null || second.type.isAir) {
            return false
        }

        return first.isSimilar(second) && first.amount == second.amount
    }
}