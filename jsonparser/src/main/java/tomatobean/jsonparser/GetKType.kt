package tomatobean.jsonparser

import java.lang.reflect.*
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.KVariance
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure


open class TypeToken<T> {
    val rawType: KType? = try {
        this::class.supertypes.find { it.jvmErasure.isSubclassOf(TypeToken::class) }?.arguments?.get(0)?.type?: getKTypeImpl()
    } catch (e: Exception) {
        try {
            getKTypeImpl()
        } catch (e: Exception) {
            this::class.starProjectedType
        }
    }
}

fun KClass<*>.getKTypeImpl(): KType = java.genericSuperclass.toKType().arguments.single().type!!

fun TypeToken<*>.getKTypeImpl(): KType =
        javaClass.genericSuperclass.toKType().arguments.single().type!!

fun Type.toKType(): KType = toKTypeProjection().type!!

fun Type.toKTypeProjection(): KTypeProjection = when (this) {
    is Class<*> -> this.kotlin.toInvariantFlexibleProjection(if(this.isArray) listOf(this.componentType.toKTypeProjection()) else emptyList())
    is ParameterizedType -> {
        val erasure = (rawType as Class<*>).kotlin
        erasure.toInvariantFlexibleProjection((erasure.typeParameters.zip(actualTypeArguments).map { (parameter, argument) ->
            val projection = argument.toKTypeProjection()
            projection.takeIf {
                // Get rid of use-site projections on arguments, where the corresponding parameters already have a declaration-site projection
                parameter.variance == KVariance.INVARIANT || parameter.variance != projection.variance
            } ?: KTypeProjection.invariant(projection.type!!)
        }))
    }
    is WildcardType -> when {
        lowerBounds.isNotEmpty() -> KTypeProjection.contravariant(lowerBounds.single().toKType())
        upperBounds.isNotEmpty() -> KTypeProjection.covariant(upperBounds.single().toKType())
    // This looks impossible to obtain through Java reflection API, but someone may construct and pass such an instance here anyway
        else -> KTypeProjection.STAR
    }
    is GenericArrayType -> Array<Any>::class.toInvariantFlexibleProjection(listOf(genericComponentType.toKTypeProjection()))
    is TypeVariable<*> -> throw TypeNotSupportException(this)
    else -> throw TypeNotSupportException(this)
}


fun KClass<*>.toInvariantFlexibleProjection(arguments: List<KTypeProjection> = emptyList()): KTypeProjection {
    return KTypeProjection.invariant(createType(arguments, nullable = false))
}