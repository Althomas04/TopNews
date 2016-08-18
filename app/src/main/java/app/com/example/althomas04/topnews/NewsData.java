package app.com.example.althomas04.topnews;

import android.graphics.Bitmap;

/**
 * Created by al.thomas04 on 8/16/2016.
 */
public class NewsData {

    private String mTitle;
    private String mUrl;
    private Bitmap mImageBitmap;
    private String mTime;
    private String mAuthor;

    public NewsData(String title, String url, Bitmap imageBitmap, String author, String time) {
        mTitle = title;
        mUrl = url;
        mImageBitmap = imageBitmap;
        mTime = time;
        mAuthor = author;
    }

    public String getTime() {
        return mTime;
    }

    public Bitmap getImageBitmap() {
        return mImageBitmap;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }
}
