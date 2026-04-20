package dk.itu.moapd.x9.nalm

inline fun <reified T> T.tag(): String = T::class.java.simpleName