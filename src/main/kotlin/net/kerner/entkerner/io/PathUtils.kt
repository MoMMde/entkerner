package net.kerner.entkerner.io


import dev.schlaubi.envconf.getEnv
import net.kerner.entkerner.abstract.SystemFileURI
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.div

object FileUtils {
    object Linux {
        private val USER by getEnv(default = "~") { user ->
            if (user == "~") "~"
            else "/home/$user"
        }
        val SystemFileURI.userHome: Path
            get() = Path(USER)
        val SystemFileURI.config: Path
            get() = userHome / ".config"
    }
}

val nullPath = Path("")
val nullPaths = object : SystemFileURI() {
    override val linux = nullPath
    override val windows = nullPath
    override val xnu = nullPath
}