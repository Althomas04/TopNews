package app.com.example.althomas04.topnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsData>> {


    //URL  to obtain news data from the newsapi dataset. (Full URL initialized in NewsLoader Class).
    //newsApiUrl = BASE_URL + "source=" + sourceParam + "&apiKey=" + API_KEY_PARAM;
    private static final String BASE_URL = "https://newsapi.org/v1/articles?";
    private String sourceParam;
    private static final String API_KEY_PARAM = "1c6bfb07da424863b2d1585048088a67";

    //Adapter for the list of news articles
    private NewsAdapter mAdapter;

    //Constant value for the news loader ID. We can choose any integer.
    //This really only comes into play if you're using multiple loaders.
    private static final int NEWS_LOADER_ID = 0;

    //TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;

    //Progress bar that is displayed while the list is being populated
    private ProgressBar mLoadingSpinnerView;


    //Spinner view that displays a list of publisher sources

    private Spinner mSpinnerList;

    //Initiialized a counter variiable for the if statement in Spinner itemSelectListener
    private int currentId = 0;

    //Stores the url from article that is accessed through clickListener
    //Also used in WebViewActivity
    public static String newsArticleUrl;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ProgressBar} in the layout
        mLoadingSpinnerView = (ProgressBar) findViewById(R.id.loading_spinner);

        // Find a reference to the {@link TextView} in the layout
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_state_Text_View);


        // Find a reference to the {@link Spinner} in the layout
        mSpinnerList = (Spinner) findViewById(R.id.source_spinner);

        // Creating adapter for spinner
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this, R.array.source_arrays, R.layout.custom_spinner_item);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        // attaching data adapter to spinner
        mSpinnerList.setAdapter(dataAdapter);

        // Setting a listener to spinner to detect if an item has been clicked and to initialize/restart loader.
        mSpinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                // On selecting a publisher source, the source parameter is set to the new source.
                String newSourceParam = parent.getItemAtPosition(position).toString();
                newSourceParam = newSourceParam.toLowerCase();
                newSourceParam = newSourceParam.replaceAll("\\s", "");
                sourceParam = newSourceParam;

                // If a new source has been selected, the loader restarts
                // and updates the news articles using the new source parameter.
                int newId = (int) id;
                if (currentId != newId) {
                    currentId = newId;
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
                    //Set the loading spinner to reappear when reloading view.
                    mLoadingSpinnerView.setVisibility(View.VISIBLE);
                } else {
                    startLoader();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Create a empty list of News info.
        final ArrayList<NewsData> newsArticlesArrayList = new ArrayList<NewsData>();

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list_view);

        // Create a new {@link ArrayAdapter} of news articles
        mAdapter = new NewsAdapter(this, newsArticlesArrayList);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        // Set the OnItemClickListener on the adapter view, so when a user clicks on an news article view
        //they are redirected to the publisher website with more info on that particular news article
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current news article that was clicked on
                NewsData currentNewsArticle = (NewsData) mAdapter.getItem(position);

                // Get the String Url (to pass into the Intent constructor)
                newsArticleUrl = currentNewsArticle.getUrl();
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });

        // Set the empty view on to the screen if news info list is empty.
        newsListView.setEmptyView(mEmptyStateTextView);

    }


    private void startLoader() {

        //Checks network connection status
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, MainActivity.this);
        } else {
            //If network connection does not exist
            //Set the loading spinner to disappear and set empty state to read "No Internet Connection"
            mLoadingSpinnerView.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<NewsData>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL params
        return new NewsLoader(this, BASE_URL, sourceParam, API_KEY_PARAM);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsData>> loader, List<NewsData> newsArticles) {
        // Clear the adapter of previous news data
        mAdapter.clear();

        // If there is a valid list of {@link News Article}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsArticles != null && !newsArticles.isEmpty()) {
            mAdapter.addAll(newsArticles);
        }
        // Set empty state text to display "No newsArticles found."
        mEmptyStateTextView.setText(R.string.no_news);

        //Set the loading spinner to disappear after the loading has finished.
        mLoadingSpinnerView.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<NewsData>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}


