package ca.landonjw.util

import java.lang.reflect.Field

object ReflectionUtils {

    fun <T> getPrivateField(instance: Any, fieldName: String): T? {
        return try {
            val field: Field = instance.javaClass.getDeclaredField(fieldName)
            field.isAccessible = true
            @Suppress("UNCHECKED_CAST")
            field.get(instance) as T
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun <T> setPrivateField(instance: Any, fieldName: String, value: T) {
        try {
            val field: Field = instance.javaClass.getDeclaredField(fieldName)
            field.isAccessible = true
            field.set(instance, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}