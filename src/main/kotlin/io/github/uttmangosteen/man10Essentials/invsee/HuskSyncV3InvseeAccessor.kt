package io.github.uttmangosteen.man10Essentials.invsee

import io.github.uttmangosteen.man10Essentials.Main
import org.bukkit.inventory.ItemStack
import java.util.UUID

class HuskSyncV3InvseeAccessor(
    private val plugin: Main,
) : HuskSyncInvseeAccessor {
    override fun load(targetUuid: UUID, targetName: String): HuskSyncInvseeData? {
        /*
         * TODO:
         * ここに HuskSync API v3 で、
         *
         * 1. UUID / 名前から HuskSync User を取得
         * 2. 最新の DataSnapshot を取得
         * 3. Inventory data を Bukkit ItemStack 配列に変換
         * 4. EnderChest data を Bukkit ItemStack 配列に変換
         * 5. armor / offhand を取り出す
         *
         * という処理を書きます。
         *
         * HuskSync API はバージョンによってメソッド名が変わる可能性があるので、
         * ここだけ導入バージョンに合わせて調整してください。
         */

        plugin.logger.warning("HuskSyncInvseeAccessor#load はまだ HuskSync API に接続されていません: $targetName")
        return null
    }

    override fun save(targetUuid: UUID, targetName: String, data: HuskSyncInvseeData): Boolean {
        /*
         * TODO:
         * ここに HuskSync API v3 で、
         *
         * 1. 対象ユーザーの最新 DataSnapshot を取得
         * 2. Inventory data を data.inventoryContents で差し替え
         * 3. EnderChest data を data.enderChestContents で差し替え
         * 4. armor / offhand を差し替え
         * 5. 新しい snapshot として保存 / 更新
         *
         * という処理を書きます。
         */

        plugin.logger.warning("HuskSyncInvseeAccessor#save はまだ HuskSync API に接続されていません: $targetName")
        return false
    }

    private fun normalize(
        source: Array<ItemStack?>?,
        size: Int,
    ): Array<ItemStack?> {
        val result = arrayOfNulls<ItemStack>(size)
        if (source == null) return result

        for (i in result.indices) {
            result[i] = source.getOrNull(i)?.clone()
        }

        return result
    }
}