package dinkz.minament.rei

import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.display.Display
import me.shedaniel.rei.api.common.display.DisplaySerializer
import me.shedaniel.rei.api.common.display.basic.BasicDisplay
import me.shedaniel.rei.api.common.entry.EntryIngredient
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Items

class SkyBlockForgeDisplay(
    inputs: List<EntryIngredient>,
    outputs: List<EntryIngredient>
) : BasicDisplay(inputs, outputs) {

    override fun getCategoryIdentifier(): CategoryIdentifier<SkyBlockForgeDisplay> {
        return CategoryIdentifier.of("minament", "forge")
    }

    @Suppress("UNCHECKED_CAST")
    override fun getSerializer() = null as DisplaySerializer<SkyBlockForgeDisplay>?
}