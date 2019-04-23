package org.springframework.data.hazelcast.repository;

import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface HazelcastStoreRepository<T extends Serializable, ID extends Serializable, C extends HazelcastStoreConfig> extends HazelcastRepository<T,ID> {
}
