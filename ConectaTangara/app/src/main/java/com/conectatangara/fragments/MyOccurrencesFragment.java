package com.conectatangara.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatangara.R;
import com.conectatangara.activities.MainActivity;
import com.conectatangara.adapters.MyOccurrenceAdapter;
import com.conectatangara.models.Ocorrencia; // Certifique-se que o modelo está correto

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit; // Para os dados de exemplo com TimeUnit

public class MyOccurrencesFragment extends Fragment implements MyOccurrenceAdapter.OnOccurrenceClickListener {

    private RecyclerView recyclerViewMyOccurrences;
    private MyOccurrenceAdapter adapter;
    private List<Ocorrencia> occurrenceList;
    private TextView tvEmptyList;

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

        recyclerViewMyOccurrences = view.findViewById(R.id.recyclerView_my_occurrences);
        tvEmptyList = view.findViewById(R.id.tv_empty_my_occurrences);

        occurrenceList = new ArrayList<>();
        // Certifique-se que o contexto não é nulo
        if (getContext() != null) {
            adapter = new MyOccurrenceAdapter(getContext(), occurrenceList, this);
            recyclerViewMyOccurrences.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewMyOccurrences.setAdapter(adapter);
        }

        loadMyOccurrences();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.toolbar_title_my_occurrences));
        }
    }

    private void loadMyOccurrences() {
        // --- DADOS DE EXEMPLO (Remover quando integrar com Firestore) ---
        // Agora usando o construtor vazio e setters para alinhar com o DatabaseSeeder e Firestore
        occurrenceList.clear();

        Ocorrencia oc1 = new Ocorrencia();
        oc1.setId("1");
        oc1.setTitulo("Buraco enorme na rua principal");
        oc1.setDescricao("Um buraco perigoso apareceu perto da escola...");
        oc1.setCategoria("Buraco na Via");
        oc1.setStatus("Recebida");
        oc1.setDataRegistro(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2)));
        // oc1.setLocalizacao(new GeoPoint(-14.62, -57.49)); // Adicionar se necessário para exibição
        occurrenceList.add(oc1);

        Ocorrencia oc2 = new Ocorrencia();
        oc2.setId("2");
        oc2.setTitulo("Poste sem luz há uma semana");
        oc2.setDescricao("O poste em frente ao número 123 está apagado.");
        oc2.setCategoria("Lâmpada Queimada/Defeito na Iluminação");
        oc2.setStatus("Em Análise");
        oc2.setDataRegistro(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)));
        occurrenceList.add(oc2);

        Ocorrencia oc3 = new Ocorrencia();
        oc3.setId("3");
        oc3.setTitulo("Lixo espalhado na praça");
        oc3.setDescricao("Muito lixo acumulado na praça central após o evento de domingo.");
        oc3.setCategoria("Lixo Acumulado/Problema na Coleta");
        oc3.setStatus("Em Andamento");
        oc3.setDataRegistro(new Date());
        occurrenceList.add(oc3);

        Ocorrencia oc4 = new Ocorrencia();
        oc4.setId("4");
        oc4.setTitulo("Vazamento de água limpa");
        oc4.setDescricao("Vazamento contínuo na calçada da Rua das Flores.");
        oc4.setCategoria("Vazamento de Água ou Esgoto");
        oc4.setStatus("Resolvida");
        oc4.setDataRegistro(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(5)));
        occurrenceList.add(oc4);

        Ocorrencia oc5 = new Ocorrencia();
        oc5.setId("5");
        oc5.setTitulo("Semáforo piscando amarelo");
        oc5.setDescricao("O semáforo do cruzamento X está apenas piscando.");
        oc5.setCategoria("Semáforo com Defeito/Problema de Sinalização");
        oc5.setStatus("Rejeitada");
        oc5.setDataRegistro(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(3)));
        occurrenceList.add(oc5);

        if (adapter != null) {
            adapter.setOccurrences(occurrenceList);
        }
        showEmptyListMessage(occurrenceList.isEmpty());
        // --- FIM DOS DADOS DE EXEMPLO ---
        // TODO: Implementar busca real do Firestore aqui
    }

    private void showEmptyListMessage(boolean show) {
        if (getContext() == null) return;
        if (show) {
            if (recyclerViewMyOccurrences != null) recyclerViewMyOccurrences.setVisibility(View.GONE);
            if (tvEmptyList != null) {
                tvEmptyList.setVisibility(View.VISIBLE);
                tvEmptyList.setText(R.string.my_occurrences_empty_list);
            }
        } else {
            if (recyclerViewMyOccurrences != null) recyclerViewMyOccurrences.setVisibility(View.VISIBLE);
            if (tvEmptyList != null) tvEmptyList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onOccurrenceClick(Ocorrencia ocorrencia) {
        if (getContext() == null) return;
        Toast.makeText(getContext(), "Clicou em: " + ocorrencia.getTitulo(), Toast.LENGTH_SHORT).show();
        // TODO: Implementar navegação para detalhes da ocorrência
    }
}
