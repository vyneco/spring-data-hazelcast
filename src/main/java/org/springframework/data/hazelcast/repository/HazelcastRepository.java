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
package org.springframework.data.hazelcast.repository;

import com.hazelcast.core.EntryListener;
import com.hazelcast.map.QueryCache;
import com.hazelcast.map.listener.MapListener;
import com.hazelcast.query.Predicate;
import com.hazelcast.spi.annotation.Beta;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.BiFunction;

/**
 * <p>
 * Subtype {@link org.springframework.stereotype.Repository @Repository} for Hazelcast usage.
 * </P>
 * <p>
 * Although part of the rationale of the repository interface is to abstract the implementation, it is useful for type
 * checking to confirm the allowed type generics for the domain classes.
 * </P>
 * <p>
 * Note that {@link org.springframework.data.keyvalue.repository.KeyValueRepository KeyValueRepository} defines that the
 * {@code ID} class extends {@link Serializable}.
 * </P>
 *
 * @param <T>  The type of the domain value class
 * @param <ID> The type of the domain key class
 * @author Neil Stevenson
 */
@NoRepositoryBean
public interface HazelcastRepository<T extends Serializable, ID extends Serializable>
        extends KeyValueRepository<T, ID> {

    T compute(ID id, BiFunction<? super ID, ? super T, ? extends T> handler);

    /**
     * Adds a {@link MapListener} for this map.
     * <p>
     * To receive an event, you should implement a corresponding {@link MapListener} sub-interface for that event.
     *
     * @param listener     {@link MapListener} for this map
     * @param includeValue {@code true} if {@code EntryEvent} should contain the value
     * @return a UUID.randomUUID().toString() which is used as a key to remove the listener
     * @throws NullPointerException if the specified listener is null
     * @see MapListener
     */
    String addEntryListener(MapListener listener, boolean includeValue);

    /**
     * Adds a {@link MapListener} for this map. To receive an event, you should
     * implement a corresponding {@link MapListener} sub-interface for that event.
     * <p>
     * <b>Warning:</b>
     * <p>
     * This method uses {@code hashCode} and {@code equals} of the binary form of
     * the {@code key}, not the actual implementations of {@code hashCode} and {@code equals}
     * defined in the {@code key}'s class.
     *
     * @param listener     {@link MapListener} for this map
     * @param key          key to listen for
     * @param includeValue {@code true} if {@code EntryEvent} should contain the value
     * @return a UUID.randomUUID().toString() which is used as a key to remove the listener
     * @throws NullPointerException if the specified listener is null
     * @throws NullPointerException if the specified key is null
     * @see MapListener
     */
    String addEntryListener(MapListener listener, ID key, boolean includeValue);

    /**
     * Adds a {@link MapListener} for this map.
     * <p>
     * To receive an event, you should implement a corresponding {@link MapListener} sub-interface for that event.
     *
     * @param listener     the added continuous {@link MapListener} for this map
     * @param predicate    predicate for filtering entries
     * @param includeValue {@code true} if {@code EntryEvent} should contain the value
     * @return a UUID.randomUUID().toString() which is used as a key to remove the listener
     * @throws NullPointerException if the specified listener is null
     * @throws NullPointerException if the specified predicate is null
     * @see MapListener
     */
    String addEntryListener(MapListener listener, Predicate<ID, T> predicate, boolean includeValue);

    /**
     * Adds a {@link MapListener} for this map.
     * <p>
     * To receive an event, you should implement a corresponding {@link MapListener} sub-interface for that event.
     *
     * @param listener     the continuous {@link MapListener} for this map
     * @param predicate    predicate for filtering entries
     * @param key          key to listen for
     * @param includeValue {@code true} if {@code EntryEvent} should contain the value
     * @return a UUID.randomUUID().toString() which is used as a key to remove the listener
     * @throws NullPointerException if the specified listener is null
     * @throws NullPointerException if the specified predicate is null
     * @see MapListener
     */
    String addEntryListener(MapListener listener, Predicate<ID,T> predicate, ID key, boolean includeValue);


    Collection<T> find(Predicate<ID,T> predicate);

    /**
     * Returns corresponding {@code QueryCache} instance for the supplied {@code name} or null.
     * <p>
     * If there is a previously created {@link QueryCache} with the supplied {@code name} or if a declarative
     * configuration exists for the supplied {@code name} this method returns or creates the instance respectively,
     * otherwise returns null.
     *
     * @param name the name of {@code QueryCache}
     * @return the {@code QueryCache} instance or null if there is no corresponding {@code QueryCacheConfig}
     * @throws NullPointerException if the specified {@code name} is {@code null}
     * @see QueryCache
     * @since 3.8
     */
    @Beta
    QueryCache<ID, T> getQueryCache(String name);

    /**
     * Creates an always up to date snapshot of this {@code IMap} according to the supplied parameters.
     * <p>
     * If there is a previously created {@link QueryCache} with the supplied {@code name}, this method returns that
     * {@link QueryCache} and ignores {@code predicate} and {@code includeValue} parameters. Otherwise it creates and returns
     * a new {@link QueryCache} instance.
     * <p>
     * Also note that if there exists a {@link com.hazelcast.config.QueryCacheConfig QueryCacheConfig} for the supplied
     * {@code name}, {@code predicate} and {@code includeValue} parameters will overwrite corresponding ones
     * in {@link com.hazelcast.config.QueryCacheConfig}.
     *
     * @param name         the name of {@code QueryCache}
     * @param predicate    the predicate for filtering entries
     * @param includeValue {@code true} if this {@code QueryCache} is allowed to cache values of entries, otherwise {@code false}
     * @return the {@code QueryCache} instance with the supplied {@code name}
     * @throws NullPointerException if the specified {@code name} or {@code predicate} is null
     * @see QueryCache
     * @since 3.8
     */
    @Beta
    QueryCache<ID, T> getQueryCache(String name, Predicate<ID, T> predicate, boolean includeValue);

    /**
     * Creates an always up to date snapshot of this {@code IMap} according to the supplied parameters.
     * <p>
     * If there is a previously created {@link QueryCache} with the supplied {@code name}, this method returns that
     * {@link QueryCache} and ignores {@code listener}, {@code predicate} and {@code includeValue} parameters.
     * Otherwise it creates and returns a new {@link QueryCache} instance.
     * <p>
     * Also note that if there exists a {@link com.hazelcast.config.QueryCacheConfig QueryCacheConfig} for the supplied
     * {@code name}, {@code listener},{@code predicate} and {@code includeValue} parameters will overwrite corresponding ones
     * in {@link com.hazelcast.config.QueryCacheConfig}.
     *
     * @param name         the name of {@code QueryCache}
     * @param listener     the {@code MapListener} which will be used to listen this {@code QueryCache}
     * @param predicate    the predicate for filtering entries
     * @param includeValue {@code true} if this {@code QueryCache} is allowed to cache values of entries, otherwise {@code false}
     * @return the {@code QueryCache} instance with the supplied {@code name}
     * @throws NullPointerException if the specified {@code name} or {@code listener} or {@code predicate} is null
     * @see QueryCache
     * @since 3.8
     */
    @Beta
    QueryCache<ID, T> getQueryCache(String name, MapListener listener, Predicate<ID, T> predicate, boolean includeValue);

}
