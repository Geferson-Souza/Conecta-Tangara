package com.conectatangara.fragments;

import android.os.Build; // Adicionado para a verificação de versão da API
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
// import androidx.appcompat.app.AppCompatActivity; // Não mais necessário para setSupportActionBar aqui
// import androidx.appcompat.widget.Toolbar; // Não mais necessário
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatangara.R;
import com.conectatangara.activities.MainActivity; // Import para poder chamar o método da MainActivity
import com.conectatangara.adapters.IndicatorAdapter;
import com.conectatangara.models.Indicator;

import java.util.ArrayList;
// import java.util.Arrays; // Não usado diretamente se os arrays são definidos no fragmento
import java.util.List;
import java.util.stream.Collectors;

public class IndicatorsFragment extends Fragment implements IndicatorAdapter.OnIndicatorClickListener {

    // private Toolbar toolbar; // REMOVIDO
    private Spinner spinnerBairro;
    private Spinner spinnerTema;
    private Spinner spinnerPeriodo;
    private RecyclerView rvIndicators;
    private TextView tvEmptyIndicatorsList;
    private IndicatorAdapter indicatorAdapter;
    private List<Indicator> allIndicatorsList;
    private List<Indicator> filteredIndicatorsList;

    private final String[] bairros = {"Todos", "Centro", "Vila Alta", "Jardim Europa", "Parque Industrial"};
    private final String[] temas = {"Todos", "Saúde", "Educação", "Infraestrutura", "Segurança"};
    private final String[] periodos = {"Todos", "2024", "2023", "Últimos 6 meses"};

    public IndicatorsFragment() {
        // Construtor público vazio obrigatório
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_indicators, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar Views
        // toolbar = view.findViewById(R.id.toolbar); // REMOVIDO - ID não existe mais no layout do fragment
        spinnerBairro = view.findViewById(R.id.spinner_bairro);
        spinnerTema = view.findViewById(R.id.spinner_tema);
        spinnerPeriodo = view.findViewById(R.id.spinner_periodo);
        rvIndicators = view.findViewById(R.id.rv_indicators);
        tvEmptyIndicatorsList = view.findViewById(R.id.tv_empty_indicators_list);

        // Configurar Toolbar da MainActivity
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.toolbar_title_indicators)); // Define o título
        }
        // O bloco que chamava setSupportActionBar(toolbar) foi REMOVIDO

        allIndicatorsList = new ArrayList<>();
        filteredIndicatorsList = new ArrayList<>();

        indicatorAdapter = new IndicatorAdapter(getContext(), filteredIndicatorsList, this);
        rvIndicators.setLayoutManager(new LinearLayoutManager(getContext()));
        rvIndicators.setAdapter(indicatorAdapter);

        setupSpinners();
        loadSampleIndicators(); // Carrega dados de exemplo
        applyFilters(); // Aplica filtros iniciais
    }

    private void setupSpinners() {
        ArrayAdapter<String> bairroAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, bairros);
        bairroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBairro.setAdapter(bairroAdapter);

        ArrayAdapter<String> temaAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, temas);
        temaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTema.setAdapter(temaAdapter);

        ArrayAdapter<String> periodoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, periodos);
        periodoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriodo.setAdapter(periodoAdapter);

        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Não faz nada
            }
        };

        spinnerBairro.setOnItemSelectedListener(filterListener);
        spinnerTema.setOnItemSelectedListener(filterListener);
        spinnerPeriodo.setOnItemSelectedListener(filterListener);
    }

    private void loadSampleIndicators() {
        allIndicatorsList.clear();
        // Certifique-se que o construtor de Indicator corresponde aos campos (id, name, value, source, period, bairro, tema)
        allIndicatorsList.add(new Indicator("1", "Leitos UTI Ocupados", "75%", "Secretaria de Saúde", "Maio/2025", "Centro", "Saúde"));
        allIndicatorsList.add(new Indicator("2", "Alunos Matriculados (Fundamental)", "12.500", "Secretaria de Educação", "2024", "Todos", "Educação"));
        allIndicatorsList.add(new Indicator("3", "Ruas Pavimentadas Recentemente", "5 km", "Secretaria de Obras", "Últimos 6 meses", "Vila Alta", "Infraestrutura"));
        allIndicatorsList.add(new Indicator("4", "Índice de Criminalidade", "Redução de 10%", "Secretaria de Segurança", "2024 vs 2023", "Todos", "Segurança"));
        allIndicatorsList.add(new Indicator("5", "Satisfação com Coleta de Lixo", "8.2/10", "Pesquisa Interna", "Abril/2025", "Jardim Europa", "Infraestrutura"));
        allIndicatorsList.add(new Indicator("6", "Novas Empresas Abertas", "35", "Junta Comercial", "2024", "Todos", "Infraestrutura"));
    }

    private void applyFilters() {
        // Adicione verificação para getContext() para evitar NPE se o fragmento for desanexado rapidamente
        if (getContext() == null || spinnerBairro == null || spinnerTema == null || spinnerPeriodo == null) {
            return;
        }

        String selectedBairro = spinnerBairro.getSelectedItem().toString();
        String selectedTema = spinnerTema.getSelectedItem().toString();
        String selectedPeriodo = spinnerPeriodo.getSelectedItem().toString();

        filteredIndicatorsList.clear();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            filteredIndicatorsList.addAll(allIndicatorsList.stream()
                    .filter(indicator -> ("Todos".equals(selectedBairro) || (indicator.getBairro() != null && indicator.getBairro().equals(selectedBairro)) ))
                    .filter(indicator -> ("Todos".equals(selectedTema) || (indicator.getTema() != null && indicator.getTema().equals(selectedTema)) ))
                    .filter(indicator -> ("Todos".equals(selectedPeriodo) || (indicator.getPeriod() != null && indicator.getPeriod().contains(selectedPeriodo)) /* Adicionar lógica de data para "Últimos 6 meses" */ ))
                    .collect(Collectors.toList()));
        } else {
            for (Indicator indicator : allIndicatorsList) {
                boolean bairroMatch = "Todos".equals(selectedBairro) || (indicator.getBairro() != null && indicator.getBairro().equals(selectedBairro));
                boolean temaMatch = "Todos".equals(selectedTema) || (indicator.getTema() != null && indicator.getTema().equals(selectedTema));
                boolean periodoMatch = "Todos".equals(selectedPeriodo) || (indicator.getPeriod() != null && indicator.getPeriod().contains(selectedPeriodo));
                // Adicionar lógica para "Últimos 6 meses" aqui também se necessário para APIs < 24

                if (bairroMatch && temaMatch && periodoMatch) {
                    filteredIndicatorsList.add(indicator);
                }
            }
        }

        if (indicatorAdapter != null) { // Adicionar verificação de null para o adapter
            indicatorAdapter.setIndicators(filteredIndicatorsList);
        }
        checkIfListIsEmpty();
    }

    private void checkIfListIsEmpty() {
        if (filteredIndicatorsList.isEmpty()) {
            if (rvIndicators != null) rvIndicators.setVisibility(View.GONE);
            if (tvEmptyIndicatorsList != null) tvEmptyIndicatorsList.setVisibility(View.VISIBLE);
        } else {
            if (rvIndicators != null) rvIndicators.setVisibility(View.VISIBLE);
            if (tvEmptyIndicatorsList != null) tvEmptyIndicatorsList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onIndicatorClick(Indicator indicator) {
        if (getContext() != null) {
            Toast.makeText(getContext(), "Indicador Clicado: " + indicator.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Garante que o título da Toolbar da MainActivity seja atualizado quando este fragmento ficar visível
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.toolbar_title_indicators));
        }
    }
}

