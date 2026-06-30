package io.github.uttmangosteen.man10Essentials.invsee

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.util.UUID

class InvseeGui(
    val targetUuid: UUID,
    val targetName: String,
    val type: InvseeType,
    val storage: InvseeStorage,
) : InventoryHolder {
    private var inventory: Inventory? = null

    override fun getInventory(): Inventory {
        return inventory ?: throw IllegalStateException("Inventory is not created")
    }

    fun setInventory(inventory: Inventory) {
        this.inventory = inventory
    }
}