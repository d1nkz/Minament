package dinkz.minament

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import net.minecraft.client.Minecraft
import java.net.URI

object SkyBlockRepo {

    data class SkyBlockItem(
        val id: String,
        val name: String,
        val material: String,
    )

    var items: List<SkyBlockItem> = emptyList()
        private set

    private val gson = Gson()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun init() {
        scope.launch {
            try {
                val url = URI("https://api.hypixel.net/v2/resources/skyblock/items").toURL()
                val text = url.readText()
                val json = gson.fromJson(text, JsonObject::class.java)
                val itemsArray = json.getAsJsonArray("items")
                items = itemsArray.map { el ->
                    val obj = el.asJsonObject
                    SkyBlockItem(
                        id = obj.get("id")?.asString ?: "",
                        name = obj.get("name")?.asString ?: "Unknown",
                        material = obj.get("material")?.asString ?: "PAPER",
                    )
                }.filter { it.id.isNotBlank() }
                Minecraft.getInstance().execute {
                    MinamentClient.logger.info("Loaded ${items.size} SkyBlock items")
                }
            } catch (e: Exception) {
                MinamentClient.logger.error("Failed to fetch SkyBlock items", e)
            }
        }
    }
}