package com.wingit.analysisserver;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Singleton class that provides all access to the database
 * @author AveryVine
 * @since 2018-07-11
 */
public class Database {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String SERVICE_ACCOUNT_KEY = "SERVICE_ACCOUNT_KEY";
    private static final String DATABASE_URL = "https://wingit-76ee6.firebaseio.com/";

    private static Database INSTANCE = null;
    private static Firestore db = null;

    private enum Collections {
        MASTER("master"),
        FILTER("filter"),
        WORD_FREQUENCY("word-frequency"),
        WING_PROBABILITY("wing-probability");

        private final String collection;

        Collections(String collection) {
            this.collection = collection;
        }
    }

    /**
     * Initializes the database by establishing a connection to Firebase/Firestore
     */
    private Database() {
        try {
            String serviceAccountKeyJsonRaw = System.getenv(SERVICE_ACCOUNT_KEY);
            String serviceAccountKeyJson = Utils.escapeWhitespace(serviceAccountKeyJsonRaw);
            InputStream serviceAccountKey = new ByteArrayInputStream(serviceAccountKeyJson.getBytes(StandardCharsets.UTF_8));
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountKey))
                    .setDatabaseUrl(DATABASE_URL)
                    .build();
            FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore();
            LOGGER.info("Successfully initialized Firebase access");
        } catch (IOException | IllegalArgumentException e) {
            LOGGER.fatal("Failed to initialize Firebase access", e);
            System.exit(1);
        }
    }

    /**
     * Adds a document to the database
     * @param document the document to be added
     * @return a boolean variable indicating whether adding the document was successful or not
     */
    public boolean addDocument(Document document) {
        LOGGER.info("Adding document: " + document.toString());
        if (document.getId() == null) {
            document.generateUUID();
        }

        //TODO - this should be changed to go through the filter database first
        //TODO - add some rules to try to overwrite existing documents properly
        ApiFuture<WriteResult> result = db.collection(Collections.MASTER.collection).document(document.getId()).set(document);
        try {
            LOGGER.info("Added document: " + document.toString() + "(at " + result.get().getUpdateTime() + ")");
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Something went wrong when adding document with id " + document.getId(), e);
            return false;
        }

        return true;
    }

    /**
     * Retrieves a document from the database by url
     * @param url the url of the document to retrieve
     * @return the document with matching url
     */
    public Document getDocumentByUrl(String url) {
        LOGGER.info("Getting document by url: " + url);
        CollectionReference collection = db.collection(Collections.MASTER.collection);
        Query query = collection.whereEqualTo("url", url).limit(1);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        try {
            List<QueryDocumentSnapshot> docs = querySnapshot.get().getDocuments();
            if (docs.size() > 0) {
                Document document = docs.get(0).toObject(Document.class);
                LOGGER.info("Found document: " + document.toString());
                return document;
            }
            return null;
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Something went wrong when getting document with url " + url);
            return null;
        }
    }

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
