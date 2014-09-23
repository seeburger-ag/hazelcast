/*
 * Copyright (c) 2008-2013, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.core;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * PartitionService allows to query {@link Partition}s
 * and attach/detach {@link MigrationListener}s to listen partition migration events.
 *
 * @see Partition
 * @see MigrationListener
 */
public interface PartitionService {

    /**
     * Returns all partitions.
     *
     * @return all partitions
     */
    Set<Partition> getPartitions();

    /**
     * Returns partition which given key belongs to.
     *
     * @param key key
     * @return partition which given key belongs to
     */
    Partition getPartition(Object key);

    /**
     * Generates a random partition key. This is useful if you want to partition data in the same partition,
     * but don't care which partition it is going to be.
     * <p/>
     * The returned value will never be null.
     *
     * @return the random partition key.
     */
    String randomPartitionKey();

    /**
     * @param migrationListener listener
     * @return returns registration id.
     */
    String addMigrationListener(MigrationListener migrationListener);

    /**
     * @param registrationId Id of listener registration.
     * @return true if registration is removed, false otherwise
     */
    boolean removeMigrationListener(final String registrationId);

    /**
     * Checks whether the cluster is in a safe state. When in a safe state
     * it is permissible to shut down a server instance.
     *
     * @return <code>true</code> if there are no partitions being migrated, and there are sufficient backups
     * for each partition per the configuration, otherwise <code>false</code>.
     * @since 3.3
     */
    boolean isClusterSafe();

    /**
     * Check if the given member is safe to shutdown, means check if 1st backups of partitions
     * those owned by given member are sync with primary.
     *
     * @param member Cluster member to query.
     * @return <code>true</code> if member in a safe state, other wise <code>false</code>.
     * @since 3.3
     */
    boolean isMemberSafe(Member member);

    /**
     * Check if local member is safe to shutdown, means check if 1st backups of partitions
     * those owned by local member are sync with primary.
     *
     * @since 3.3
     */
    boolean isLocalMemberSafe();

    /**
     * Force local member to be safe by checking and syncing owned partitions with 1st backups.
     *
     * @since 3.3
     */
    boolean forceLocalMemberToBeSafe(long timeout, TimeUnit unit);

}
