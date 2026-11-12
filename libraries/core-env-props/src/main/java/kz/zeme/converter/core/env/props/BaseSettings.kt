package kz.zeme.converter.core.env.props

import org.koin.core.component.KoinComponent

@Suppress("ConstPropertyName")
object BaseSettings {

    const val baseUrl: String = "https://api.frankfurter.dev/v2/"

    object Property : KoinComponent {
        val baseUrl: String = getProperty(BASE_URL_PROPERTY)
    }

    private fun KoinComponent.getProperty(key: String): String = getKoin().getProperty(key)!!
}
