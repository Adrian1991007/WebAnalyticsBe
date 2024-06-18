package com.analytics.web.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FirebaseConfiguration {
    @Bean
    public FirebaseApp initialize() {
        try {
      FileInputStream serviceAccount =
          new FileInputStream(
              "src/main/resources/serviceAccountKey.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            log.info("Firebase has been successfully initialized.");
            return FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
