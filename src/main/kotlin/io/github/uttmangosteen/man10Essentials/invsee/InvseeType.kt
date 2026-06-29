package io.github.uttmangosteen.man10Essentials.invsee

enum class InvseeType(
    val id: String,
    val size: Int,
) {
    EC("ec", 27),
    INV("inv", 36),
    ARMOR("armor", 9);

    companion object {
        fun fromId(id: String): InvseeType? {
            return entries.firstOrNull { it.id == id }
        }
    }
}