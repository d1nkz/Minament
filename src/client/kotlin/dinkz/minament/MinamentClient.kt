package dinkz.minament

import dinkz.minament.textures.CustomSkyBlockTextures
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object MinamentClient : ClientModInitializer {
	val logger: Logger = LoggerFactory.getLogger("Minament")

	override fun onInitializeClient() {
		ModelLoadingPlugin.register(CustomSkyBlockTextures)
		SkyBlockRepo.init()
	}
}