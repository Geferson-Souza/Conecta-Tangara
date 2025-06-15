package com.conectatangara.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatangara.R;
import com.conectatangara.models.Ocorrencia;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PublicOccurrenceAdapter extends RecyclerView.Adapter<PublicOccurrenceAdapter.ViewHolder> {

    private final Context context;
    private List<Ocorrencia> occurrenceList;
    private final OnOccurrenceClickListener clickListener;

    // Interface para comunicar eventos de clique para o Fragment
    public interface OnOccurrenceClickListener {
        void onOccurrenceClick(Ocorrencia ocorrencia);
        void onSupportClick(Ocorrencia ocorrencia);
    }

    public PublicOccurrenceAdapter(Context context, List<Ocorrencia> occurrenceList, OnOccurrenceClickListener listener) {
        this.context = context;
        this.occurrenceList = occurrenceList;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_occurrence, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ocorrencia ocorrencia = occurrenceList.get(position);

        holder.tvCategory.setText(ocorrencia.getCategoria());
        holder.tvTitle.setText(ocorrencia.getTitulo());
        holder.tvDescription.setText(ocorrencia.getDescricao());

        // Configura o status com cor
        holder.tvStatus.setText(ocorrencia.getStatus());
        holder.tvStatus.setBackground(ContextCompat.getDrawable(context, getStatusBackground(ocorrencia.getStatus())));

        // Formata a data
        if (ocorrencia.getDataRegistro() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            holder.tvDate.setText(sdf.format(ocorrencia.getDataRegistro()));
        }

        // Exibe o número de apoios
        holder.tvSupportCount.setText(String.valueOf(ocorrencia.getApoios()));

        // Listeners de clique
        holder.itemView.setOnClickListener(v -> clickListener.onOccurrenceClick(ocorrencia));
        holder.btnSupport.setOnClickListener(v -> clickListener.onSupportClick(ocorrencia));
    }

    @Override
    public int getItemCount() {
        return occurrenceList.size();
    }

    public void setOccurrences(List<Ocorrencia> occurrences) {
        this.occurrenceList = occurrences;
        notifyDataSetChanged();
    }

    // Método auxiliar para definir a cor de fundo do status
    private int getStatusBackground(String status) {
        if (status == null) return R.drawable.status_background_placeholder;
        switch (status) {
            case "Em Análise":
                return R.drawable.status_background_em_analise; // Crie este drawable
            case "Em Andamento":
                return R.drawable.status_background_em_andamento; // Crie este drawable
            case "Resolvida":
                return R.drawable.status_background_resolvida; // Crie este drawable
            default: // "Recebida" ou outros
                return R.drawable.status_background_recebida; // Crie este drawable
        }
    }

    // ViewHolder para manter as referências das views do item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvStatus, tvTitle, tvDescription, tvSupportCount, tvDate;
        Button btnSupport;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tv_item_occurrence_category);
            tvStatus = itemView.findViewById(R.id.tv_item_occurrence_status);
            tvTitle = itemView.findViewById(R.id.tv_item_occurrence_title);
            tvDescription = itemView.findViewById(R.id.tv_item_occurrence_description);
            tvSupportCount = itemView.findViewById(R.id.tv_item_support_count);
            tvDate = itemView.findViewById(R.id.tv_item_occurrence_date);
            btnSupport = itemView.findViewById(R.id.button_item_support);
        }
    }
}
