package world.respect.libutil.ext


/**
 * Get the last distinct item in a list by a given key. This could be useful when we have a list of
 * updates, but only want to apply the latest received item.
 */
inline fun <T, K> List<T>.lastDistinctBy(
    selector: (T) -> K
): List<T>{
    val map = mutableMapOf<K, T>()
    forEach {
        map[selector(it)] = it
    }
    return map.values.toList()
}
