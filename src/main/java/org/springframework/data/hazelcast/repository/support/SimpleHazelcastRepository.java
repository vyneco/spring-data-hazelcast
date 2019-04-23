/*
 * Copyright (c) 2008-2018, Hazelcast, Inc. All Rights Reserved.
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
package org.springframework.data.hazelcast.repository.support;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.map.QueryCache;
import com.hazelcast.map.listener.MapListener;
import com.hazelcast.query.Predicate;
import org.springframework.data.hazelcast.repository.HazelcastRepository;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.core.mapping.context.KeyValueMappingContext;
import org.springframework.data.keyvalue.repository.support.SimpleKeyValueRepository;
import org.springframework.data.repository.core.EntityInformation;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

/**
 * <P>A concrete implementation to instantiate directly rather than allow
 * Spring to generate.
 * </P>
 *
 * @param <T>  The domain object
 * @param <ID> The key of the domain object
 * @author Neil Stevenson
 */
public class SimpleHazelcastRepository<T extends Serializable, ID extends Serializable>
        extends SimpleKeyValueRepository<T, ID>
        implements HazelcastRepository<T, ID> {

    private final HazelcastInstance hazelcastInstance;
    private final String keySpace;

    public SimpleHazelcastRepository(EntityInformation<T, ID> metadata, KeyValueOperations operations, HazelcastInstance hazelcastInstance) {
        super(metadata, operations);
        this.hazelcastInstance = hazelcastInstance;


        KeyValueMappingContext<?, ?> mappingContext = (KeyValueMappingContext<?, ?>) operations.getMappingContext();
        this.keySpace = mappingContext.getRequiredPersistentEntity(metadata.getJavaType()).getKeySpace();
    }

    private IMap<ID, T> getMap() {
        return this.hazelcastInstance.getMap(keySpace);
    }

    public Collection<T> find(Predicate<ID,T> predicate) {
        return getMap().values(predicate);
    }


    @Override
    public T compute(ID id, BiFunction<? super ID, ? super T, ? extends T> handler) {
        return getMap().compute(id, handler);
    }

    @Override
    public String addEntryListener(MapListener listener, boolean includeValue) {
        return getMap().addEntryListener(listener, includeValue);
    }

    @Override
    public String addEntryListener(MapListener listener, ID key, boolean includeValue) {
        return getMap().addEntryListener(listener, key, includeValue);
    }

    @Override
    public String addEntryListener(MapListener listener, Predicate<ID, T> predicate, boolean includeValue) {
        return getMap().addEntryListener(listener, predicate, includeValue);
    }

    @Override
    public String addEntryListener(MapListener listener, Predicate<ID, T> predicate, ID key, boolean includeValue) {
        return getMap().addEntryListener(listener, predicate, key, includeValue);
    }

    @Override
    public QueryCache<ID, T> getQueryCache(String name) {
        return getMap().getQueryCache(name);
    }

    @Override
    public QueryCache<ID, T> getQueryCache(String name, Predicate<ID, T> predicate, boolean includeValue) {
        return getMap().getQueryCache(name,predicate,includeValue);
    }

    @Override
    public QueryCache<ID, T> getQueryCache(String name, MapListener listener, Predicate<ID, T> predicate, boolean includeValue) {
        return getMap().getQueryCache(name,listener,predicate,includeValue);
    }
}
