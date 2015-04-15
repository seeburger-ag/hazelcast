/*
 * Copyright (c) 2008-2015, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.client.impl.protocol.parameters;

import com.hazelcast.annotation.EncodeMethod;
import com.hazelcast.annotation.GenerateParameters;
import com.hazelcast.nio.serialization.Data;

import java.util.List;
import java.util.Set;

@GenerateParameters(id = 1, name = "Map", ns = "Hazelcast.Client.Protocol.Map")
public interface MapTemplate {

    @EncodeMethod(id = 1)
    void put(String name, Data key, Data value, long threadId, long ttl);

    @EncodeMethod(id = 2)
    void get(String name, Data key, long threadId);

    @EncodeMethod(id = 3)
    void remove(String name, Data key, long threadId);

    @EncodeMethod(id = 4)
    void replace(String name, Data key, Data value, long threadId);

    @EncodeMethod(id = 5)
    void replaceIfSame(String name, Data key, Data testValue, Data value, long threadId);

    @EncodeMethod(id = 9)
    void putAsync(String name, Data key, Data value, long threadId, long ttl);

    @EncodeMethod(id = 10)
    void getAsync(String name, Data key, long threadId);

    @EncodeMethod(id = 11)
    void removeAsync(String name, Data key, long threadId);

    @EncodeMethod(id = 12)
    void containsKey(String name, Data key, long threadId);

    @EncodeMethod(id = 13)
    void containsValue(String name, Data value);

    @EncodeMethod(id = 14)
    void removeIfSame(String name, Data key, Data value, long threadId);

    @EncodeMethod(id = 15)
    void delete(String name, Data key, long threadId);

    @EncodeMethod(id = 16)
    void flush(String name);

    @EncodeMethod(id = 17)
    void tryRemove(String name, Data key, long threadId, long timeout);

    @EncodeMethod(id = 18)
    void tryPut(String name, Data key, Data value, long threadId, long timeout);

    @EncodeMethod(id = 19)
    void putTransient(String name, Data key, Data value, long threadId, long ttl);

    @EncodeMethod(id = 20)
    void putIfAbsent(String name, Data key, Data value, long threadId, long ttl);

    @EncodeMethod(id = 21)
    void set(String name, Data key, Data value, long threadId, long ttl);

    @EncodeMethod(id = 22)
    void lock(String name, Data key, long threadId, long ttl, long timeout);

    @EncodeMethod(id = 23)
    void isLocked(String name, Data key, long threadId);

    @EncodeMethod(id = 24)
    void unlock(String name, Data key, long threadId, boolean force);

    @EncodeMethod(id = 25)
    void addInterceptor(String name, Data interceptor);

    @EncodeMethod(id = 26)
    void removeInterceptor(String name, String id);

    @EncodeMethod(id = 27)
    void addEntryListenerToKeyWithPredicate(String name, Data key, Data predicate, boolean includeValue);

    @EncodeMethod(id = 28)
    void addEntryListenerWithPredicate(String name, Data predicate, boolean includeValue);

    @EncodeMethod(id = 29)
    void addEntryListenerToKey(String name, Data key, boolean includeValue);

    @EncodeMethod(id = 30)
    void addEntryListener(String name, boolean includeValue);

    @EncodeMethod(id = 31)
    void addNearCacheEntryListener(String name, boolean includeValue);

    @EncodeMethod(id = 32)
    void removeEntryListener(String name, String registrationId);

    @EncodeMethod(id = 33)
    void addPartitionLostListener(String name);

    @EncodeMethod(id = 34)
    void removePartitionLostListener(String name, String registrationId);

    @EncodeMethod(id = 35)
    void getEntryView(String name, Data key, long threadId);

    @EncodeMethod(id = 36)
    void evict(String name, Data key, long threadId);

    @EncodeMethod(id = 37)
    void evictAll(String name);

    @EncodeMethod(id = 38)
    void loadAll(String name, boolean replaceExistingValues);

    @EncodeMethod(id = 39)
    void loadGivenKeys(String name, List<Data> keys, boolean replaceExistingValues);

    @EncodeMethod(id = 40)
    void keySet(String name);

    @EncodeMethod(id = 41)
    void getAll(String name, Set<Data> keys);

    @EncodeMethod(id = 42)
    void values(String name);


    @EncodeMethod(id = 43)
    void entrySet(String name);

    @EncodeMethod(id = 44)
    void keySetWithPredicate(String name, Data predicate);

    @EncodeMethod(id = 45)
    void valuesWithPredicate(String name, Data predicate);

    @EncodeMethod(id = 46)
    void entriesWithPredicate(String name, Data predicate);

    @EncodeMethod(id = 47)
    void addIndex(String name, String attribute, boolean ordered);

    @EncodeMethod(id = 48)
    void size(String name);

    @EncodeMethod(id = 49)
    void isEmpty(String name);

    @EncodeMethod(id = 50)
    void putAll(String name, List<Data> keys, List<Data> values);

    @EncodeMethod(id = 51)
    void clear(String name);

    @EncodeMethod(id = 52)
    void executeOnKey(String name, Data entryProcessor, Data key);

    @EncodeMethod(id = 53)
    void submitToKey(String name, Data entryProcessor, Data key);

    @EncodeMethod(id = 54)
    void executeOnAllKeys(String name, Data entryProcessor);

    @EncodeMethod(id = 55)
    void executeWithPredicate(String name, Data entryProcessor, Data predicate);

    @EncodeMethod(id = 56)
    void executeOnKeys(String name, Data entryProcessor, Set<Data> keys);

}