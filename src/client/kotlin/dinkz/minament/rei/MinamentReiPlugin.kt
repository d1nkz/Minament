package dinkz.minament.rei

import dinkz.minament.SkyBlockRepo
import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class MinamentReiPlugin : REIClientPlugin {

    override fun registerEntries(registry: EntryRegistry) {
        SkyBlockRepo.items.forEach { sbItem ->
            val stack = createStack(sbItem)
            registry.addEntry(EntryStacks.of(stack))
        }
    }

    private fun createStack(sbItem: SkyBlockRepo.SkyBlockItem): ItemStack {
        val material = materialToItem(sbItem.material)
        val stack = ItemStack(material)

        // Set the display name
        stack.set(DataComponents.CUSTOM_NAME, Component.literal(sbItem.name))

        // Store the SkyBlock ID in custom data so texture pack support can read it
        val tag = CompoundTag()
        tag.putString("id", sbItem.id)
        stack.set(
            DataComponents.CUSTOM_DATA,
            net.minecraft.world.item.component.CustomData.of(tag)
        )

        return stack
    }

    private fun materialToItem(material: String) = when (material.uppercase()) {
        "DIAMOND_SWORD" -> Items.DIAMOND_SWORD
        "DIAMOND_PICKAXE" -> Items.DIAMOND_PICKAXE
        "DIAMOND_AXE" -> Items.DIAMOND_AXE
        "DIAMOND_SHOVEL" -> Items.DIAMOND_SHOVEL
        "DIAMOND_HOE" -> Items.DIAMOND_HOE
        "GOLD_INGOT" -> Items.GOLD_INGOT
        "IRON_INGOT" -> Items.IRON_INGOT
        "DIAMOND" -> Items.DIAMOND
        "EMERALD" -> Items.EMERALD
        "BOW" -> Items.BOW
        "FISHING_ROD" -> Items.FISHING_ROD
        "LEATHER_HELMET" -> Items.LEATHER_HELMET
        "LEATHER_CHESTPLATE" -> Items.LEATHER_CHESTPLATE
        "LEATHER_LEGGINGS" -> Items.LEATHER_LEGGINGS
        "LEATHER_BOOTS" -> Items.LEATHER_BOOTS
        "SKULL_ITEM" -> Items.PLAYER_HEAD
        "MUSHROOM_SOUP" -> Items.MUSHROOM_STEW
        "MONSTER_EGG" -> Items.BAT_SPAWN_EGG
        else -> Items.PAPER
    }
}