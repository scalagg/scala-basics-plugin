package gg.scala.basics.plugin.settings.utils

import com.google.common.base.Preconditions

object SettingEnumInternal
{
    fun <T : Enum<T>> getFirstConstant(enumClass: Class<T>): T?
    {
        Preconditions.checkNotNull(enumClass, "enumClass")
        val constants = enumClass.enumConstants
        verifyConstantsNotEmpty(enumClass, constants)
        return constants[0]
    }

    fun <T : Enum<T>> getLastConstant(enumClass: Class<T>): T?
    {
        Preconditions.checkNotNull(enumClass, "enumClass")
        val constants = enumClass.enumConstants
        verifyConstantsNotEmpty(enumClass, constants)
        return constants[constants.size - 1]
    }

    fun <T : Enum<T>> getConstantIndex(constant: T): Int
    {
        Preconditions.checkNotNull(constant, "constant")
        val constants: Array<T> = constant.declaringJavaClass.enumConstants
        for (index in constants.indices)
        {
            if (constants[index] === constant)
            {
                return index
            }
        }
        throw IllegalStateException("could not find enum constant index of constant=$constant")
    }

    fun <T : Enum<T>> getNextConstant(enumClass: Class<T>, constant: T?): T?
    {
        Preconditions.checkNotNull(enumClass, "enumClass")
        val constants = enumClass.enumConstants
        verifyConstantsNotEmpty(enumClass, constants)
        if (constant == null)
        {
            return getFirstConstant(enumClass)
        }
        val index = getConstantIndex(constant)
        return if (index == constants.size - 1) null else constants[index + 1]
    }

    fun <T : Enum<T>> getPreviousConstant(enumClass: Class<T>, constant: T?): T?
    {
        Preconditions.checkNotNull(enumClass, "enumClass")
        val constants = enumClass.enumConstants
        verifyConstantsNotEmpty(enumClass, constants)
        if (constant == null)
        {
            return getLastConstant(enumClass)
        }
        val index = getConstantIndex(constant)
        return if (index == 0) null else constants[index - 1]
    }

    private fun verifyConstantsNotEmpty(enumClass: Class<out Enum<*>>, constants: Array<*>)
    {
        check(constants.isNotEmpty()) { "enumClass=" + enumClass.name + " has no constants" }
    }
}
