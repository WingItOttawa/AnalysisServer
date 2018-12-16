package com.wingit.analysisserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Class responsible for everything related to the process of transferring documents from the FILTER collection to the MASTER collection
 *
 * @author AveryVine
 * @since September 2018
 */
public class FilterCollectionProcessor {

    private static int MAX_DOCS_IN_FILTER = 100;
    private static int PROCESS_TIMER_IN_MINUTES = 1;
    private static long PROCESS_TIMER_IN_MILLIS = TimeUnit.MINUTES.toMillis(PROCESS_TIMER_IN_MINUTES);

    private static final Logger LOGGER = LogManager.getLogger();
    private static FilterCollectionProcessor INSTANCE = null;

    private long lastCleanTime;

    /**
     * Constructor that sets up a timer task to periodically clean the FILTER
     */
    public FilterCollectionProcessor() {
        lastCleanTime = 0L;

        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastCleanTime > PROCESS_TIMER_IN_MILLIS) {
                    LOGGER.info("Executing a timed clean of the filter collection");
                    List<Document> documents = Database.getInstance().getDocumentsInFilterCollection();
                    documents = cleanDocuments(documents);
                    Database.getInstance().transferDocuments(documents);
                    lastCleanTime = System.currentTimeMillis();
                }
            }
        };
        Timer timer = new Timer("FilterCollectionDocumentCleanerTimer");

        long delay = PROCESS_TIMER_IN_MILLIS;
        long period = PROCESS_TIMER_IN_MILLIS;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }

    /**
     * Processes the documents in the filter collection, assuming there are enough
     */
    public void processDocuments() {
        List<Document> documents = Database.getInstance().getDocumentsInFilterCollection();
        if (documents.size() >= MAX_DOCS_IN_FILTER) {
            documents = cleanDocuments(documents);
            Database.getInstance().transferDocuments(documents);
        }
    }

    /**
     * Removes duplicate and excess/unnecessary information from database entries before passing them to the MASTER collection
     *
     * @param docs the list of documents to be cleaned
     * @return the cleaned list of documents
     */
    public List<Document> cleanDocuments(List<Document> docs) {
        LOGGER.info("Cleaning the filter collection");

        //TODO: actually filter entries in the FILTER collection

        lastCleanTime = System.currentTimeMillis();
        return docs;
    }

    /**
     * Provides access to the filter collection processor (thread-safe)
     *
     * @return the singleton instance of the filter collection processor class
     */
    public static synchronized FilterCollectionProcessor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FilterCollectionProcessor();
        }
        return INSTANCE;
    }

}
