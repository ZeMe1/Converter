package kz.zeme.converter.core.env.props

const val BASE_URL_PROPERTY = "base_url_property"

object KoinProperties {

    fun getKoinProperties(): Map<String, String> = mapOf(
        BASE_URL_PROPERTY to BaseSettings.baseUrl,
    )
}
