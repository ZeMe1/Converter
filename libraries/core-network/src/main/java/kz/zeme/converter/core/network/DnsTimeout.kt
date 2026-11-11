package kz.zeme.converter.core.network

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.Dns
import java.net.InetAddress
import java.net.UnknownHostException

class DnsTimeout(
    private val timeoutMs: Long,
    private val delegate: Dns = Dns.SYSTEM,
) : Dns {

    override fun lookup(hostname: String): List<InetAddress> {
        val addresses = runBlocking {
            withTimeoutOrNull(timeoutMs) {
                delegate.lookup(hostname)
            }
        }

        return addresses ?: throw UnknownHostException("dns lookup timeout for $hostname")
    }
}
