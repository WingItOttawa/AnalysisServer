package com.wingit.analysisserver;

import java.util.UUID;

/**
 * Data entity that represents a basic online news article
 *
 * @author AveryVine
 * @since July 2018
 */
@SuppressWarnings("unused")
public class Document {

    private String id;
    private String title;
    private String url;
    private String content;
    private Wing wing;
    private Double wingValue;

    /**
     * Constructor required for POJO deserialization by both Spring and Firebase
     */
    private Document() {
    }

    /**
     * Retrieves the UUID of the document. If there isn't one, the UUID is generated
     *
     * @return the UUID of the document
     */
    public String getId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }

    /**
     * Retrieves the title of the document
     *
     * @return the title of the document
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retrieves the URL of the document
     *
     * @return the URL of the document
     */
    public String getUrl() {
        return url;
    }

    /**
     * Retrieves the content of the document
     *
     * @return the content of the document
     */
    public String getContent() {
        return content;
    }

    /**
     * Retrieves the political leaning of the document
     *
     * @return the political leaning of the document
     */
    public Wing getWing() {
        return wing;
    }

    /**
     * Retrieves the political leaning value of the document
     *
     * @return the political leaning value of the document
     */
    public Double getWingValue() {
        return wingValue;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Document)) {
            return false;
        }
        Document d = (Document) o;
        return d.getId() != null && d.getId().equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "\nDOCUMENT ->\n\tID: " + id + "\n\tTitle: " + title + "\n\tURL: " + url + "\n\tContent: " + content + "\n\tWing: " + wing + "\n\tWing Value: " + wingValue + "\n";
    }

}
