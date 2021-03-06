package app.com.example.althomas04.topnews;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
        // Check if the existing view is being reused, otherwise inflate the view
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Get the {@link word} object located at this position in the list
        NewsData currentNewsData = (NewsData) getItem(position);

        // Find the TextView in the list_item.xml layout with the ID article_title_text_view
        // Get the title string from the CurrentNewsData and set the title string text on the articleTitleTextView
        TextView articleTitleTextView = (TextView) listItem.findViewById(R.id.article_title_text_view);
        String articleTitle = currentNewsData.getTitle();
        articleTitleTextView.setText(articleTitle);

        // Find the TextView in the list_item.xml layout with the ID author_text_view
        // Get the author from the current news data object and set this text on the author_text_view
        // only IF author is not set as "null" or a http link, if it is, the publisher will be displayed instead.
        TextView authorTextView = (TextView) listItem.findViewById(R.id.author_text_view);
        String author = currentNewsData.getAuthor();
        String publisher = currentNewsData.getPublisher();
        if (author != "null" && !author.contains("http")) {
            authorTextView.setText(author);
        } else {
            authorTextView.setText(publisher.toUpperCase());
        }
        // Create a new string object from the published timed date, and convert into date object.
        String timedDateObject = currentNewsData.getTime();
        Date convertedTimedDate = convertTimedDate(timedDateObject);

        // Find the TextView in the list_item.xml layout with the ID date_text_view
        // Format the date string (i.e. "Mar 3, 1984")
        // Set the formatted date string text on the dateTextView
        TextView dateTextView = (TextView) listItem.findViewById(R.id.date_text_view);
        String formattedDate = formatDate(convertedTimedDate);
        dateTextView.setText(formattedDate);

        // Find the TextView in the list_item.xml layout with the ID date_text_view
        // Format the time string (i.e. "4:30PM")
        // Set the formatted time string text on the timeTextView
        TextView timeTextView = (TextView) listItem.findViewById(R.id.time_text_view);
        String formattedTime = formatTime(convertedTimedDate);
        timeTextView.setText(formattedTime);

        // Find the TextView in the list_item.xml layout with the ID article_description_text_view
        // Get the description string from the current news data object, and display it
        // only IF news data object does not contain an article image. (Checked in if statement).
        TextView descriptionTextView = (TextView) listItem.findViewById(R.id.article_description_text_view);
        String articleDescription = currentNewsData.getDescription();
        descriptionTextView.setText(articleDescription);

        // Find the ImageView in the list_item.xml layout with the ID article_image_view
        // Get the image using picasso library, scale it, and set this image on the ImageView
        // only IF news data object contains a vaild image. (Checked in if statement).
        SimpleDraweeView draweeView = (SimpleDraweeView) listItem.findViewById(R.id.article_image_view);
        String articleImageUrl = currentNewsData.getImageUrl();

        //Checks if image is valid (not null) and the orientation mode.
        //Description view is only visible when image is not valid or if the orientation is in landscape.
        if (articleImageUrl != "null") {
            Uri uri = Uri.parse(articleImageUrl);
            draweeView.setImageURI(uri);
            draweeView.setVisibility(View.VISIBLE);
            if (getContext().getResources().getBoolean(R.bool.is_portrait)) {
                descriptionTextView.setVisibility(View.GONE);
            } else {
                descriptionTextView.setVisibility(View.VISIBLE);
            }
        } else {
            draweeView.setVisibility(View.GONE);
            descriptionTextView.setVisibility(View.VISIBLE);
        }

        // Return the whole list item layout so that it can be shown in the ListView
        return listItem;
    }

    /**
     * Establish Timed Date string into "MM/dd/yyyy'T'HH:mm:ss" format.
     */
    private Date convertTimedDate(String timedDateObject) {
        SimpleDateFormat timedDateFormat;
        if (timedDateObject.length() == 20) {
            timedDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        } else {
            timedDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.US);
        }
        timedDateFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
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
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        return dateFormatter.format(dateObject);
    }

    /**
     * Return the formatted time string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a", Locale.US);
        return timeFormatter.format(dateObject);
    }

}
