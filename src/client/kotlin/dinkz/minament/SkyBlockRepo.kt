package dinkz.minament

import io.github.moulberry.repo.NEURepository
import io.github.moulberry.repo.data.NEUItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.minecraft.client.Minecraft
import java.nio.file.Path

object SkyBlockRepo {

    var neuRepo: NEURepository? = null
        private set

    var items: Collection<NEUItem> = emptyList()
        private set

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun getRepoDir(): Path {
        return Minecraft.getInstance().gameDirectory.toPath()
            .resolve("config")
            .resolve("minament")
            .resolve("repo")
    }

    fun init() {
        scope.launch {
            try {
                val repoDir = getRepoDir().toFile()
                repoDir.mkdirs()

                // Clone or update the NEU repo
                val repoUrl = "https://github.com/NotEnoughUpdates/NotEnoughUpdates-REPO.git"
                val gitDir = repoDir.resolve(".git")

                if (!gitDir.exists()) {
                    MinamentClient.logger.info("Cloning NEU repo...")
                    Runtime.getRuntime().exec(
                        arrayOf("git", "clone", "--depth=1", repoUrl, repoDir.absolutePath)
                    ).waitFor()
                } else {
                    MinamentClient.logger.info("Updating NEU repo...")
                    Runtime.getRuntime().exec(
                        arrayOf("git", "-C", repoDir.absolutePath, "pull")
                    ).waitFor()
                }

                val repo = NEURepository.of(repoDir.toPath())
                repo.reload()
                neuRepo = repo
                items = repo.items?.items?.values ?: emptyList()

                Minecraft.getInstance().execute {
                    MinamentClient.logger.info("Loaded ${items.size} NEU items")
                    try {
                        val entryRegistry = Class.forName("me.shedaniel.rei.api.client.registry.entry.EntryRegistry")
                        val getInstance = entryRegistry.getMethod("getInstance")
                        val instance = getInstance.invoke(null)
                        val refilter = entryRegistry.getMethod("refilter")
                        refilter.invoke(instance)
                    } catch (e: Exception) {
                        MinamentClient.logger.warn("REI reload failed: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                MinamentClient.logger.error("Failed to load NEU repo", e)
            }
        }
    }
}