package com.analytics.web.config;

import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebsitesChangeListener {
    @Autowired
    CorsConfiguration corsConfiguration;
    @Autowired
    FirebaseApp firebaseApp;

    @PostConstruct
    public void startListening() {
        String WEBSITES = "websites";
        String WEBSITE = "website";

        Firestore firestore = FirestoreClient.getFirestore(firebaseApp);
        CollectionReference collection = firestore.collection(WEBSITES);
        
    collection.addSnapshotListener(
        (snapshots, error) -> {
          if (error != null) {
            log.error("Listener failed: " + error);
            return;
          }
          assert snapshots != null;
          for (DocumentChange documentChange : snapshots.getDocumentChanges()) {
            DocumentSnapshot document = documentChange.getDocument();
            
            if (documentChange.getType() == DocumentChange.Type.ADDED) {
              String website = (String) document.get(WEBSITE);
              corsConfiguration.addAllowedOrigins(website);
            }
          }
        });
    }
}
