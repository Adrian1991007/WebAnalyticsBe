package com.analytics.web.services;

import static com.analytics.web.utils.Constants.EMPTY_STRING;
import static com.analytics.web.utils.Constants.EVENT_TYPES;

import com.analytics.web.dto.*;
import com.analytics.web.repository.EventRepository;
import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.byteowls.jopencage.model.JOpenCageResult;
import com.byteowls.jopencage.model.JOpenCageReverseRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

@Service
public class EventService {
  @Autowired
  EventRepository eventRepository;

  @Autowired
  private MongoTemplate mongoTemplate;
  @Value("${opencage.api.key}")
  private String openCageApiKey;

  public void registerEvent(Event event) {
    validateEvent(event);
    if(event.getLocation() != null)
    {
      event.setCountry(getCountry(event.getLocation().getLatitude(), event.getLocation().getLongitude()));
    }
    insertEvent(event);
  }

  public List<CountryAggregation> getCountries(String clientId) {
    List<CountryAggregation> countryList;

    GroupOperation groupOperation = Aggregation.group("country").count().as("value");

    Aggregation aggregation =
        Aggregation.newAggregation(
            Aggregation.match(Criteria.where("clientId").is(clientId)), groupOperation);

    AggregationResults<CountryAggregation> results =
        mongoTemplate.aggregate(aggregation, Event.class, CountryAggregation.class);
    countryList = results.getMappedResults();

    return countryList;
    }

  public void validateEvent(Event event) {
    try {
      FirebaseAuth.getInstance().getUser(event.getClientId());
    } catch (FirebaseAuthException e) {
      throw new RuntimeException(e);
    }

    if (!EVENT_TYPES.contains(event.getEventType())) {
      throw new RuntimeException("Invalid event type");
    }
  }

  public void insertEvent(Event event) {
    eventRepository.insert(event);
  }

  public String getCountry(double latitude, double longitude) {
    JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder(openCageApiKey);

    JOpenCageReverseRequest request = new JOpenCageReverseRequest(latitude, longitude);
    request.setNoAnnotations(true);

    JOpenCageResponse response = jOpenCageGeocoder.reverse(request);
    JOpenCageResult result = response.getResults().get(0);

    if(result != null)
    {
      return result.getComponents().getIso31661Alpha2();
    }

    return EMPTY_STRING;
  }

}
