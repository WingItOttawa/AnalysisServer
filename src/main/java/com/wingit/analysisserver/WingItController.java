package com.wingit.analysisserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Entry class for REST interactions with the server
 * @author AveryVine
 * @since 2018-07-11
 */
@RestController
public class WingItController {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Database DATABASE = Database.getInstance();

    /**
     * Entry point for REST interactions with the server
     */
    public WingItController() {
        LOGGER.info("WingItController REST interface initialized");
    }

    /**
     * Logs the method and URI of all incoming requests to the server
     * @param request the incoming HTTP request
     */
    @ModelAttribute
    protected void logRequest(HttpServletRequest request) {
        LOGGER.info("Received " + request.getMethod() + " request to " + request.getRequestURI());
    }

    /**
     * Tests the connection to the server using a ping-pong response
     * @return the word pong
     */
    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok().body("Pong");
    }

    /**
     * Adds a single document to the database
     * @param document the document to be added
     * @return a 200 response
     */
    @PostMapping("/addDocument")
    public ResponseEntity<?> addDocument(@RequestBody Document document) {
        if (document != null) {
            if (DATABASE.addDocument(document)) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.badRequest().body("Invalid parameters");
    }

    /**
     * Retrieves a single document from the database by its URL
     * @param url the URL of the document to retrieve
     * @return the retrieved document
     */
    @GetMapping("/getDocumentByUrl")
    public ResponseEntity<?> getDocumentByUrl(@RequestParam(value = "url", defaultValue = "") String url) {
        if (url != null) {
            Document document = DATABASE.getDocumentByUrl(url);
            return ResponseEntity.ok().body(document);
        }
        return ResponseEntity.badRequest().body("Invalid parameters");
    }

}
