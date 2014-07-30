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

package com.hazelcast.map.operation;

import com.hazelcast.config.MapConfig;
import com.hazelcast.map.MapContainer;
import com.hazelcast.map.MapService;
import com.hazelcast.map.PartitionContainer;
import com.hazelcast.map.RecordStore;
import com.hazelcast.map.SizeEstimator;
import com.hazelcast.map.record.Record;
import com.hazelcast.map.record.RecordReplicationInfo;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.spi.AbstractOperation;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author mdogan 7/24/12
 */
public class MapReplicationOperation extends AbstractOperation {

    private Map<String, Set<RecordReplicationInfo>> data;

    public MapReplicationOperation() {
    }

    public MapReplicationOperation(MapService mapService, PartitionContainer container, int partitionId, int replicaIndex) {
        this.setPartitionId(partitionId).setReplicaIndex(replicaIndex);
        data = new HashMap<String, Set<RecordReplicationInfo>>(container.getMaps().size());
        for (Entry<String, RecordStore> entry : container.getMaps().entrySet()) {
            RecordStore recordStore = entry.getValue();
            MapContainer mapContainer = recordStore.getMapContainer();
            final MapConfig mapConfig = mapContainer.getMapConfig();
            if (mapConfig.getTotalBackupCount() < replicaIndex) {
                continue;
            }

            String name = entry.getKey();
            // now prepare data to migrate records
            Set<RecordReplicationInfo> recordSet = new HashSet<RecordReplicationInfo>();
            for (Entry<Data, Record> recordEntry : recordStore.getReadonlyRecordMap().entrySet()) {
                Data key = recordEntry.getKey();
                Record record = recordEntry.getValue();
                RecordReplicationInfo recordReplicationInfo;
                recordReplicationInfo = mapService.createRecordReplicationInfo(mapContainer, record);
                recordSet.add(recordReplicationInfo);
            }
            data.put(name, recordSet);
        }

    }

    public void run() {
        MapService mapService = getService();
        if (data != null) {
            for (Entry<String, Set<RecordReplicationInfo>> dataEntry : data.entrySet()) {
                Set<RecordReplicationInfo> recordReplicationInfos = dataEntry.getValue();
                final String mapName = dataEntry.getKey();
                RecordStore recordStore = mapService.getRecordStore(getPartitionId(), mapName);
                for (RecordReplicationInfo recordReplicationInfo : recordReplicationInfos) {
                    Data key = recordReplicationInfo.getKey();
                    Record newRecord = mapService.createRecord(mapName, key, recordReplicationInfo.getValue(), -1, false);
                    mapService.applyRecordInfo(newRecord, mapName, recordReplicationInfo);
                    // put record.
                    final Record existingRecord = recordStore.putRecord(key, newRecord);
                    // size estimator calculations.
                    final SizeEstimator sizeEstimator = recordStore.getSizeEstimator();
                    updateSizeEstimator(-calculateRecordSize(existingRecord, sizeEstimator), sizeEstimator);
                    updateSizeEstimator(calculateRecordSize(newRecord, sizeEstimator), sizeEstimator);
                }
                recordStore.setLoaded(true);
            }
        }
    }

    public String getServiceName() {
        return MapService.SERVICE_NAME;
    }

    protected void readInternal(final ObjectDataInput in) throws IOException {
        int size = in.readInt();
        data = new HashMap<String, Set<RecordReplicationInfo>>(size);
        for (int i = 0; i < size; i++) {
            String name = in.readUTF();
            int mapSize = in.readInt();
            Set<RecordReplicationInfo> recordReplicationInfos = new HashSet<RecordReplicationInfo>(mapSize);
            for (int j = 0; j < mapSize; j++) {
                RecordReplicationInfo recordReplicationInfo = in.readObject();
                recordReplicationInfos.add(recordReplicationInfo);
            }
            data.put(name, recordReplicationInfos);
        }
    }

    protected void writeInternal(final ObjectDataOutput out) throws IOException {
        out.writeInt(data.size());
        for (Entry<String, Set<RecordReplicationInfo>> mapEntry : data.entrySet()) {
            out.writeUTF(mapEntry.getKey());
            Set<RecordReplicationInfo> recordReplicationInfos = mapEntry.getValue();
            out.writeInt(recordReplicationInfos.size());
            for (RecordReplicationInfo recordReplicationInfo : recordReplicationInfos) {
                out.writeObject(recordReplicationInfo);
            }
        }
    }

    public boolean isEmpty() {
        return data == null || data.isEmpty();
    }

    private void updateSizeEstimator(long recordSize, SizeEstimator sizeEstimator) {
        sizeEstimator.add(recordSize);
    }

    private long calculateRecordSize(Record record, SizeEstimator sizeEstimator) {
        return sizeEstimator.getCost(record);
    }

}
