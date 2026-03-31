package dinkz.minament.storage

import net.minecraft.world.item.ItemStack

object StorageCache {
    // Maps page index to list of item stacks
    val pages: MutableMap<Int, List<ItemStack>> = mutableMapOf()

    fun storePage(index: Int, items: List<ItemStack>) {
        pages[index] = items.map { it.copy() }
    }

    fun clear() {
        pages.clear()
    }
}