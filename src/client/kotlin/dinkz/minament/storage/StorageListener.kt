package dinkz.minament.storage

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.world.inventory.ChestMenu

object StorageListener {

    fun init() {
        ScreenEvents.AFTER_INIT.register { client, screen, scaledWidth, scaledHeight ->
            if (screen !is AbstractContainerScreen<*>) return@register
            val menu = screen.menu
            if (menu !is ChestMenu) return@register

            // Check if this is a SkyBlock storage container by title
            val title = screen.title.string
            val pageIndex = detectStoragePage(title) ?: return@register

            // Cache the contents after the screen is fully initialized
            ScreenEvents.afterTick(screen).register { s ->
                val items = (0 until menu.container.containerSize).map { slot ->
                    menu.container.getItem(slot)
                }
                StorageCache.storePage(pageIndex, items)
            }
        }
    }

    private fun detectStoragePage(title: String): Int? {
        // Ender chest pages: "Ender Chest (Page 1/9)" etc
        val enderChestRegex = Regex("Ender Chest \\(Page (\\d+)/\\d+\\)")
        enderChestRegex.find(title)?.let {
            return it.groupValues[1].toIntOrNull()?.minus(1)
        }

        // Backpack pages: "Backpack (Slot 1)" etc
        val backpackRegex = Regex(".*Backpack.*\\((Slot|Page) (\\d+)\\)")
        backpackRegex.find(title)?.let {
            return it.groupValues[2].toIntOrNull()?.plus(100)
        }

        return null
    }
}