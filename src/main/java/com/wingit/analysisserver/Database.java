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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Singleton class that provides all access to the database
 *
 * @author AveryVine
 * @since September 2018
 */
public class Database {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String SERVICE_ACCOUNT_KEY = "SERVICE_ACCOUNT_KEY";
    private static final String DATABASE_URL = "https://wingit-76ee6.firebaseio.com/";
    private static Database INSTANCE = null;

    private Firestore db;
    private FilterCollectionProcessor filterCollectionProcessor;

    private enum Collections {
        MASTER("master"),
        FILTER("filter"),
        WORD_FREQUENCY("word-frequency"),
        WING_PROBABILITY("wing-probability");

        private final String name;

        /**
         * Constructs a Collections enum to be a part of the persistent set
         *
         * @param name the name of the Firebase collection
         */
        Collections(String name) {
            this.name = name;
        }
    }

    /**
     * Initializes the database by establishing a connection to Firebase/Firestore
     */
    private Database() {
        try {
            String serviceAccountKeyJsonRaw = System.getenv(SERVICE_ACCOUNT_KEY);
            String serviceAccountKeyJson;
            try {
                serviceAccountKeyJson = Utils.escapeWhitespace(serviceAccountKeyJsonRaw);
            } catch (IllegalArgumentException e) {
                throw new FileNotFoundException("SERVICE_ACCOUNT_KEY missing from environment!");
            }

            InputStream serviceAccountKey = new ByteArrayInputStream(serviceAccountKeyJson.getBytes(StandardCharsets.UTF_8));
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountKey))
                    .setDatabaseUrl(DATABASE_URL)
                    .build();
            FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore();
            LOGGER.info("Successfully initialized Firebase access");

            filterCollectionProcessor = FilterCollectionProcessor.getInstance();
        } catch (IOException e) {
            LOGGER.fatal("Failed to initialize Firebase access", e);
            System.exit(1);
        }
    }

    /**
     * Adds a document to the database
     *
     * @param document the document to be added
     * @return a boolean variable indicating whether adding the document was successful or not
     */
    public boolean addDocument(Document document) {
        LOGGER.info("Adding document: " + document);
        Collections collection = Collections.FILTER;

        //TODO - investigate rules for properly overwriting existing documents
        ApiFuture<WriteResult> result = db.collection(collection.name)
                .document(document.getId())
                .set(document);

        try {
            LOGGER.info("Added document to " + collection + " collection: " + document + "(at " + result.get().getUpdateTime() + ")");
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Something went wrong when adding document with id " + document.getId() + " to " + collection + " collection", e);
            return false;
        }

        filterCollectionProcessor.processDocuments();

        return true;
    }

    /**
     * Retrieves a document from the database by url
     *
     * @param url the url of the document to retrieve
     * @return the document with matching url
     */
    public Document getDocumentByUrl(String url) {
        LOGGER.info("Getting document by url: " + url);
        Collections collection = Collections.MASTER;

        Query query = db.collection(collection.name)
                .whereEqualTo("url", url)
                .limit(1);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            List<QueryDocumentSnapshot> docsSnapshot = querySnapshot.get().getDocuments();
            if (docsSnapshot.size() > 0) {
                Document document = docsSnapshot.get(0).toObject(Document.class);
                LOGGER.info("Found document in collection " + collection + ": " + document);
                return document;
            }
            return null;
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Something went wrong when getting document with url " + url + " in collection " + collection);
            return null;
        }
    }

    /**
     * Retrieves the list of documents in the filter collection
     *
     * @return the list of documents from the filter collection
     */
    public List<Document> getDocumentsInFilterCollection() {
        Collections collection = Collections.FILTER;

        Query query = db.collection(collection.name);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            List<QueryDocumentSnapshot> docsSnapshot = querySnapshot.get().getDocuments();
            List<Document> documents = new ArrayList<>();
            for (DocumentSnapshot docSnapshot : docsSnapshot) {
                documents.add(docSnapshot.toObject(Document.class));
            }
            return documents;
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Something went wrong when getting the number of documents in the " + collection + " collection");
            return null;
        }
    }

    /**
     * Transfers document objects from the FILTER collection to the MASTER collection
     *
     * @param docs the list of documents to transfer
     */
    public void transferDocuments(List<Document> docs) {
        Collections fromCollection = Collections.FILTER;
        Collections toCollection = Collections.MASTER;

        WriteBatch batch = db.batch();
        for (Document doc : docs) {
            String id = doc.getId();

            DocumentReference fromDocRef = db.collection(fromCollection.name).document(id);
            DocumentReference toDocRef = db.collection(toCollection.name).document(id);
            batch.delete(fromDocRef);
            batch.set(toDocRef, doc);
        }
        ApiFuture<List<WriteResult>> future = batch.commit();

        try {
            if (future.get().size() > 0) {
                LOGGER.info("Copied " + docs.size() + " documents from " + fromCollection + " to " + toCollection + " (at " + future.get().get(0).getUpdateTime() + ")");
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Something went wrong when copying document from " + fromCollection + " to " + toCollection, e);
        }

    }

    /**
     * Provides access to the database (thread-safe)
     *
     * @return the singleton instance of the database access class
     */
    public static synchronized Database getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Database();
        }
        return INSTANCE;
    }

}
