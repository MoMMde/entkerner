package net.kerner.entkerner.io


import dev.schlaubi.envconf.getEnv
import net.kerner.entkerner.abstract.SystemFileURI
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.div

object FileUtils {
    object Linux {
        val USER by getEnv { user ->
            "/home/$user"
        }
        val userHome: Path
            get() = Path(USER)

        val config: Path
            get() = userHome / ".config"
    }

    object Windows {
        private val TEMP = getEnv("TEMP")
        init {
            println(TEMP)
        }
        //private val user = TEMP.split('/')[1]
        private val user = "mommd"
        //private val drive = TEMP.split(":")[0]
        private val drive = "C"
        // todo: !!!!
        val appData: Path
            get() = Path("$drive:\\Users\\$user\\AppData")
    }
}

val nullPath = Path("")
val nullPaths = object : SystemFileURI() {
    override val linux = nullPath
    override val windows = nullPath
    override val darwin = nullPath
}