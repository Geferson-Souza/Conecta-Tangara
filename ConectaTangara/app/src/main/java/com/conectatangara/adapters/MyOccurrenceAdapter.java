package com.conectatangara.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatangara.R;
import com.conectatangara.models.Ocorrencia;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MyOccurrenceAdapter extends RecyclerView.Adapter<MyOccurrenceAdapter.OccurrenceViewHolder> {

    private Context context;
    private List<Ocorrencia> occurrenceList;
    private OnOccurrenceClickListener listener; // Interface para cliques no item

    // Interface para lidar com cliques no item (opcional, mas útil)
    public interface OnOccurrenceClickListener {
        void onOccurrenceClick(Ocorrencia ocorrencia);
    }

    public MyOccurrenceAdapter(Context context, List<Ocorrencia> occurrenceList, OnOccurrenceClickListener listener) {
        this.context = context;
        this.occurrenceList = occurrenceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OccurrenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_my_occurrence, parent, false);
        return new OccurrenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OccurrenceViewHolder holder, int position) {
        Ocorrencia ocorrencia = occurrenceList.get(position);
        holder.bind(ocorrencia, listener);
    }

    @Override
    public int getItemCount() {
        return occurrenceList.size();
    }

    static class OccurrenceViewHolder extends RecyclerView.ViewHolder {
        View viewStatusIndicator;
        TextView tvCategory;
        TextView tvDate;
        TextView tvTitle;
        TextView tvDescriptionSnippet;

        public OccurrenceViewHolder(@NonNull View itemView) {
            super(itemView);
            viewStatusIndicator = itemView.findViewById(R.id.view_status_indicator);
            tvCategory = itemView.findViewById(R.id.tv_occurrence_category);
            tvDate = itemView.findViewById(R.id.tv_occurrence_date);
            tvTitle = itemView.findViewById(R.id.tv_occurrence_title);
            tvDescriptionSnippet = itemView.findViewById(R.id.tv_occurrence_description_snippet);
        }

        void bind(final Ocorrencia ocorrencia, final OnOccurrenceClickListener listener) {
            tvCategory.setText(ocorrencia.getCategoria());
            if (ocorrencia.getDataRegistro() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                tvDate.setText(sdf.format(ocorrencia.getDataRegistro()));
            } else {
                tvDate.setText(R.string.occurrence_date_placeholder);
            }
            tvTitle.setText(ocorrencia.getTitulo());
            tvDescriptionSnippet.setText(ocorrencia.getDescricao()); // Pode querer um snippet menor aqui

            // Define a cor do indicador de status
            int statusColor = R.color.non_photo_blue; // Cor padrão
            if (ocorrencia.getStatus() != null) {
                switch (ocorrencia.getStatus().toLowerCase()) { // Comparação case-insensitive
                    case "recebida": // Use as strings exatas que você salvará no Firestore
                        statusColor = R.color.non_photo_blue;
                        break;
                    case "em análise": // Ou "em analise"
                    case "em analise":
                        statusColor = R.color.cerulean;
                        break;
                    case "em andamento":
                        statusColor = R.color.status_em_andamento_yellow;
                        break;
                    case "resolvida":
                        statusColor = R.color.status_resolvida_green;
                        break;
                    case "rejeitada":
                        statusColor = R.color.red_pantone;
                        break;
                }
            }
            // Para a View, é melhor pegar o GradientDrawable e setar a cor
            // ou se for uma cor sólida, apenas setBackgroundColor.
            // Se viewStatusIndicator for um shape drawable com cantos, use GradientDrawable.
            // Se for uma View simples, setBackgroundColor é suficiente.
            viewStatusIndicator.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), statusColor));


            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onOccurrenceClick(ocorrencia);
                }
            });
        }
    }

    // Método para atualizar a lista
    public void setOccurrences(List<Ocorrencia> newOccurrences) {
        this.occurrenceList = newOccurrences;
        notifyDataSetChanged(); // Ou usar DiffUtil para melhor performance
    }
}
    