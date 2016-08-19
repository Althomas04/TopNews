package app.com.example.althomas04.topnews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by al.thomas04 on 8/16/2016.
 */
public class NewsLoader extends AsyncTaskLoader<List<NewsData>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = NewsLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     * //@param url     to load data from
     */
    public NewsLoader(Context context, String baseUrl, String sourceParam, String apiKeyParam) {
        super(context);
        mUrl = baseUrl + "source=" + sourceParam + "&apiKey=" + apiKeyParam;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsData> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of news articles.
        List<NewsData> newsArticles = QueryUtils.fetchNewsData(mUrl);
        return newsArticles;
    }
}
