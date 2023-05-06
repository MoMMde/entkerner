package net.kerner.entkerner.extractors.chrome

import io.ktor.client.*
import net.kerner.entkerner.abstract.AbstractDataExtractor
import net.kerner.entkerner.abstract.SystemFileURI
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.file.Path
import kotlin.io.path.*

// https://docs.google.com/document/d/18wpuaitdvki6mO4aWkjvORgryUHM-mvDIqppDXhbSTk/edit#
abstract class ChromeLocalStorageExtractor(
    val httpClient: HttpClient
) : AbstractDataExtractor<List<String>>(httpClient) {

    fun readLocalStorage(leveldb: Path) {
        if (!leveldb.isDirectory())
            return

        val current = leveldb.resolve("CURRENT")
        if (!current.isRegularFile())
            return                                                              

        val manifest = leveldb.resolve("000005.ldb")
        println(manifest.pathString)
        val database = Database.connect("jdbc:sqlite:$manifest")

        transaction(database) {
            val tableNames = TransactionManager.current().db.dialect.allTablesNames()
            println(tableNames)
        }

    }

}