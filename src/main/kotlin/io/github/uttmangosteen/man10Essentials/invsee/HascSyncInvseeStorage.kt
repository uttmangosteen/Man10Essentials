package io.github.uttmangosteen.man10Essentials.invsee

import org.bukkit.inventory.ItemStack
import java.util.UUID

class HuskSyncInvseeStorage(
    private val targetUuid: UUID,
    private val targetName: String,
    private val accessor: HuskSyncInvseeAccessor,
) : InvseeStorage {
    private var inventoryContents: Array<ItemStack?> = arrayOfNulls(36)
    private var enderChestContents: Array<ItemStack?> = arrayOfNulls(27)

    private var helmet: ItemStack? = null
    private var chestplate: ItemStack? = null
    private var leggings: ItemStack? = null
    private var boots: ItemStack? = null
    private var offhand: ItemStack? = null

    fun load(): Boolean {
        val data = accessor.load(targetUuid, targetName) ?: return false

        inventoryContents = normalize(data.inventoryContents, 36)
        enderChestContents = normalize(data.enderChestContents, 27)

        helmet = data.helmet?.clone()
        chestplate = data.chestplate?.clone()
        leggings = data.leggings?.clone()
        boots = data.boots?.clone()
        offhand = data.offhand?.clone()

        return true
    }

    override fun getContents(type: InvseeType): Array<ItemStack?> {
        return when (type) {
            InvseeType.EC -> enderChestContents.map { it?.clone() }.toTypedArray()
            InvseeType.INV -> inventoryContents.map { it?.clone() }.toTypedArray()
            InvseeType.ARMOR -> arrayOf(
                helmet?.clone(),
                chestplate?.clone(),
                leggings?.clone(),
                boots?.clone(),
                offhand?.clone(),
                null,
                null,
                null,
                null,
            )
        }
    }

    override fun getItem(type: InvseeType, guiSlot: Int): ItemStack? {
        return when (type) {
            InvseeType.EC -> enderChestContents.getOrNull(guiSlot)
            InvseeType.INV -> inventoryContents.getOrNull(guiSlot)
            InvseeType.ARMOR -> getArmorItem(guiSlot)
        }
    }

    override fun setItem(type: InvseeType, guiSlot: Int, itemStack: ItemStack?): Boolean {
        return when (type) {
            InvseeType.EC -> {
                if (guiSlot !in enderChestContents.indices) return false
                enderChestContents[guiSlot] = itemStack?.clone()
                true
            }

            InvseeType.INV -> {
                if (guiSlot !in inventoryContents.indices) return false
                inventoryContents[guiSlot] = itemStack?.clone()
                true
            }

            InvseeType.ARMOR -> setArmorItem(guiSlot, itemStack)
        }
    }

    override fun save(): Boolean {
        return accessor.save(
            targetUuid = targetUuid,
            targetName = targetName,
            data = HuskSyncInvseeData(
                inventoryContents = inventoryContents.map { it?.clone() }.toTypedArray(),
                enderChestContents = enderChestContents.map { it?.clone() }.toTypedArray(),
                helmet = helmet?.clone(),
                chestplate = chestplate?.clone(),
                leggings = leggings?.clone(),
                boots = boots?.clone(),
                offhand = offhand?.clone(),
            ),
        )
    }

    override fun refresh() {
        // オフライン / 別鯖ユーザーなので Bukkit 側で updateInventory は不要
    }

    private fun getArmorItem(guiSlot: Int): ItemStack? {
        return when (guiSlot) {
            0 -> helmet
            1 -> chestplate
            2 -> leggings
            3 -> boots
            4 -> offhand
            else -> null
        }
    }

    private fun setArmorItem(guiSlot: Int, itemStack: ItemStack?): Boolean {
        when (guiSlot) {
            0 -> helmet = itemStack?.clone()
            1 -> chestplate = itemStack?.clone()
            2 -> leggings = itemStack?.clone()
            3 -> boots = itemStack?.clone()
            4 -> offhand = itemStack?.clone()
            else -> return false
        }

        return true
    }

    private fun normalize(
        source: Array<ItemStack?>,
        size: Int,
    ): Array<ItemStack?> {
        val result = arrayOfNulls<ItemStack>(size)

        for (i in result.indices) {
            result[i] = source.getOrNull(i)?.clone()
        }

        return result
    }
}