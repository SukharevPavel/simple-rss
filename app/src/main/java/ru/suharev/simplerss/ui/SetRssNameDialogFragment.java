package ru.suharev.simplerss.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ru.suharev.simplerss.R;
import ru.suharev.simplerss.provider.RssProvider;

/**
 * Created by pasha on 13.10.2015.
 */
public class SetRssNameDialogFragment extends DialogFragment {

    public static final String EXTRA_RSS_URI = "extra_uri";
    private EditText mNameEditText;
    private EditText mUriEditText;
    private String mRssUri;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mRssUri = bundle.getString(EXTRA_RSS_URI);
        setRetainInstance(true);
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.fragment_set_rss_name, null);
        mNameEditText = (EditText) v.findViewById(R.id.set_rss_name);
        mUriEditText = (EditText) v.findViewById(R.id.set_rss_uri);
        mUriEditText.setText(mRssUri);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(v)
                .setPositiveButton(R.string.set_rss_name_add_to_db,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String uri = String.valueOf(mUriEditText.getText());
                                String name = String.valueOf(mNameEditText.getText());
                                addNewEntry(uri,name);
                            }
                        })
                .setNegativeButton(R.string.dialog_cancel, null);
        return builder.create();

    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setOnDismissListener(null);
        super.onDestroyView();
    }

    private void addNewEntry(String uri, String name){
        ContentValues cv = new ContentValues();
        cv.put(RssProvider.Columns.NAME, name);
        cv.put(RssProvider.Columns.RSS_URI, uri);
        getContext().getContentResolver().insert(RssProvider.Uris.URI_RSS, cv);
    }


}
