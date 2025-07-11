### RESPECT-Datasource

This is based on primarily on the [Android offline-first data layer architecture](https://developer.android.com/topic/architecture/data-layer/offline-first).

For each data domain (e.g. Respect Apps, OPDS, xAPI etc) there is:

An interface (e.g. OpdsDataSource) which has a local (database based) implementation and a
network (http based) implementation (in a separate module). There is also a repository implementation 
that takes care of combining data from local and remote sources.

This allows for both offline-first operation (e.g. in an app) and network-only (e.g. in a browser).
The network implementation can thus be used directly by the browser implementation (where there is
no local database).

The ViewModel sees only the interface, and does not need to be concerned with the underlying 
implementation.
