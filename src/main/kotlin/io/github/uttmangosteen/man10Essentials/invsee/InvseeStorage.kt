package io.github.uttmangosteen.man10Essentials.invsee

import org.bukkit.inventory.ItemStack

interface InvseeStorage {
    fun getContents(type: InvseeType): Array<ItemStack?>
    fun getItem(type: InvseeType, guiSlot: Int): ItemStack?
    fun setItem(type: InvseeType, guiSlot: Int, itemStack: ItemStack?): Boolean
    fun save(): Boolean
    fun refresh()
}