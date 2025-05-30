package com.conectatangara.adapters;

       //**Observações no código do adapter:**
       // * Adicionei o `import com.conectatangara.R;` e `import com.conectatangara.interfaces.OnMediaRemoveListener;`.
       // * No `onBindViewHolder`, chamo `holder.bind(mediaUri);` sem a posição.
       //* No método `bind` do `ViewHolder`, o `OnClickListener` para `ibRemoveMedia` agora usa `getAdapterPosition()` para obter a posição correta do item no momento do clique, o que é mais seguro.
       //* Adicionei métodos `addMediaItem` e `removeMediaItem` para uma manipulação mais granular da lista, que é melhor para as animações do `RecyclerView`.

    import android.content.Context;
    import android.net.Uri;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageButton;
    import android.widget.ImageView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.bumptech.glide.Glide; // Biblioteca para carregar imagens
    import com.conectatangara.R; // Import para R.layout e R.id
    import com.conectatangara.interfaces.OnMediaRemoveListener; // Import da sua interface

    import java.util.ArrayList;

    public class MediaPreviewAdapter extends RecyclerView.Adapter<MediaPreviewAdapter.MediaViewHolder> {

        private Context context;
        private ArrayList<Uri> mediaUris;
        private OnMediaRemoveListener removeListener;

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
            holder.bind(mediaUri); // Removido 'position' daqui, pois getAdapterPosition é mais seguro no listener
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
                        int currentPosition = getAdapterPosition(); // Pega a posição atual no momento do clique
                        if (currentPosition != RecyclerView.NO_POSITION) {
                            removeListener.onMediaRemove(mediaUris.get(currentPosition), currentPosition);
                        }
                    }
                });
            }
        }

        // Método para atualizar a lista no adapter (se necessário de fora)
        // Pode não ser necessário se a Activity modificar a lista original e notificar o adapter
        public void updateMediaList(ArrayList<Uri> newMediaUris) {
            this.mediaUris.clear();
            this.mediaUris.addAll(newMediaUris);
            notifyDataSetChanged();
        }

        // Métodos para adicionar e remover itens de forma mais granular (melhor para animações)
        public void addMediaItem(Uri mediaUri) {
            mediaUris.add(mediaUri);
            notifyItemInserted(mediaUris.size() - 1);
        }

        public void removeMediaItem(int position) {
            if (position >= 0 && position < mediaUris.size()) {
                mediaUris.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mediaUris.size()); // Para atualizar as posições dos itens restantes
            }
        }
    }

