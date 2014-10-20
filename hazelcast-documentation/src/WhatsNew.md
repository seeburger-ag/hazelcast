# What's New in Hazelcast 3.3



## Release Notes

### New Features
This section provides the new features introduced with Hazelcast 3.3 release. 

- Heartbeat for Java client: Before this release, a Java client could not detect a node as dead, if the client is not trying to connect to it. With this heartbeat feature, each node will be pinged periodically. If no response is returned from a node, it will be deemed as dead. Main goal of this feature is to decrease the time for detection of dead (disconnected) nodes by Java clients, so that the user operations will be sent directly to a responsive one. For more information, please see [Client Properties](#client-properties).
- Tomcat 6 and 7 Web Sessions Clustering: Please see [Web Session Replication](#web-session-replication).
- Replicated Map implemented: Please see [Replicated Map](#replicated-map-beta)
- WAN Replication improved: Added configurable replication queue size [WAN Replication Queue Size](#wan-replication-queue-size).
- Data Aggregation implemented: Added common data aggregations, please find [Aggregators](#aggregators) documentation.
- EvictAll and LoadAll features for IMap: `evictAll` and `loadAll` methods have been introduced to be able to evict all entries except the locked ones and that loads all or a set of keys from a configured map store, respectively. Please see [Evicting All Entries](#evicting-all-entries) and [Forcing All Keys to be Loaded](#forcing-all-keys-to-be-loaded) sections for more information.
- Hazelcast JCache implementation introduced: Starting with release 3.3.1, Hazelcast offers its JCache implementation. Please see [Hazelcast JCache Implementation](#hazelcast-jcache-implementation) for details.


