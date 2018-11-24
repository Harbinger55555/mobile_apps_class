package com.techexchange.mobileapps.lab16;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<ImgViewInfo> ImgViewInfoList;
    private final Context context;
    private final ImgFetcher<ImageView> thumbnailThread;

    public ImgAdapter(Context context, List<ImgViewInfo> ImgViewInfoList, ImgFetcher<ImageView> thumbnailThread) {
        this.ImgViewInfoList = ImgViewInfoList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.thumbnailThread = thumbnailThread;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View itemView = layoutInflater.inflate(R.layout.fetched_img, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImgViewInfo ImgViewInfo = ImgViewInfoList.get(position);
        holder.imageView.setImageBitmap(ImgViewInfo.bitmap);
        holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_launcher_foreground));

        thumbnailThread.queueThumbnail(holder.imageView, ImgViewInfoList.get(position).url);

    }

    @Override
    public int getItemCount() {
        return ImgViewInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.fetched_img);
        }
    }
}
