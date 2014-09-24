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

package com.hazelcast.cache.impl;

import com.hazelcast.config.CacheConfig;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.spi.NodeEngine;
import com.hazelcast.util.EmptyStatement;
import com.hazelcast.util.ExceptionUtil;

import java.util.Map;
import java.util.Set;

/**
 * Helper methods for Cache Proxy impl
 */
public final class CacheProxyUtil {

    public static final int AWAIT_COMPLETION_TIMEOUT_SECONDS = 60;

    static final String NULL_KEY_IS_NOT_ALLOWED = "Null key is not allowed!";
    static final String NULL_VALUE_IS_NOT_ALLOWED = "Null value is not allowed!";

    private CacheProxyUtil() {
    }

    public static void validateResults(Map<Integer, Object> results) {
        for (Object result : results.values()) {
            if (result != null && result instanceof CacheClearResponse) {
                final Object response = ((CacheClearResponse) result).getResponse();
                if (response instanceof Throwable) {
                    ExceptionUtil.sneakyThrow((Throwable) response);
                }
            }
        }
    }

    protected static int getPartitionId(NodeEngine nodeEngine, Data key) {
        return nodeEngine.getPartitionService().getPartitionId(key);
    }

    public static <K> void validateNotNull(K key) {
        if (key == null) {
            throw new NullPointerException(NULL_KEY_IS_NOT_ALLOWED);
        }
    }

    public static <K, V> void validateNotNull(K key, V value) {
        if (key == null) {
            throw new NullPointerException(NULL_KEY_IS_NOT_ALLOWED);
        }
        if (value == null) {
            throw new NullPointerException(NULL_VALUE_IS_NOT_ALLOWED);
        }
    }

    public static <K, V> void validateNotNull(K key, V value1, V value2) {
        if (key == null) {
            throw new NullPointerException(NULL_KEY_IS_NOT_ALLOWED);
        }
        if (value1 == null) {
            throw new NullPointerException(NULL_VALUE_IS_NOT_ALLOWED);
        }
        if (value2 == null) {
            throw new NullPointerException(NULL_VALUE_IS_NOT_ALLOWED);
        }
    }

    public static <K> void validateNotNull(Set<? extends K> keys) {
        if (keys == null || keys.contains(null)) {
            throw new NullPointerException(NULL_KEY_IS_NOT_ALLOWED);
        }
    }

    public static <K, V> void validateNotNull(Map<? extends K, ? extends V> map) {
        if (map == null) {
            throw new NullPointerException("map is null");
        }
        boolean containsNullKey = false;
        boolean containsNullValue = false;
        //in case this map keySet or values do not support null values
        //TODO is it possible to validate a map without try-catch blocks, more efficiently?
        try {
            containsNullKey = map.keySet().contains(null);
        } catch (NullPointerException e) {
            //ignore as null not allowed for this map
            EmptyStatement.ignore(e);
        }
        try {
            containsNullValue = map.values().contains(null);
        } catch (NullPointerException e) {
            //ignore as null not allowed for this map
            EmptyStatement.ignore(e);
        }
        if (containsNullKey) {
            throw new NullPointerException(NULL_KEY_IS_NOT_ALLOWED);
        }
        if (containsNullValue) {
            throw new NullPointerException(NULL_VALUE_IS_NOT_ALLOWED);
        }

    }

    public static <K> void validateConfiguredTypes(CacheConfig cacheConfig, K key)
            throws ClassCastException {
        final Class keyType = cacheConfig.getKeyType();
        validateConfiguredKeyType(keyType, key);
    }

    public static <K, V> void validateConfiguredTypes(CacheConfig cacheConfig, K key, V value)
            throws ClassCastException {
        final Class keyType = cacheConfig.getKeyType();
        final Class valueType = cacheConfig.getValueType();
        validateConfiguredKeyType(keyType, key);
        validateConfiguredValueType(valueType, value);
    }

    public static <K, V> void validateConfiguredTypes(CacheConfig cacheConfig, K key, V value1, V value2)
            throws ClassCastException {
        final Class keyType = cacheConfig.getKeyType();
        final Class valueType = cacheConfig.getValueType();
        validateConfiguredKeyType(keyType, key);
        validateConfiguredValueType(valueType, value1);
        validateConfiguredValueType(valueType, value2);
    }

    public static <K> void validateConfiguredKeyType(Class<K> keyType, K key)
            throws ClassCastException {
        if (Object.class != keyType) {
            //means type checks required
            if (!keyType.isAssignableFrom(key.getClass())) {
                throw new ClassCastException("Key " + key + "is not assignable to " + keyType);
            }
        }
    }

    public static <V> void validateConfiguredValueType(Class<V> valueType, V value)
            throws ClassCastException {
        if (Object.class != valueType) {
            //means type checks required
            if (!valueType.isAssignableFrom(value.getClass())) {
                throw new ClassCastException("Value " + value + "is not assignable to " + valueType);
            }
        }
    }

}
