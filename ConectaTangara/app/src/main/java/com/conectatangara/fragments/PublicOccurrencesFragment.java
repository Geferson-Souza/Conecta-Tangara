package com.conectatangara.fragments;

import android.content.Intent;
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
import com.conectatangara.activities.OccurrenceDetailActivity;
import com.conectatangara.adapters.PublicOccurrenceAdapter;
import com.conectatangara.models.Ocorrencia;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PublicOccurrencesFragment extends Fragment implements PublicOccurrenceAdapter.OnOccurrenceClickListener {

    private RecyclerView recyclerView;
    private PublicOccurrenceAdapter adapter;
    private List<Ocorrencia> occurrenceList;
    private TextView tvEmptyList;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Use o layout que você renomeou. Se não renomeou, crie um novo com este nome.
        return inflater.inflate(R.layout.fragment_public_occurrences, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView_public_occurrences); // Use o ID do seu layout
        tvEmptyList = view.findViewById(R.id.tv_empty_public_occurrences); // Use o ID do seu layout

        occurrenceList = new ArrayList<>();
        if (getContext() != null) {
            adapter = new PublicOccurrenceAdapter(getContext(), occurrenceList, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }

        loadPublicOccurrences();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            // Defina um título apropriado para a tela de ocorrências públicas
            ((MainActivity) getActivity()).setToolbarTitle("Ocorrências Públicas");
        }
    }

    private void loadPublicOccurrences() {
        db.collection("ocorrencias")
                .orderBy("dataRegistro", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        occurrenceList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Ocorrencia ocorrencia = document.toObject(Ocorrencia.class);
                            ocorrencia.setId(document.getId());
                            occurrenceList.add(ocorrencia);
                        }
                        adapter.setOccurrences(occurrenceList);
                        showEmptyListMessage(occurrenceList.isEmpty());
                    } else {
                        Toast.makeText(getContext(), "Erro ao carregar ocorrências.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showEmptyListMessage(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.GONE);
            tvEmptyList.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmptyList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onOccurrenceClick(Ocorrencia ocorrencia) {
        // Abre a tela de detalhes da ocorrência
        Intent intent = new Intent(getActivity(), OccurrenceDetailActivity.class);
        intent.putExtra("OCCURRENCE_ID", ocorrencia.getId());
        startActivity(intent);
    }

    @Override
    public void onSupportClick(Ocorrencia ocorrencia) {
        // Incrementa o contador de "apoios" no Firestore
        DocumentReference occurrenceRef = db.collection("ocorrencias").document(ocorrencia.getId());
        occurrenceRef.update("apoios", FieldValue.increment(1))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Ocorrência apoiada!", Toast.LENGTH_SHORT).show();
                    // Para atualizar a contagem visualmente, você pode recarregar os dados
                    loadPublicOccurrences();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Erro ao apoiar.", Toast.LENGTH_SHORT).show());
    }
}
