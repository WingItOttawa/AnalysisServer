package com.wingit.analysisserver;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Entry class for REST interactions with the server
 * @author AveryVine
 * @since 2018-07-05
 */
@RestController
public class WingItController {

    /**
     * Tests the connection to the server using a ping-pong response
     * @return the word pong
     */
    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok().body("Pong");
    }

}
