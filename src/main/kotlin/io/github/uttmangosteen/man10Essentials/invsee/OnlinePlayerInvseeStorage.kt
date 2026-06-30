package io.github.uttmangosteen.man10Essentials.invsee

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class OnlinePlayerInvseeStorage(
    private val target: Player,
) : InvseeStorage {
    override fun getContents(type: InvseeType): Array<ItemStack?> {
        return when (type) {
            InvseeType.EC -> target.enderChest.contents.map { it?.clone() }.toTypedArray()
            InvseeType.INV -> target.inventory.storageContents.map { it?.clone() }.toTypedArray()
            InvseeType.ARMOR -> arrayOf(
                target.inventory.helmet?.clone(),
                target.inventory.chestplate?.clone(),
                target.inventory.leggings?.clone(),
                target.inventory.boots?.clone(),
                target.inventory.itemInOffHand.clone(),
                null,
                null,
                null,
                null,
            )
        }
    }

    override fun getItem(type: InvseeType, guiSlot: Int): ItemStack? {
        return when (type) {
            InvseeType.EC -> {
                if (guiSlot !in target.enderChest.contents.indices) return null
                target.enderChest.getItem(guiSlot)
            }

            InvseeType.INV -> {
                target.inventory.storageContents.getOrNull(guiSlot)
            }

            InvseeType.ARMOR -> getArmorItem(guiSlot)
        }
    }

    override fun setItem(type: InvseeType, guiSlot: Int, itemStack: ItemStack?): Boolean {
        return when (type) {
            InvseeType.EC -> {
                if (guiSlot !in target.enderChest.contents.indices) return false
                target.enderChest.setItem(guiSlot, itemStack)
                true
            }

            InvseeType.INV -> {
                val contents = target.inventory.storageContents
                if (guiSlot !in contents.indices) return false

                contents[guiSlot] = itemStack
                target.inventory.storageContents = contents
                true
            }

            InvseeType.ARMOR -> setArmorItem(guiSlot, itemStack)
        }
    }

    override fun refresh() {
        target.updateInventory()
    }

    private fun getArmorItem(guiSlot: Int): ItemStack? {
        return when (guiSlot) {
            0 -> target.inventory.helmet
            1 -> target.inventory.chestplate
            2 -> target.inventory.leggings
            3 -> target.inventory.boots
            4 -> target.inventory.itemInOffHand
            else -> null
        }
    }

    private fun setArmorItem(guiSlot: Int, itemStack: ItemStack?): Boolean {
        when (guiSlot) {
            0 -> target.inventory.helmet = itemStack
            1 -> target.inventory.chestplate = itemStack
            2 -> target.inventory.leggings = itemStack
            3 -> target.inventory.boots = itemStack
            4 -> target.inventory.setItemInOffHand(itemStack)
            else -> return false
        }

        return true
    }
}