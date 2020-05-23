package com.newcode.meeting.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FireBaseConfig {
    private Bucket bucket;

    public FireBaseConfig() throws IOException {

        FileInputStream serviceAccount = new FileInputStream("./serviceAccountKey.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("meeting-app-af0af.appspot.com")
                .build();
        FirebaseApp.initializeApp(options);
        this.bucket = StorageClient.getInstance().bucket("meeting-app-af0af.appspot.com");
    }


    public Bucket getBucket() {
        return bucket;
    }
}
