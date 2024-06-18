package com.analytics.web.dto;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("security")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SecurityProperties {

    CookieProperties cookieProps;
    boolean allowCredentials;
}