package app.com.example.althomas04.topnews;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by al.thomas04 on 8/16/2016.
 */
public class NewsAdapter extends ArrayAdapter {

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context      The current context. Used to inflate the layout file.
     * @param NewsDataList A List of News data objects to display in a list
     */
    public NewsAdapter(Activity context, ArrayList<NewsData> NewsDataList) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for multiple Views, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, NewsDataList);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being resused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Get the {@link word} object located at this position in the list
        NewsData currentNewsData = (NewsData) getItem(position);

        // Find the TextView in the list_item.xml layout with the ID article_title_text_view
        TextView articleTitleTextView = (TextView) listItemView.findViewById(R.id.article_title_text_view);
        // Get the title string from the CurrentNewsData and set the title string text on the articleTitleTextView
        String articleTitle = currentNewsData.getTitle();
        articleTitleTextView.setText(articleTitle);

        // Find the TextView in the list_item.xml layout with the ID publisher_text_view
        TextView publisherTextView = (TextView) listItemView.findViewById(R.id.publisher_text_view);
        // Get the locationOffset from the current news data object and set this text on the publisher_text_view
        String publisher = currentNewsData.getPublisher();
        publisherTextView.setText(publisher);

        // Find the TextView in the list_item.xml layout with the ID time_text_view
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time_text_view);
        // Get the time from the current location object and set this text on the time_text_view
        String time = currentNewsData.getTime();
        timeTextView.setText(time);

        ImageView articleImageView = (ImageView) listItemView.findViewById(R.id.article_image_view);
        Bitmap articleImage = currentNewsData.getImageBitmap();
        articleImageView.setImageBitmap(articleImage);

        // Return the whole list item layout so that it can be shown in the ListView
        return listItemView;
    }

}
