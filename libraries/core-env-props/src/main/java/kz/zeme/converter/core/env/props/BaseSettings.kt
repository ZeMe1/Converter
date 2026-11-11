package kz.zeme.converter.core.env.props

import org.koin.core.component.KoinComponent

@Suppress("ConstPropertyName")
object BaseSettings {

    const val baseUrl: String = "https://api.example.com/"
    const val filesUrl: String = "https://files.example.com/"

    object Property : KoinComponent {
        val baseUrl: String = getProperty(BASE_URL_PROPERTY)
        val filesUrl: String = getProperty(FILES_URL_PROPERTY)
    }

    private fun KoinComponent.getProperty(key: String): String = getKoin().getProperty(key)!!
}
