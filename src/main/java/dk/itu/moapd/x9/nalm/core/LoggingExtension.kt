package dk.itu.moapd.x9.nalm.core

inline fun <reified T> T.tag(): String = T::class.java.simpleName