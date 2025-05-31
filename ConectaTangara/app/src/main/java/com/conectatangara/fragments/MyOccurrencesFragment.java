package com.conectatangara.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
// import androidx.appcompat.app.AppCompatActivity; // Não mais necessário para setSupportActionBar aqui
// import androidx.appcompat.widget.Toolbar; // Não mais necessário aqui
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatangara.R;
import com.conectatangara.activities.MainActivity; // Import para chamar o método da MainActivity
import com.conectatangara.adapters.MyOccurrenceAdapter;
import com.conectatangara.models.Ocorrencia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyOccurrencesFragment extends Fragment implements MyOccurrenceAdapter.OnOccurrenceClickListener {

    private RecyclerView recyclerViewMyOccurrences;
    private MyOccurrenceAdapter adapter;
    private List<Ocorrencia> occurrenceList;
    private TextView tvEmptyList;
    // private Toolbar toolbar; // REMOVIDO

    public MyOccurrencesFragment() {
        // Construtor público vazio obrigatório
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_occurrences, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // toolbar = view.findViewById(R.id.toolbar_my_occurrences); // REMOVIDO - ID não existe mais no layout do fragment
        // if (getActivity() instanceof AppCompatActivity) {
        //     ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar); // REMOVIDO
        // }

        recyclerViewMyOccurrences = view.findViewById(R.id.recyclerView_my_occurrences);
        tvEmptyList = view.findViewById(R.id.tv_empty_my_occurrences);

        occurrenceList = new ArrayList<>();
        adapter = new MyOccurrenceAdapter(getContext(), occurrenceList, this);

        recyclerViewMyOccurrences.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMyOccurrences.setAdapter(adapter);

        loadMyOccurrences();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Define o título na Toolbar da MainActivity quando este fragmento estiver visível
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.toolbar_title_my_occurrences));
        }
    }

    private void loadMyOccurrences() {
        // --- DADOS DE EXEMPLO (Remover quando integrar com Firestore) ---
        occurrenceList.clear();
        occurrenceList.add(new Ocorrencia("1", "Buraco enorme na rua principal", "Um buraco perigoso apareceu perto da escola...", "Buraco na Via", "Recebida", new Date(System.currentTimeMillis() - 86400000 * 2)));
        occurrenceList.add(new Ocorrencia("2", "Poste sem luz há uma semana", "O poste em frente ao número 123 está apagado.", "Lâmpada Queimada/Defeito na Iluminação", "Em Análise", new Date(System.currentTimeMillis() - 86400000)));
        occurrenceList.add(new Ocorrencia("3", "Lixo espalhado na praça", "Muito lixo acumulado na praça central após o evento de domingo.", "Lixo Acumulado/Problema na Coleta", "Em Andamento", new Date()));
        occurrenceList.add(new Ocorrencia("4", "Vazamento de água limpa", "Vazamento contínuo na calçada da Rua das Flores.", "Vazamento de Água ou Esgoto", "Resolvida", new Date(System.currentTimeMillis() - 86400000 * 5)));
        occurrenceList.add(new Ocorrencia("5", "Semáforo piscando amarelo", "O semáforo do cruzamento X está apenas piscando.", "Semáforo com Defeito/Problema de Sinalização", "Rejeitada", new Date(System.currentTimeMillis() - 86400000 * 3)));
        adapter.setOccurrences(occurrenceList);
        showEmptyListMessage(occurrenceList.isEmpty());
        // --- FIM DOS DADOS DE EXEMPLO ---
        // TODO: Implementar busca real do Firestore aqui
    }

    private void showEmptyListMessage(boolean show) {
        if (getContext() == null) return; // Evita crash se o fragmento for desanexado
        if (show) {
            recyclerViewMyOccurrences.setVisibility(View.GONE);
            tvEmptyList.setVisibility(View.VISIBLE);
            tvEmptyList.setText(R.string.my_occurrences_empty_list);
        } else {
            recyclerViewMyOccurrences.setVisibility(View.VISIBLE);
            tvEmptyList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onOccurrenceClick(Ocorrencia ocorrencia) {
        if (getContext() == null) return;
        Toast.makeText(getContext(), "Clicou em: " + ocorrencia.getTitulo(), Toast.LENGTH_SHORT).show();
        // TODO: Implementar navegação para detalhes da ocorrência
    }
}
