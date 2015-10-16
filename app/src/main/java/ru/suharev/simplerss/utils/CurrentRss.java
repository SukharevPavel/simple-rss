package ru.suharev.simplerss.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pasha on 08.10.2015.
 */
public class CurrentRss {

    private static List<RssItem> mCurrent;

    private static RssItem mCurrentItem;

    public static List<RssItem> get() throws NoRssException {
        if (mCurrent != null) return mCurrent;
        else throw new NoRssException(NoRssException.NOT_FOUND);
    }

    public static RssItem getCurrentItem() throws NoRssException {
        if (mCurrentItem != null) return mCurrentItem;
        else throw new NoRssException(NoRssException.NOT_FOUND);
    }

    public static RssItem getItem(int pos) throws NoRssException {
        try {
            return mCurrent.get(pos);
        } catch (IndexOutOfBoundsException ex) {
            throw new NoRssException(NoRssException.NOT_FOUND);
        }
    }

    public static void set(List<RssItem> newRss){
        mCurrent = newRss;
    }

    public static void setItem(RssItem item){
        mCurrentItem = item;
    }

    private CurrentRss(){};

}
