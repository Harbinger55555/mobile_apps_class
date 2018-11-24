package com.techexchange.mobileapps.lab16;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ParseUrlCheck {

    private List<ImgViewInfo> ImgViewInfoList;
    private RecyclerView imageRecyclerView;
    private ImgAdapter imgAdapter;
    private ImgFetcher<ImageView> thumbnailThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImgFile ImgFile = new ImgFile(this);
        ImgFile.getPhotosURLs();

        thumbnailThread = new ImgFetcher<>(new Handler());
        thumbnailThread.setListener(new ImgFetcher.Listener<ImageView>() {
            @Override
            public void onThumbnailDownloaded(ImageView imageView, Bitmap thumbnail) {
                imageView.setImageBitmap(thumbnail);
            }
        });
        thumbnailThread.start();
        thumbnailThread.getLooper();

        imageRecyclerView = findViewById(R.id.image_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        imageRecyclerView.setLayoutManager(gridLayoutManager);

    }

    @Override
    public void onFinish(List<URL> photosURLs) {
        List<ImgViewInfo> ImgViewInfoList = new ArrayList<>();
        for (URL url : photosURLs) {
            ImgViewInfoList.add(new ImgViewInfo(url));
        }
        this.ImgViewInfoList = ImgViewInfoList;
        this.imgAdapter = new ImgAdapter(this, ImgViewInfoList, thumbnailThread);
        imageRecyclerView.setAdapter(imgAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thumbnailThread.clearQueue();
        thumbnailThread.quit();
    }
}
