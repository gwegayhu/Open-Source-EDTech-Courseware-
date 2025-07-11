### RESPECT-Datasource

The designed is based primarily on the [Android offline-first data layer architecture](https://developer.android.com/topic/architecture/data-layer/offline-first).

A data source implementation can be:
 * Local - e.g. a database
 * Network - using HTTP over a REST API
 * Repository - an offline-first combination of local and network data sources.

Each data source is defined as an interface (e.g. OpdsDataSource).

The ViewModel sees only the interface, and does not need to be concerned with the underlying 
implementation.

This helps maximize code reusage:
 * Desktop/mobile apps: uses the repository implementation as the datasource
 * Browser app: uses the network implementation on its own as the datasource (no local database)
 * Server app: uses the database implementation on its own as the datasource (no network datasource)
