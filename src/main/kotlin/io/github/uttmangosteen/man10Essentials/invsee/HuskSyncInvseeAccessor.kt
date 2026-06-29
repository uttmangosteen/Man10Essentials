package io.github.uttmangosteen.man10Essentials.invsee

import java.util.UUID

interface HuskSyncInvseeAccessor {
    fun load(targetUuid: UUID, targetName: String): HuskSyncInvseeData?
    fun save(targetUuid: UUID, targetName: String, data: HuskSyncInvseeData): Boolean
}