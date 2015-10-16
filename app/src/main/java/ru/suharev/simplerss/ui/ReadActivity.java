package ru.suharev.simplerss.ui;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import java.util.List;

import ru.suharev.simplerss.R;
import ru.suharev.simplerss.utils.CurrentRss;
import ru.suharev.simplerss.utils.NoRssException;
import ru.suharev.simplerss.utils.RssItem;

public class ReadActivity extends AppCompatActivity
        implements RssListFragment.NavigationDrawerCallbacks, AddRssDialogFragment.GetRssListCallback {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private RssListFragment mNavigationDrawerFragment;
    private AddRssDialogFragment mAddRssFragment;
    private SetRssNameDialogFragment mSetRssFragment;
    private RssFragment mRssFragment;
    private static String ADD_RSS_FRAGMENT_TAG = "add_rss_fragment_tag";
    private static String SET_RSS_FRAGMENT_TAG = "set_rss_fragment_tag";


    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        mRssFragment = (RssFragment) getSupportFragmentManager().findFragmentById(R.id.read_fragment);
        mNavigationDrawerFragment = (RssListFragment)
                getSupportFragmentManager().findFragmentById(R.id.rss_list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.rss_list,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                mToolbar);

        try {
            mRssFragment.setArray(CurrentRss.get());
        } catch (NoRssException e) {
            //листа нет, не делаем ничего
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
    }

    private void showAddRssDialog(){
        if (getSupportFragmentManager().findFragmentByTag(ADD_RSS_FRAGMENT_TAG) == null) {
            mAddRssFragment = new AddRssDialogFragment();
            mAddRssFragment.show(getSupportFragmentManager(), ADD_RSS_FRAGMENT_TAG);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.read, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_find_rss) {
            showAddRssDialog();
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onGetRssList(List<RssItem> list) {
        Log.i("ReadActivity", "Successfully get list");
        mRssFragment.setArray(list);
        CurrentRss.set(list);
    }

    @Override
    public void onFail() {
        Toast.makeText(this, getString(R.string.toast_error_get_rss), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddNewRss(String rss) {
        if (getSupportFragmentManager().findFragmentByTag(SET_RSS_FRAGMENT_TAG) == null) {
            mSetRssFragment = new SetRssNameDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(SetRssNameDialogFragment.EXTRA_RSS_URI, rss);
            mSetRssFragment.setArguments(bundle);
            mSetRssFragment.show(getSupportFragmentManager(), SET_RSS_FRAGMENT_TAG);
        }
    }
}
