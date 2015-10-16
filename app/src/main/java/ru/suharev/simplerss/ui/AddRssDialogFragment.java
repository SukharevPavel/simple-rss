package ru.suharev.simplerss.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import ru.suharev.simplerss.R;
import ru.suharev.simplerss.utils.CurrentRss;
import ru.suharev.simplerss.utils.GetRssTask;
import ru.suharev.simplerss.utils.RssItem;

/**
 * Created by pasha on 06.10.2015.
 */
public class AddRssDialogFragment extends DialogFragment {

    private EditText mEditText;
    private GetRssListCallback mCallback;
    private GetRssFromDialogTask mGetRssTask;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mGetRssTask = new GetRssFromDialogTask();
        setRetainInstance(true);
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        mCallback = (GetRssListCallback) getActivity();
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
        if (mGetRssTask.getStatus() != AsyncTask.Status.RUNNING) mGetRssTask.execute(uri);
    }

    public void addNewRssEntry(String uri) {
        mCallback.onAddNewRss(uri);
    }


    class GetRssFromDialogTask extends GetRssTask{

        @Override
        protected void onPostExecute(List<RssItem> result){
            if (result != null) {
                mCallback.onGetRssList(result);
            } else
                mCallback.onFail();
        }

    }

    interface GetRssListCallback {

        void onGetRssList(List<RssItem> list);

        void onFail();

        void onAddNewRss(String rss);

    }


}
