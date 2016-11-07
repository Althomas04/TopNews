package app.com.example.althomas04.topnews;

/**
 * Created by al.thomas04 on 8/16/2016.
 */

//Customized class that enables the creation of a custom array, which is populated using the News API JSON.
public class NewsData {

    private String mTitle;
    private String mUrl;
    private String mImageUrl;
    private String mTime;
    private String mAuthor;
    private String mPublisher;
    private String mDescription;

    public NewsData(String title, String url, String imageUrl, String author, String time, String publisher, String description) {
        mTitle = title;
        mUrl = url;
        mImageUrl = imageUrl;
        mTime = time;
        mAuthor = author;
        mPublisher = publisher;
        mDescription = description;
    }

    public String getTime() {
        return mTime;
    }

    public String getImageUrl() {
        return mImageUrl;
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

    public String getPublisher() {
        return mPublisher;
    }

    public String getDescription() {
        return mDescription;
    }
}
