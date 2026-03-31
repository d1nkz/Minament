package dinkz.minament.rei

import dinkz.minament.SkyBlockRepo
import io.github.moulberry.repo.data.NEUCraftingRecipe
import io.github.moulberry.repo.data.NEUForgeRecipe
import io.github.moulberry.repo.data.NEUIngredient
import io.github.moulberry.repo.data.NEUItem
import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry
import me.shedaniel.rei.api.common.entry.EntryIngredient
import me.shedaniel.rei.api.common.util.EntryIngredients
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.CustomData

class MinamentReiPlugin : REIClientPlugin {

    override fun registerEntries(registry: EntryRegistry) {
        val repo = SkyBlockRepo.neuRepo ?: return
        registry.removeEntryIf { true }
        repo.items?.items?.values?.forEach { neuItem ->
            registry.addEntry(EntryStacks.of(neuItem.toItemStack()))
        }
    }

    override fun registerDisplays(registry: DisplayRegistry) {
        val repo = SkyBlockRepo.neuRepo ?: return
        repo.items?.items?.values?.forEach { neuItem ->
            neuItem.recipes?.forEach { recipe ->
                when (recipe) {
                    is NEUCraftingRecipe -> registerCraftingRecipe(registry, neuItem, recipe)
                    is NEUForgeRecipe -> registerForgeRecipe(registry, neuItem, recipe)
                    else -> {}
                }
            }
        }
    }

    private fun registerCraftingRecipe(
        registry: DisplayRegistry,
        neuItem: NEUItem,
        recipe: NEUCraftingRecipe
    ) {
        try {
            val inputs = (0 until 9).map { slot ->
                val ingredient = recipe.inputs.getOrNull(slot)
                if (ingredient == null || ingredient.itemId == NEUIngredient.NEU_SENTINEL_EMPTY) {
                    EntryIngredient.empty()
                } else {
                    EntryIngredients.of(neuItemIdToStack(ingredient.itemId, ingredient.amount.toInt()))
                }
            }
            val outputAmount = recipe.output?.amount?.toInt() ?: 1
            val output = listOf(EntryIngredients.of(neuItem.toItemStack(outputAmount)))
            registry.add(SkyBlockCraftingDisplay(inputs, output))
        } catch (e: Exception) {
            // Skip malformed recipes
        }
    }

    private fun registerForgeRecipe(
        registry: DisplayRegistry,
        neuItem: NEUItem,
        recipe: NEUForgeRecipe
    ) {
        try {
            val inputs = recipe.inputs.map { ingredient ->
                EntryIngredients.of(neuItemIdToStack(ingredient.itemId, ingredient.amount.toInt()))
            }
            val output = listOf(EntryIngredients.of(neuItem.toItemStack()))
            registry.add(SkyBlockForgeDisplay(inputs, output))
        } catch (e: Exception) {
            // Skip malformed recipes
        }
    }

    private fun neuItemIdToStack(itemId: String, amount: Int = 1): ItemStack {
        if (itemId == NEUIngredient.NEU_SENTINEL_EMPTY) return ItemStack.EMPTY
        val repo = SkyBlockRepo.neuRepo ?: return ItemStack(Items.PAPER, amount)
        val neuItem = repo.items?.items?.get(itemId)
        return neuItem?.toItemStack(amount) ?: ItemStack(Items.PAPER, amount)
    }
}

fun NEUItem.toItemStack(amount: Int = 1): ItemStack {
    val material = materialToItem(this.minecraftItemId ?: "PAPER")
    val stack = ItemStack(material, amount.coerceIn(1, 64))
    stack.set(DataComponents.CUSTOM_NAME, Component.literal(this.displayName ?: this.skyblockItemId))
    val tag = CompoundTag()
    tag.putString("id", this.skyblockItemId)
    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag))
    return stack
}

fun materialToItem(material: String) = when (material.uppercase().split(":").last()) {
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
    "INK_SACK" -> Items.INK_SAC
    "SUGAR_CANE" -> Items.SUGAR_CANE
    "REDSTONE" -> Items.REDSTONE
    "GLOWSTONE_DUST" -> Items.GLOWSTONE_DUST
    "NETHER_STALK" -> Items.NETHER_WART
    "WOOD" -> Items.OAK_LOG
    "LOG" -> Items.OAK_LOG
    "WOOL" -> Items.WHITE_WOOL
    "STONE" -> Items.STONE
    "COBBLESTONE" -> Items.COBBLESTONE
    "IRON_ORE" -> Items.IRON_ORE
    "GOLD_ORE" -> Items.GOLD_ORE
    "COAL" -> Items.COAL
    "FEATHER" -> Items.FEATHER
    "ARROW" -> Items.ARROW
    "BONE" -> Items.BONE
    "STRING" -> Items.STRING
    "SLIME_BALL" -> Items.SLIME_BALL
    "BLAZE_ROD" -> Items.BLAZE_ROD
    "GHAST_TEAR" -> Items.GHAST_TEAR
    "SPIDER_EYE" -> Items.SPIDER_EYE
    "ENDER_PEARL" -> Items.ENDER_PEARL
    "BLAZE_POWDER" -> Items.BLAZE_POWDER
    "MAGMA_CREAM" -> Items.MAGMA_CREAM
    "BREWING_STAND" -> Items.BREWING_STAND
    "CAULDRON" -> Items.CAULDRON
    "EYE_OF_ENDER" -> Items.ENDER_EYE
    "GOLDEN_APPLE" -> Items.GOLDEN_APPLE
    "GOLDEN_CARROT" -> Items.GOLDEN_CARROT
    "BOOK" -> Items.BOOK
    "ENCHANTED_BOOK" -> Items.ENCHANTED_BOOK
    "APPLE" -> Items.APPLE
    "WHEAT" -> Items.WHEAT
    "CARROT" -> Items.CARROT
    "POTATO" -> Items.POTATO
    "PUMPKIN" -> Items.PUMPKIN
    "MELON" -> Items.MELON_SLICE
    "NETHER_BRICK" -> Items.NETHER_BRICK
    "QUARTZ" -> Items.QUARTZ
    "PRISMARINE_SHARD" -> Items.PRISMARINE_SHARD
    "PRISMARINE_CRYSTALS" -> Items.PRISMARINE_CRYSTALS
    "RABBIT_HIDE" -> Items.RABBIT_HIDE
    "CHORUS_FRUIT" -> Items.CHORUS_FRUIT
    "SHULKER_SHELL" -> Items.SHULKER_SHELL
    "NAUTILUS_SHELL" -> Items.NAUTILUS_SHELL
    "HEART_OF_THE_SEA" -> Items.HEART_OF_THE_SEA
    "PHANTOM_MEMBRANE" -> Items.PHANTOM_MEMBRANE
    "NETHERITE_INGOT" -> Items.NETHERITE_INGOT
    "CRYING_OBSIDIAN" -> Items.CRYING_OBSIDIAN
    "OBSIDIAN" -> Items.OBSIDIAN
    "LAPIS_LAZULI" -> Items.LAPIS_LAZULI
    "CLAY_BALL" -> Items.CLAY_BALL
    "BRICK" -> Items.BRICK
    else -> Items.PAPER
}