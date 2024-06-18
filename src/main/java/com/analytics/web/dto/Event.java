package com.analytics.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Event {
  @Id
  private String eventId;

  @NotBlank(message = "ClientId is mandatory")
  private String clientId;

  @NotBlank(message = "Event type is mandatory")
  private String eventType;

  private String browser;

  @NotBlank(message = "Page path is mandatory")
  private String pagePath;

  @NotBlank(message = "Page title is mandatory")
  private String pageTitle;

  @NotNull(message = "Event time action is mandatory")
  private Date eventTime;

  private String platform;

  private String language;

  private Location location;

  private String country;

  private Boolean mobile;

  private WindowSize windowSize;

  // click event
  private String target;

  private String label;

  // leave page event
  private String referrer;

  private String timeOnPage;
  
  // request event
  private Integer status;

  private Long responseTime;

  private String url;
}
