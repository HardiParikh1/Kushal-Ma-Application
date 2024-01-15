package com.example.nutritracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private final List<Video> videoList;
    private final OnVideoSelectedListener listener;

    public VideoAdapter(List<Video> videoList, OnVideoSelectedListener listener) {
        this.videoList = videoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.bind(video, listener);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public VideoViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tvVideoTitle);
        }

        public void bind(Video video, OnVideoSelectedListener listener) {
            titleTextView.setText(video.title);
            itemView.setOnClickListener(v -> listener.onVideoSelected(video));
        }
    }

    interface OnVideoSelectedListener {
        void onVideoSelected(Video video);
    }
}
