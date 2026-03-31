package dinkz.minament.rei

import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.display.Display
import me.shedaniel.rei.api.common.display.DisplaySerializer
import me.shedaniel.rei.api.common.display.basic.BasicDisplay
import me.shedaniel.rei.api.common.entry.EntryIngredient

class SkyBlockCraftingDisplay(
    inputs: List<EntryIngredient>,
    outputs: List<EntryIngredient>
) : BasicDisplay(inputs, outputs) {
    override fun getCategoryIdentifier(): CategoryIdentifier<SkyBlockCraftingDisplay> {
        return CategoryIdentifier.of("minecraft", "plugins/crafting")
    }
    @Suppress("UNCHECKED_CAST")
    override fun getSerializer() = null as DisplaySerializer<SkyBlockCraftingDisplay>?
}