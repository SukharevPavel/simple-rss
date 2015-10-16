package ru.suharev.simplerss.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ru.suharev.simplerss.R;
import ru.suharev.simplerss.utils.RssItem;

/**
 * Created by pasha on 06.10.2015.
 */
public class RssFragment extends ListFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static RssFragment newInstance(int sectionNumber) {
        RssFragment fragment = new RssFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public void setArray(List<RssItem> list){
        setListAdapter(new RssFeedAdapter(getContext(),
                R.layout.rss_feed_item,
                list));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_read, container, false);
        return rootView;
    }

    public class RssFeedAdapter extends ArrayAdapter<RssItem>{
        
        private LayoutInflater mInflater;
        private int mResource;

        public RssFeedAdapter(Context context, int resource, List<RssItem> objects) {
            super(context, resource, objects);
            mResource = resource;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(mResource, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.rss_title);
                holder.date = (TextView) convertView.findViewById(R.id.rss_date);
                holder.desc = (TextView) convertView.findViewById(R.id.rss_desc);
                holder.link = (TextView) convertView.findViewById(R.id.rss_link);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.title.setText(getItem(position).getTitle() != null ?
                    getItem(position).getTitle() :
                    getResources().getString(R.string.rss_no_data));
            holder.date.setText(getItem(position).getPubDate() != null ?
                    String.valueOf(getItem(position).getPubDate()) :
                    getResources().getString(R.string.rss_no_data));
            holder.desc.setText(getItem(position).getDescription() != null ?
                    getItem(position).getDescription() :
                    getResources().getString(R.string.rss_no_data));
            holder.link.setText(getItem(position).getLink() != null ?
                    getItem(position).getLink() :
                    getResources().getString(R.string.rss_no_data));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),RssItemActivity.class);
                    intent.putExtra(RssItemActivity.EXTRA_POSITION, position);
                    startActivity(intent);
                    }
            });
            return convertView;
        }

        private void openItem(RssItem item){

        }

        class ViewHolder{
            TextView title;
            TextView date;
            TextView desc;
            TextView link;
        }

    }

}