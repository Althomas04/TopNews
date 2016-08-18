package app.com.example.althomas04.topnews;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by al.thomas04 on 8/16/2016.
 */
public class NewsAdapter extends ArrayAdapter {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = NewsAdapter.class.getName();

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
        // Get the title string from the CurrentNewsData and set the title string text on the articleTitleTextView
        TextView articleTitleTextView = (TextView) listItemView.findViewById(R.id.article_title_text_view);
        String articleTitle = currentNewsData.getTitle();
        articleTitleTextView.setText(articleTitle);

        // Find the TextView in the list_item.xml layout with the ID author_text_view
        // Get the author from the current news data object and set this text on the author_text_view
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_text_view);
        String author = currentNewsData.getAuthor();
        authorTextView.setText(author);

        // Create a new string object from the published timed date, and convert into date object.
        String timedDateObject = currentNewsData.getTime();
        Date convertedTimedDate = convertTimedDate(timedDateObject);

        // Find the TextView in the list_item.xml layout with the ID date_text_view
        // Format the date string (i.e. "Mar 3, 1984")
        // Set the formatted date string text on the dateTextView
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        String formattedDate = formatDate(convertedTimedDate);
        dateTextView.setText(formattedDate);

        // Find the TextView in the list_item.xml layout with the ID date_text_view
        // Format the time string (i.e. "4:30PM")
        // Set the formatted time string text on the timeTextView
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time_text_view);
        String formattedTime = formatTime(convertedTimedDate);
        timeTextView.setText(formattedTime);

        // Find the ImageView in the list_item.xml layout with the ID article_image_view
        // Get the bitmap image from the current news data object, scale it, and set this image on the ImageView
        ImageView articleImageView = (ImageView) listItemView.findViewById(R.id.article_image_view);
        Bitmap articleImage = currentNewsData.getImageBitmap();
        articleImage = Bitmap.createScaledBitmap(articleImage, 1500, 1000, true);
        articleImageView.setImageBitmap(articleImage);

        // Return the whole list item layout so that it can be shown in the ListView
        return listItemView;
    }

    /**
     * Convert Timed Date string into "MM/dd/yyyy'T'HH:mm:ss" format.
     */
    private Date convertTimedDate(String timedDateObject) {
        SimpleDateFormat timedDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date convertedTimedDate = new Date();
        try {
            convertedTimedDate = timedDateFormat.parse(timedDateObject);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return convertedTimedDate;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormatter.format(dateObject);
    }

    /**
     * Return the formatted time string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        return timeFormatter.format(dateObject);
    }

}