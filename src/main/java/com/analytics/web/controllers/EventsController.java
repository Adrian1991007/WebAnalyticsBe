package com.analytics.web.controllers;

import com.analytics.web.dto.CountryAggregation;
import com.analytics.web.dto.Event;
import com.analytics.web.dto.User;
import com.analytics.web.exceptions.ApiError;
import com.analytics.web.services.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.text.MessageFormat;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/analytics")
public class EventsController {
  @Autowired EventService service;

  @PostMapping("/events")
  @Operation(summary = "Add new event")
  @ApiResponse(
          responseCode = "500",
          description = "INTERNAL_SERVER_ERROR",
          content = {@Content(schema = @Schema(implementation = ApiError.class))})
  public void registerEvent(@RequestBody Event event) {
    log.info(MessageFormat.format("Event register request {0}", event));
    service.registerEvent(event);
    log.info("Event registered.");
  }

  @GetMapping("/getCountries")
  @Operation(summary = "Get countries")
  @ApiResponse(
          responseCode = "500",
          description = "INTERNAL_SERVER_ERROR",
          content = {@Content(schema = @Schema(implementation = ApiError.class))})
  public List<CountryAggregation> getCountries(@AuthenticationPrincipal User user) {
    log.info(MessageFormat.format("GetCountries request for user id {0}", user.getUid()));
    List<CountryAggregation> countryList =  service.getCountries(user.getUid());
    log.info(MessageFormat.format("GetCountries response {0}", user.getUid()));
    return countryList;
  }
}
