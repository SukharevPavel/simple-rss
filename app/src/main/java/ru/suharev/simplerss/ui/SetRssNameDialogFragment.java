package ru.suharev.simplerss.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ru.suharev.simplerss.R;
import ru.suharev.simplerss.provider.RssProvider;

/**
 * Фрагмент для изменения параметров RSS-ленты
 */
public class SetRssNameDialogFragment extends DialogFragment {

    public static final String EXTRA_RSS_URI = "extra_uri";
    public static final String EXTRA_RSS_POSITION = "extra_position";
    public static final String EXTRA_RSS_NAME = "extra_name";
    public static final String EXTRA_RSS_IS_CHANGING = "extra_is_changing";
    private EditText mNameEditText;
    private EditText mUriEditText;
    private int mId;
    private String mRssUri;
    private boolean isUpdate = false;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.fragment_set_rss_name, null);

        mNameEditText = (EditText) v.findViewById(R.id.set_rss_name);
        mUriEditText = (EditText) v.findViewById(R.id.set_rss_uri);

        Bundle bundle = getArguments();
        mRssUri = bundle.getString(EXTRA_RSS_URI);

        if (isUpdate = bundle.getBoolean(EXTRA_RSS_IS_CHANGING, false)) {
            mNameEditText.setText(bundle.getString(EXTRA_RSS_NAME));
            mId = bundle.getInt(EXTRA_RSS_POSITION);
        }

        mUriEditText.setText(mRssUri);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(v)
                .setPositiveButton(R.string.set_rss_name_add_to_db,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String uri = String.valueOf(mUriEditText.getText());
                                String name = String.valueOf(mNameEditText.getText());
                                if (!isUpdate) addNewEntry(uri, name);
                                else changeEntry(uri, name, mId);
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

    private void changeEntry(String uri, String name, int position) {
        ContentValues cv = new ContentValues();
        cv.put(RssProvider.Columns.NAME, name);
        cv.put(RssProvider.Columns.RSS_URI, uri);
        getContext().getContentResolver().update(RssProvider.Uris.URI_RSS,
                cv,
                RssProvider.Columns._ID + "=" + position,
                null);
    }


}
