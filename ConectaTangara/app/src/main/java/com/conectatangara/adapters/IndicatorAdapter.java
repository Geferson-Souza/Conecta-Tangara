package com.conectatangara.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
// import android.widget.ImageView; // Se for usar ImageView para gráficos estáticos

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatangara.R; // Importe sua classe R
import com.conectatangara.models.Indicator; // Importe seu modelo Indicator

// import com.github.mikephil.charting.charts.BarChart; // Exemplo se usar MPAndroidChart
// import com.github.mikephil.charting.data.BarData;
// import com.github.mikephil.charting.data.BarDataSet;
// import com.github.mikephil.charting.data.BarEntry;
// import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.List;

public class IndicatorAdapter extends RecyclerView.Adapter<IndicatorAdapter.IndicatorViewHolder> {

    private Context context;
    private List<Indicator> indicatorList;
    private OnIndicatorClickListener listener;

    // Interface para lidar com cliques no item (opcional, mas útil)
    public interface OnIndicatorClickListener {
        void onIndicatorClick(Indicator indicator);
    }

    public IndicatorAdapter(Context context, List<Indicator> indicatorList, OnIndicatorClickListener listener) {
        this.context = context;
        this.indicatorList = indicatorList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IndicatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_indicator_with_chart, parent, false);
        return new IndicatorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IndicatorViewHolder holder, int position) {
        Indicator indicator = indicatorList.get(position);
        holder.bind(indicator, listener);
    }

    @Override
    public int getItemCount() {
        return indicatorList.size();
    }

    static class IndicatorViewHolder extends RecyclerView.ViewHolder {
        TextView tvIndicatorName;
        TextView tvIndicatorValue;
        TextView tvIndicatorSourcePeriod;
        FrameLayout chartContainer;
        // ImageView ivChartPlaceholder; // Se você decidir usar uma ImageView para um gráfico estático
        // BarChart barChart; // Exemplo se usar MPAndroidChart

        public IndicatorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIndicatorName = itemView.findViewById(R.id.tv_indicator_item_name);
            tvIndicatorValue = itemView.findViewById(R.id.tv_indicator_item_value);
            tvIndicatorSourcePeriod = itemView.findViewById(R.id.tv_indicator_item_source_period);
            chartContainer = itemView.findViewById(R.id.chart_container_indicator_item);
            // ivChartPlaceholder = itemView.findViewById(R.id.iv_chart_placeholder_image); // Se adicionou uma ImageView
            // barChart = itemView.findViewById(R.id.bar_chart_indicator); // Se adicionou um BarChart no XML
        }

        void bind(final Indicator indicator, final OnIndicatorClickListener listener) {
            tvIndicatorName.setText(indicator.getName());
            tvIndicatorValue.setText(indicator.getValue());

            String sourcePeriodText = "";
            if (indicator.getSource() != null && !indicator.getSource().isEmpty()) {
                sourcePeriodText += "Fonte: " + indicator.getSource();
            }
            if (indicator.getPeriod() != null && !indicator.getPeriod().isEmpty()) {
                if (!sourcePeriodText.isEmpty()) {
                    sourcePeriodText += " | ";
                }
                sourcePeriodText += "Período: " + indicator.getPeriod();
            }
            tvIndicatorSourcePeriod.setText(sourcePeriodText);
            tvIndicatorSourcePeriod.setVisibility(sourcePeriodText.isEmpty() ? View.GONE : View.VISIBLE);


            // Lógica para configurar o gráfico (placeholder por enquanto)
            // Você precisará adicionar uma biblioteca de gráficos como MPAndroidChart ou GraphView
            // e então configurar o gráfico aqui com base em indicator.getChartType() e indicator.getChartData()

            // Exemplo MUITO básico de como você poderia começar com MPAndroidChart (requer configuração da lib):
            /*
            if ("BAR".equals(indicator.getChartType()) && indicator.getChartData() instanceof List) {
                // Supondo que chartData seja uma List<BarEntry> para este exemplo
                List<BarEntry> entries = (List<BarEntry>) indicator.getChartData();
                if (entries != null && !entries.isEmpty()) {
                    BarDataSet dataSet = new BarDataSet(entries, "Label do Dataset");
                    //dataSet.setColors(ColorTemplate.MATERIAL_COLORS); // Cores de exemplo
                    dataSet.setValueTextColor(ContextCompat.getColor(itemView.getContext(), R.color.berkeley_blue));
                    dataSet.setValueTextSize(10f);

                    BarData barData = new BarData(dataSet);
                    barChart.setData(barData);
                    barChart.getDescription().setEnabled(false);
                    barChart.setFitBars(true); // Faz as barras preencherem o espaço
                    barChart.invalidate(); // Atualiza o gráfico
                    barChart.setVisibility(View.VISIBLE);
                    chartContainer.setVisibility(View.GONE); // Esconde o placeholder de texto
                } else {
                     barChart.setVisibility(View.GONE);
                     chartContainer.setVisibility(View.VISIBLE); // Mostra o placeholder de texto
                }
            } else {
                // Se não for BAR ou não tiver dados, esconde o gráfico e mostra o placeholder
                // barChart.setVisibility(View.GONE);
                chartContainer.setVisibility(View.VISIBLE);
            }
            */


            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onIndicatorClick(indicator);
                }
            });
        }
    }

    // Método para atualizar a lista de indicadores
    public void setIndicators(List<Indicator> newIndicators) {
        this.indicatorList = newIndicators;
        notifyDataSetChanged(); // Para uma melhor performance, use DiffUtil aqui
    }
}
