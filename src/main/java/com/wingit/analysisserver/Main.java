package com.wingit.analysisserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry class for SpringBootApplication
 *
 * @author AveryVine
 * @since July 2018
 */
@SpringBootApplication
public class Main {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Entry point for SpringBootApplication
     *
     * @param args optional command line arguments
     */
    public static void main(String[] args) {
        LOGGER.info("WingIt Analysis Server initialized");
        SpringApplication.run(Main.class, args);
    }
}
