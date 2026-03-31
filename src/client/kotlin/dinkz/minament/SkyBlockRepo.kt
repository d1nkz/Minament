package dinkz.minament

import io.github.moulberry.repo.NEURepository
import io.github.moulberry.repo.data.NEUItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.minecraft.client.Minecraft
import java.io.FileOutputStream
import java.net.URI
import java.nio.file.Path
import java.util.zip.ZipInputStream

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
                val itemsDir = repoDir.resolve("items")

                if (!itemsDir.exists() || itemsDir.listFiles().isNullOrEmpty()) {
                    MinamentClient.logger.info("Downloading NEU repo...")
                    repoDir.mkdirs()

                    val zipUrl = URI("https://github.com/NotEnoughUpdates/NotEnoughUpdates-REPO/archive/refs/heads/master.zip").toURL()
                    val connection = zipUrl.openConnection()
                    connection.connectTimeout = 15000
                    connection.readTimeout = 60000

                    ZipInputStream(connection.getInputStream()).use { zip ->
                        var entry = zip.nextEntry
                        while (entry != null) {
                            if (!entry.isDirectory) {
                                val name = entry.name
                                // Strip the top level folder name
                                val relativePath = name.substringAfter("/")
                                if (relativePath.isNotBlank()) {
                                    val outFile = repoDir.resolve(relativePath)
                                    outFile.parentFile.mkdirs()
                                    FileOutputStream(outFile).use { out ->
                                        zip.copyTo(out)
                                    }
                                }
                            }
                            zip.closeEntry()
                            entry = zip.nextEntry
                        }
                    }
                    MinamentClient.logger.info("NEU repo downloaded successfully")
                } else {
                    MinamentClient.logger.info("NEU repo already exists, loading...")
                }

                val repo = NEURepository.of(repoDir.toPath())
                repo.reload()
                neuRepo = repo
                items = repo.items?.items?.values ?: emptyList()

                MinamentClient.logger.info("Loaded ${items.size} NEU items")

                Minecraft.getInstance().execute {
                    try {
                        val entryRegistry = Class.forName("me.shedaniel.rei.api.client.registry.entry.EntryRegistry")
                        val getInstance = entryRegistry.getMethod("getInstance")
                        val instance = getInstance.invoke(null)
                        val refilter = entryRegistry.getMethod("refilter")
                        refilter.invoke(instance)
                        MinamentClient.logger.info("REI reload triggered")
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