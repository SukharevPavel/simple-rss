package ru.suharev.simplerss.utils;

/**
 * Created by pasha on 09.10.2015.
 */
public class NoRssException extends Exception {

    public static final String NOT_FOUND = "No Rss available for this request:";

    public NoRssException(String s){
            super(s);
    }

}
