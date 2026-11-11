package kz.zeme.converter.core.env.props

import android.app.Application

const val BASE_URL_PROPERTY = "base_url_property"
const val FILES_URL_PROPERTY = "files_url_property"

object KoinProperties {

    fun getKoinProperties(
        application: Application,
    ): Map<String, String> = mapOf(
        BASE_URL_PROPERTY to BaseSettings.baseUrl,
        FILES_URL_PROPERTY to BaseSettings.filesUrl,
    )
}
