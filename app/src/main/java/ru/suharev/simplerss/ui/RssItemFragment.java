package ru.suharev.simplerss.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.suharev.simplerss.R;
import ru.suharev.simplerss.utils.CurrentRss;
import ru.suharev.simplerss.utils.NoRssException;
import ru.suharev.simplerss.utils.RssItem;


public class RssItemFragment extends Fragment {

    public static final String RSS_FEED_POS = "rss_feed_position";

    private TextView mTitleView;
    private TextView mDateView;
    private TextView mDescView;
    private TextView mLinkView;

    public RssItemFragment() {
        // Required empty public constructor
    }

    public static RssItemFragment getInstance(int position){
        RssItemFragment fragment = new RssItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(RSS_FEED_POS, position);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss_item, container, false);
        mTitleView = (TextView) view.findViewById(R.id.fragment_rss_title);
        mDateView = (TextView) view.findViewById(R.id.fragment_rss_date);
        mDescView = (TextView) view.findViewById(R.id.fragment_rss_desc);
        mLinkView = (TextView) view.findViewById(R.id.fragment_rss_link);
        Bundle bundle = getArguments();
        int position = bundle.getInt(RSS_FEED_POS);
        try {
            RssItem item = CurrentRss.getItem(position);
            setFields(item);
        } catch (NoRssException e) {
            setEmptyFields();
        }
        return view;
    }

    public void setFields(RssItem item) {
        mTitleView.setText(item.getTitle());
        mDateView.setText(item.getPubDate().toString());
        mDescView.setText(item.getDescription());
        mLinkView.setText(item.getLink());
    }

    public void setEmptyFields() {
        mTitleView.setText(getString(R.string.rss_no_data));
        mDateView.setText(R.string.rss_no_data);
        mDescView.setText(R.string.rss_no_data);
        mLinkView.setText(R.string.rss_no_data);
    }


}
