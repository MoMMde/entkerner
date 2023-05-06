package net.kerner.entkerner.io

object JavaClassLoaderResources {
    fun getResource(file: String) = this::class.java.classLoader.getResource(file)
}