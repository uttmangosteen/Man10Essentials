package io.github.uttmangosteen.man10Essentials.invsee

import org.bukkit.inventory.ItemStack

data class HuskSyncInvseeData(
    val inventoryContents: Array<ItemStack?>,
    val enderChestContents: Array<ItemStack?>,
    val helmet: ItemStack?,
    val chestplate: ItemStack?,
    val leggings: ItemStack?,
    val boots: ItemStack?,
    val offhand: ItemStack?,
)