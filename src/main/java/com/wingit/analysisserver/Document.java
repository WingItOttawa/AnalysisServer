package com.wingit.analysisserver;

import java.util.*;

/**
 * Data entity that represents a basic online news article
 * @author AveryVine
 * @since 2018-07-08
 */
@SuppressWarnings("unused")
public class Document {

    private String id;
    private String title;
    private String url;
    private String content;
    private String wing;
    private Double wingValue;

    /**
     * Constructor required for POJO deserialization by both Spring and Firebase
     */
    private Document() {}

    public void generateUUID() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    public String getWing() {
        return wing;
    }

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
