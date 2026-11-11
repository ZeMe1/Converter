package kz.zeme.converter.core.di.extensions

import org.koin.core.definition.KoinDefinition
import org.koin.dsl.binds
import kotlin.reflect.KClass

inline fun <reified T> KoinDefinition<T>.binds(
    vararg classes: KClass<*>
): KoinDefinition<T> {
    binds(arrayOf(*classes))

    return this
}
