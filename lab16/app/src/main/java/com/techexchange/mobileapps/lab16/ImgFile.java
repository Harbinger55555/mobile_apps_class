package com.techexchange.mobileapps.lab16;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImgFile implements FetchUrlCheck {

    private final String TAG = this.getClass().getSimpleName();
    private static final String API_KEY = "638e21e6b6d009a453fe4eeb9cd546d6";
    private static final String QUERY = Uri.parse("https://api.flickr.com/services/rest")
            .buildUpon()
            .appendQueryParameter("method", "flickr.photos.getRecent")
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "url_s")
            .build()
            .toString();
    private final FetchUrl FetchUrl;
    private final ParseUrlCheck ParseUrlCheck;
    private List<URL> photosURLs;

    public ImgFile(ParseUrlCheck ParseUrlCheck) {
        this.ParseUrlCheck = ParseUrlCheck;
        this.FetchUrl = new FetchUrl(this);
    }

    public void getPhotosURLs() {
        this.FetchUrl.execute(QUERY);
    }

    @Override
    public void onFinish(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject photosObject = jsonResponse.getJSONObject("photos");
            JSONArray photosArray = photosObject.getJSONArray("photo");
            List<URL> photosURLs = new ArrayList<>();
            for (int i = 0; i < photosArray.length(); ++i) {
                JSONObject photoObject = photosArray.getJSONObject(i);
                if (photoObject.has("url_s")) {
                    String photoURLString = photoObject.getString("url_s");
                    URL photoURL = new URL(Uri.parse(photoURLString).toString());
                    photosURLs.add(photoURL);
                }
            }
            this.photosURLs = photosURLs;

            ParseUrlCheck.onFinish(photosURLs);

        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        } catch (MalformedURLException e) {
            Log.d(TAG, e.getMessage());
        }
    }
}
