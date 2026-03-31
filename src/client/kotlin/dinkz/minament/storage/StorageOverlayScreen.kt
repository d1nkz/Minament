package dinkz.minament.storage

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class StorageOverlayScreen : Screen(Component.literal("Storage Overview")) {

    private val slotSize = 18
    private val padding = 4
    private val columns = 9
    private val pageHeight = slotSize * 3 + padding
    private var scrollOffset = 0

    override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        // Draw dark background manually instead of renderBackground
        graphics.fill(0, 0, width, height, 0xC0000000.toInt())

        super.render(graphics, mouseX, mouseY, delta)

        val startX = (width - (columns * slotSize)) / 2
        var startY = padding - scrollOffset

        // Draw title
        graphics.drawCenteredString(font, "Storage Overview", width / 2, 4, 0xFFFFFF)

        if (StorageCache.pages.isEmpty()) {
            graphics.drawCenteredString(
                font,
                "No storage data yet. Open your ender chest first.",
                width / 2,
                height / 2,
                0xFFFFFF
            )
            return
        }

        startY += 20

        StorageCache.pages.toSortedMap().forEach { (pageIndex, items) ->
            val label = if (pageIndex < 100) "Ender Chest Page ${pageIndex + 1}"
            else "Backpack Slot ${pageIndex - 100 + 1}"

            // Draw page label
            graphics.drawString(font, label, startX, startY, 0xFFFFFF)
            startY += 12

            // Draw slots
            items.forEachIndexed { slot, item ->
                val col = slot % columns
                val row = slot / columns
                val x = startX + col * slotSize
                val y = startY + row * slotSize

                // Draw slot background
                graphics.fill(x, y, x + slotSize - 1, y + slotSize - 1, 0xFF8B8B8B.toInt())
                graphics.fill(x + 1, y + 1, x + slotSize - 2, y + slotSize - 2, 0xFF373737.toInt())

                // Draw item
                if (!item.isEmpty) {
                    graphics.renderItem(item, x + 1, y + 1)
                    graphics.renderItemDecorations(font, item, x + 1, y + 1)
                }

                // Tooltip on hover - TODO: add in future update
            }

            val rows = (items.size + columns - 1) / columns
            startY += rows * slotSize + padding
        }
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        scrollOffset = (scrollOffset - (verticalAmount * 10).toInt()).coerceAtLeast(0)
        return true
    }

    override fun isPauseScreen() = false
}