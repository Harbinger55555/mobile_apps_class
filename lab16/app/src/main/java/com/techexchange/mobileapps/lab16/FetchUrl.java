package com.techexchange.mobileapps.lab16;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchUrl extends AsyncTask<String, Void, String> {
    private final String TAG = this.getClass().getSimpleName();
    private final FetchUrlCheck fetchUrlCheck;

    public FetchUrl(FetchUrlCheck fetchUrlCheck) {
        this.fetchUrlCheck = fetchUrlCheck;
    }

    @Override
    protected String doInBackground(String... urlStrings) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlStrings[0]);

            connection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            for (int bytesRead; (bytesRead = inputStream.read(buffer)) > 0; ) {
                outputStream.write(buffer, 0, bytesRead);
            }

            byte[] responseBytes = outputStream.toByteArray();
            return new String(responseBytes);

        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        fetchUrlCheck.onFinish(s);
    }
}
