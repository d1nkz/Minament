package dinkz.minament

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object MinamentClient : ClientModInitializer {
	val logger: Logger = LoggerFactory.getLogger("Minament")

	override fun onInitializeClient() {
		SkyBlockRepo.init()
	}
}