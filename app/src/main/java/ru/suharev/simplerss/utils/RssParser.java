package ru.suharev.simplerss.utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by pasha on 12.10.2015.
 */
public class RssParser {

    private static final String TAG_RSS = "rss";
    private static final String TAG_CHANNEL = "channel";
    private static final String TAG_ITEM = "item";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_PUB_DATE = "pubDate";
    private static final String TAG_LINK = "link";


    private XmlPullParser mParser;

    public RssParser() {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            factory.setNamespaceAware(true);
            mParser = factory.newPullParser();
        } catch (XmlPullParserException e) {
            mParser = null;
        }
    }

    public ArrayList<RssItem> parseResult(String s) throws XmlPullParserException, IOException {
        ArrayList<RssItem> list = new ArrayList<>();
        mParser.setInput(new StringReader(s));
        mParser.nextTag();
        mParser.require(XmlPullParser.START_TAG, null, TAG_RSS);
        while (mParser.next() != XmlPullParser.END_TAG) {
            if (mParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = mParser.getName();
            if (name.equals(TAG_CHANNEL)) {
                list.addAll(readRssItemArray());
            } else {
                skip();
            }
        }
        return list;
    }

    private ArrayList<RssItem> readRssItemArray() throws IOException, XmlPullParserException {
        ArrayList<RssItem> list = new ArrayList<>();
        mParser.require(XmlPullParser.START_TAG, null, TAG_CHANNEL);
        while (mParser.next() != XmlPullParser.END_TAG) {
            if (mParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = mParser.getName();
            if (name.equals(TAG_ITEM)) {
                list.add(readRssItem());
            } else {
                skip();
            }
        }
        return list;
    }


    private RssItem readRssItem() throws XmlPullParserException, IOException {
        String title = null;
        String pubDate = null;
        String desc = null;
        String link = null;
        mParser.require(XmlPullParser.START_TAG, null, TAG_ITEM);
        while (mParser.next() != XmlPullParser.END_TAG) {
            if (mParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = mParser.getName();
            switch (name) {
                case TAG_TITLE:
                    title = readTitle();
                    break;
                case TAG_PUB_DATE:
                    pubDate = readPubDate();
                    break;
                case TAG_DESCRIPTION:
                    desc = readDesc();
                    break;
                case TAG_LINK:
                    link = readLink();
                    break;
                default:
                    skip();
                    break;
            }
        }
        return new RssItem(title, pubDate, desc, link);
    }


    private String readTitle() throws IOException, XmlPullParserException {
        mParser.require(XmlPullParser.START_TAG, null, TAG_TITLE);
        String text = readString();
        mParser.require(XmlPullParser.END_TAG, null, TAG_TITLE);
        return text;
    }

    private String readDesc() throws IOException, XmlPullParserException {
        mParser.require(XmlPullParser.START_TAG, null, TAG_DESCRIPTION);
        String desc = readString();
        mParser.require(XmlPullParser.END_TAG, null, TAG_DESCRIPTION);
        return desc;
    }

    private String readPubDate() throws IOException, XmlPullParserException {
        mParser.require(XmlPullParser.START_TAG, null, TAG_PUB_DATE);
        String date = readString();
        mParser.require(XmlPullParser.END_TAG, null, TAG_PUB_DATE);
        return date;
    }

    private String readLink() throws IOException, XmlPullParserException {
        mParser.require(XmlPullParser.START_TAG, null, TAG_LINK);
        String link = readString();
        mParser.require(XmlPullParser.END_TAG, null, TAG_LINK);
        return link;
    }

    private String readString() throws IOException, XmlPullParserException {
        String result = "";
        if (mParser.next() == XmlPullParser.TEXT) {
            result = mParser.getText();
            mParser.nextTag();
        }
        return result;
    }

    private void skip() throws XmlPullParserException, IOException {
        if (mParser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (mParser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}

