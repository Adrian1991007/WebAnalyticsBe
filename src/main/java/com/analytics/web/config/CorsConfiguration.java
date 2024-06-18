package com.analytics.web.config;

import java.text.MessageFormat;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Getter
@Setter
@ConfigurationProperties(prefix = "cors")
@Configuration
@Slf4j
public class CorsConfiguration {
  private org.springframework.web.cors.CorsConfiguration config;

  private List<String> allowedOrigins;
  private List<String> allowedHeaders;
  private List<String> allowedMethods;

  @Bean
  public FilterRegistrationBean<CorsFilter> corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    config = new org.springframework.web.cors.CorsConfiguration();

    config.setAllowCredentials(true);
    config.setAllowedOrigins(allowedOrigins);
    config.setAllowedHeaders(allowedHeaders);
    config.setAllowedMethods(allowedMethods);
    source.registerCorsConfiguration("/**", config);

    FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
    bean.setOrder(0);
    
    log.info("Cors configuration has been successfully initialized.");
    
    return bean;
  }

  public void addAllowedOrigins(String newOrigin) {
    if (!allowedOrigins.contains(newOrigin)) {
      allowedOrigins.add(newOrigin);
      config.setAllowedOrigins(allowedOrigins);
      log.info(MessageFormat.format("{0} has been successfully added to cors configuration.", newOrigin));
    }
    else log.error(MessageFormat.format("The source {0} already exists in the list of accepted sources.", newOrigin));
  }
}
