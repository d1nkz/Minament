package dinkz.minament.textures

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.minecraft.client.Minecraft
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack

object CustomSkyBlockTextures : ModelLoadingPlugin {

    const val NAMESPACE = "firmskyblock"

    fun getSkyBlockId(stack: ItemStack): String? {
        val customData = stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA)
            ?: return null
        val tag = customData.copyTag()
        val id = tag.getString("id")
        return if (id.isPresent && id.get().isNotBlank()) id.get() else null
    }

    fun getCustomModelId(skyBlockId: String): Identifier {
        return Identifier.fromNamespaceAndPath(
            NAMESPACE,
            skyBlockId.lowercase().replace(";", "__")
        )
    }

    override fun initialize(ctx: ModelLoadingPlugin.Context) {
        // Resource packs place models at assets/firmskyblock/models/item/<skyblock_id>.json
        // Minecraft will load them automatically when present
    }
}