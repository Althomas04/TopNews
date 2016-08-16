package app.com.example.althomas04.topnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsData>> {

    /** URL for news data from the newsapi dataset */
    /**
     * JSON response for a newsapi query
     */
    private static final String NEWSAPI_REQUEST_URL = "https://newsapi.org/v1/articles?source=techcrunch&sortBy=latest&apiKey=1c6bfb07da424863b2d1585048088a67";
    /**
     * Adapter for the list of news articles
     */
    private NewsAdapter mAdapter;

    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    /**
     * Progress bar that is displayed while the list is being populated
     */
    private ProgressBar mLoadingSpinnerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ProgressBar} in the layout
        mLoadingSpinnerView = (ProgressBar) findViewById(R.id.loading_spinner);

        // Find a reference to the {@link TextView} in the layout
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_state_Text_View);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            //If network connection does not exist
            //Set the loading spinner to disappear and set empty state to read "No Internet Connection"
            mLoadingSpinnerView.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        // Create a empty list of News info.
        ArrayList<NewsData> newsArticlesArrayList = new ArrayList<NewsData>();

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list_view);

        // Create a new {@link ArrayAdapter} of news articles
        mAdapter = new NewsAdapter(this, newsArticlesArrayList);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        // Set the OnItemClickListener on the adapter view, so when a user clicks on an news article view
        //they are redirected to the USGS website with more info on that particular news article
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current news article that was clicked on
                NewsData currentNewsArticle = (NewsData) mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsArticleUri = Uri.parse(currentNewsArticle.getUrl());

                // Create a new intent to view the news article URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsArticleUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Set the empty view on to the screen if news info list is empty.
        newsListView.setEmptyView(mEmptyStateTextView);

    }

    @Override
    public Loader<List<NewsData>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new NewsLoader(this, NEWSAPI_REQUEST_URL);
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

