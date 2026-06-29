package io.github.uttmangosteen.man10Essentials.invsee

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent

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

        swapWithTarget(viewer, gui, gui.storage, guiSlot, e)
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
        storage: InvseeStorage,
        guiSlot: Int,
        e: InventoryClickEvent,
    ) {
        val targetItem = storage.getItem(gui.type, guiSlot)?.clone()
        val cursorItem = viewer.itemOnCursor.clone().takeUnless { it.type.isAir }

        if (targetItem == null && cursorItem == null) return

        if (!storage.setItem(gui.type, guiSlot, cursorItem?.clone())) {
            sendRefreshMessage(viewer)
            return
        }

        if (!storage.save()) {
            storage.setItem(gui.type, guiSlot, targetItem?.clone())
            sendRefreshMessage(viewer)
            return
        }

        viewer.setItemOnCursor(targetItem?.clone())
        e.view.topInventory.setItem(guiSlot, cursorItem?.clone())

        storage.refresh()
        viewer.updateInventory()
        viewer.sendMessage("§aアイテムを交換しました")
    }

    private fun sendRefreshMessage(viewer: Player) {
        viewer.sendMessage("§c操作に失敗しました、インベントリを開きなおして更新してください")
    }
}