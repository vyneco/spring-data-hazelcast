package org.springframework.data.hazelcast.repository;

import com.hazelcast.core.MapLoader;

public interface HazelcastStoreConfig<K, V> {

    MapLoader<K, V> getMapLoader();

    default Integer getWriteDelaySeconds() {
        return 0;
    }
}
