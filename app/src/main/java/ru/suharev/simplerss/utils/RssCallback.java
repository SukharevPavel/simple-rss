package ru.suharev.simplerss.utils;

public interface RssCallback {

    void onAddNewRss(String rss);

    void onGetRssRequest(String uri);

}