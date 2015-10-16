package ru.suharev.simplerss.utils;

import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by pasha on 07.10.2015.
 */
public class GetRssTask extends AsyncTask<String,Void,List<RssItem>> {

  //  private final static String RSS_EXT = "/rss.xml";

    public static String getCharacterData(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }


    @Override
    protected List<RssItem> doInBackground(String... params) {
        ArrayList<RssItem> result = new ArrayList<>();
        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();


                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();

                Document document = db.parse(is);
                Element element = document.getDocumentElement();

                NodeList nodeList = element.getElementsByTagName(RssItem.Field.ITEM);

                if (nodeList.getLength() > 0) {
                    for (int i = 0; i < nodeList.getLength(); i++) {

                        Element entry = (Element) nodeList.item(i);

                        Element _titleE = (Element) entry.getElementsByTagName(
                                RssItem.Field.TITLE).item(0);
                        Element _descriptionE = (Element) entry
                                .getElementsByTagName(RssItem.Field.DESCRIPTION).item(0);
                        Element _pubDateE = (Element) entry
                                .getElementsByTagName(RssItem.Field.PUB_DATE).item(0);
                        Element _linkE = (Element) entry.getElementsByTagName(
                                RssItem.Field.LINK).item(0);

                        String _title = _titleE.getFirstChild().getNodeValue();
                        String _description = getCharacterData(_descriptionE);
                        String _pubDate = _pubDateE.getFirstChild().getNodeValue();
                        String _link = _linkE.getFirstChild().getNodeValue();

                        //create RssItemObject and add it to the ArrayList
                        RssItem rssItem = new RssItem(_title,
                                _pubDate, _description, _link);

                        result.add(rssItem);
                    }
                }

            }
        } catch (Exception e) {
            return null;
        }

        return result;
    }

    /*
    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuilder builder = new StringBuilder();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String buffer;
                while ((buffer = in.readLine()) != null) {
                    builder.append(buffer);
                }
                result = new RssParser().parseResult(buffer);
                }
     */

}
