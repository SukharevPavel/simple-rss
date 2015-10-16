package ru.suharev.simplerss.utils;

import java.util.Date;

/**
 * Created by pasha on 07.10.2015.
 */
public class RssItem {

    public static class Field{
        public static final String RSS = "rss";
        public static final String CHANNEL = "channel";
        public static final String ITEM = "item";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String PUB_DATE = "pubDate";
        public static final String LINK = "link";
    }

    private String title;
    private String description;
    private String pubDate;
    private String link;

    public RssItem(String title, String pubDate, String description, String link) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.link = link;
    }

    public String getTitle() {
        return this.title;
    }

    public String getLink() {
        return this.link;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPubDate() {
        return this.pubDate;
    }
}