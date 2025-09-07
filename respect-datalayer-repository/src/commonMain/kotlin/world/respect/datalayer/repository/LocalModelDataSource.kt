package world.respect.datalayer.repository

/**
 * Represents a local datasource for a specific model e.g. Person, Class, etc.
 */
interface LocalModelDataSource<T : Any> {

    /**
     * updateLocalFromRemote is used to handle when new data has been received from the remote
     * data source. It is therefor NOT subject to permission checks
     *
     * @param list - specific list of model data to insert
     * @param forceOverwrite normally local data will only be updated if it is newer than what is
     *        stored locally. Sometimes (e.g. during conflict resolution) this may need overridden
     */
    suspend fun updateLocalFromRemote(
        list: List<T>,
        forceOverwrite: Boolean = false,
    )

}