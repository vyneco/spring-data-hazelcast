package test.utils.repository.mapstore;

import org.springframework.data.hazelcast.repository.HazelcastStoreConfig;
import org.springframework.data.hazelcast.repository.HazelcastStoreRepository;

public interface BookRepository extends HazelcastStoreRepository<Book,String, BookMapStoreConfig> {
}
