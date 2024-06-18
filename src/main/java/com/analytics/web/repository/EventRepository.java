package com.analytics.web.repository;

import com.analytics.web.dto.Event;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findByClientId(String clientId);

    List<Event> findByClientIdAndEventType(String clientId, String eventType);
}
