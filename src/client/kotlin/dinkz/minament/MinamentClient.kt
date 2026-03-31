package dinkz.minament

import dinkz.minament.storage.StorageListener
import dinkz.minament.storage.StorageOverlayScreen
import dinkz.minament.textures.CustomSkyBlockTextures
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object MinamentClient : ClientModInitializer {
	val logger: Logger = LoggerFactory.getLogger("Minament")

	override fun onInitializeClient() {
		ModelLoadingPlugin.register(CustomSkyBlockTextures)
		SkyBlockRepo.init()
		StorageListener.init()

		// /minament storage - opens the storage overlay
		ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
			dispatcher.register(
				literal("minament").then(
					literal("storage").executes { ctx ->
						val client = net.minecraft.client.Minecraft.getInstance()
						client.execute { client.setScreen(StorageOverlayScreen()) }
						1
					}
				)
			)
		}
	}
}