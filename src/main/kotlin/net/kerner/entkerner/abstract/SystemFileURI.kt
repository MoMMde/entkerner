package net.kerner.entkerner.abstract

import net.kerner.entkerner.model.SystemType
import java.nio.file.Path


abstract class SystemFileURI {
    var system = SystemType.WINDOWS
    abstract val linux:   Path
    abstract val windows: Path
    abstract val darwin:  Path
}
