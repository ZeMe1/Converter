package kz.zeme.converter.core.network

import com.google.gson.annotations.SerializedName
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class EnumRetrofitConverterFactory : Converter.Factory() {

    companion object {
        fun <E : Enum<*>> E.getSerializedNameOrNull(): String? = try {
            this.javaClass
                .getField(this.name)
                .getAnnotation(SerializedName::class.java)
                ?.value
        } catch (exception: Exception) {
            null
        }
    }

    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<*, String>? {
        if (type is Class<*> && type.isEnum) return enumRetrofitConverter()
        return null
    }

    private fun enumRetrofitConverter() = Converter<Any?, String> { value ->
        (value as? Enum<*>)?.getSerializedNameOrNull()
    }
}
