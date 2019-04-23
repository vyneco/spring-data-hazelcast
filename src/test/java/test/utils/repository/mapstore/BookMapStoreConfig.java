package test.utils.repository.mapstore;

import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.core.MapLoader;
import org.springframework.data.hazelcast.repository.HazelcastStoreConfig;
import org.springframework.data.hazelcast.repository.HazelcastStoreRepository;

public class BookMapStoreConfig implements HazelcastStoreConfig<String,Book> {
    @Override
    public MapLoader<String, Book> getMapLoader() {
        return null;
    }

    @Override
    public Integer getWriteDelaySeconds() {
        return null;
    }
}
