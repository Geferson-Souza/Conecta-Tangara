package com.conectatangara.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.conectatangara.R;
import com.conectatangara.interfaces.OnMediaRemoveListener;

import java.util.ArrayList;

public class MediaPreviewAdapter extends RecyclerView.Adapter<MediaPreviewAdapter.MediaViewHolder> {

    private final Context context;
    private final ArrayList<Uri> mediaUris;
    private final OnMediaRemoveListener removeListener;

    public MediaPreviewAdapter(Context context, ArrayList<Uri> mediaUris, OnMediaRemoveListener removeListener) {
        this.context = context;
        this.mediaUris = mediaUris;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_media_preview_placeholder, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        Uri mediaUri = mediaUris.get(position);
        holder.bind(mediaUri);
    }

    @Override
    public int getItemCount() {
        return mediaUris.size();
    }

    class MediaViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMediaPreview;
        ImageButton ibRemoveMedia;

        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMediaPreview = itemView.findViewById(R.id.iv_media_preview);
            ibRemoveMedia = itemView.findViewById(R.id.ib_remove_media);
        }

        void bind(final Uri mediaUri) {
            Glide.with(context)
                    .load(mediaUri)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_placeholder)
                    .centerCrop()
                    .into(ivMediaPreview);

            ibRemoveMedia.setVisibility(View.VISIBLE);
            ibRemoveMedia.setOnClickListener(v -> {
                if (removeListener != null) {
                    int currentPosition = getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        removeListener.onMediaRemove(mediaUris.get(currentPosition), currentPosition);
                    }
                }
            });
        }
    }

    public void addMediaItem(Uri mediaUri) {
        mediaUris.add(mediaUri);
        notifyItemInserted(mediaUris.size() - 1);
    }
}
