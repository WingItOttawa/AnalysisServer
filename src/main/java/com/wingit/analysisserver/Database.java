package com.wingit.analysisserver;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Singleton class that provides all access to the database
 * @author AveryVine
 * @since 2018-07-05
 */
public class Database {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String SERVICE_ACCOUNT_KEY_FILE = "ServiceAccountKey.json";
    private static final String DATABASE_URL = "https://wingit-76ee6.firebaseio.com/";

    private static Database INSTANCE = null;
    private static Firestore firestore = null;

    /**
     * Initializes the database by establishing a connection to Firebase/Firestore
     */
    private Database() {
        try {
            InputStream serviceAccount = new ClassPathResource(SERVICE_ACCOUNT_KEY_FILE).getInputStream();
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(DATABASE_URL)
                    .build();
            FirebaseApp.initializeApp(options);
            firestore = FirestoreClient.getFirestore();
            LOGGER.info("Successfully initialized Firebase access");
        } catch (IOException e) {
            LOGGER.fatal("Failed to initialize Firebase access", e);
            System.exit(1);
        }
    }

    //This works, but is commented out due to actually being useless data. Leaving it in just for reference, until we get our first working functions that add to the db.
//    public static void addDataTest() {
//
//        DocumentReference docRef = firestore.collection("test").document("alovelace");
//        // Add document data  with id "alovelace" using a hashmap
//        Map<String, Object> data = new HashMap<String, Object>();
//        data.put("first", "Ada");
//        data.put("last", "Lovelace");
//        data.put("born", 1815);
//        //asynchronously write data
//        ApiFuture<WriteResult> result = docRef.set(data);
//        // ...
//        // result.get() blocks on response
//        //System.out.println("Result : " + result.get().toString());
//
//
//        DocumentReference docRef2 = firestore.collection("test").document("aturing");
//        // Add document data with an additional field ("middle")
//        Map<String, Object> data2 = new HashMap<String, Object>();
//        data.put("first", "Alan");
//        data.put("middle", "Mathison");
//        data.put("last", "Turing");
//        data.put("born", 1912);
//
//        ApiFuture<WriteResult> result2 = docRef.set(data);
//        //System.out.println("Update time : " + result2.get().getUpdateTime());
//    }

    /**
     * Provides access to the database (thread-safe)
     * @return the singleton instance of the database access class
     */
    public static synchronized Database getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Database();
        }
        return INSTANCE;
    }

}
