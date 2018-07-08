package com.wingit.analysisserver;

/**
 * Data entity that represents a basic online news article
 * @author AveryVine
 * @since 2018-07-08
 */
public class Document {

    private String title;
    private String url;
    private String content;
    private String wing;
    private double wingValue;

    /**
     * Constructor required by Spring for converting from a JSON string to a Document object
     */
    @SuppressWarnings("unused")
    private Document() {}

    /**
     * Constructs a complete Document object
     * @param title the title of the document
     * @param url the url of the document
     * @param content the content of the document
     * @param wing the political leaning of the document, anywhere from "far-left" to "far-right"
     * @param wingValue the political leaning of the document as a number between 0 and 100, with 0 being furthest left and 100 being furthest right
     */
    public Document(String title, String url, String content, String wing, double wingValue) {
        this.title = title;
        this.url = url;
        this.content = content;
        this.wing = wing;
        this.wingValue = wingValue;
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

    public double getWingValue() {
        return wingValue;
    }

    public static Document getSampleDocument() {
        return new Document("Document title", "www.example.com/test", "This is the content", "centrist", 50);
    }

    @Override
    public String toString() {
        return "\nDOCUMENT ->\n\tTitle: " + title + "\n\tContent: " + content + "\n\tURL: " + url + "\n\tWing: " + wing + "\n\tWing Value: " + wingValue + "\n";
    }

}
