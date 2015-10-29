package ru.suharev.simplerss.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ru.suharev.simplerss.R;
import ru.suharev.simplerss.utils.RssCallback;

/**
 * Dialog fragment for pulling rss and adding new rss entry to database
 */
public class AddRssDialogFragment extends DialogFragment {

    private EditText mEditText;
    private RssCallback mCallback;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        mCallback = (RssCallback) getActivity();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.fragment_add_rss, null);
        mEditText = (EditText) v.findViewById(R.id.add_rss_edit_text);
        return buildDialog(v);
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setOnDismissListener(null);
        super.onDestroyView();
    }

    public Dialog buildDialog(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(R.string.dialog_get_rss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String rss = String.valueOf(mEditText.getText());
                        getNewRssFeed(rss);
                    }
                })
                .setNeutralButton(R.string.dialog_add_rss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String rss = String.valueOf(mEditText.getText());
                        addNewRssEntry(rss);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null);
        return builder.create();
    }

    public void getNewRssFeed(String uri){
        mCallback.onGetRssRequest(uri);
    }

    public void addNewRssEntry(String uri) {
        mCallback.onAddNewRss(uri);
    }







}
