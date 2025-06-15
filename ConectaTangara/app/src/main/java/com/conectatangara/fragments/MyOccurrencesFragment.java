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
import com.conectatangara.activities.OccurrenceDetailActivity; // Para abrir detalhes
import com.conectatangara.adapters.MyOccurrenceAdapter;
import com.conectatangara.models.Ocorrencia;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyOccurrencesFragment extends Fragment implements MyOccurrenceAdapter.OnOccurrenceClickListener {

    private RecyclerView recyclerViewMyOccurrences;
    private MyOccurrenceAdapter adapter;
    private List<Ocorrencia> occurrenceList;
    private TextView tvEmptyList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public MyOccurrencesFragment() {
        // Construtor vazio
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Renomeie seu layout para fragment_my_occurrences.xml se necessário
        return inflater.inflate(R.layout.fragment_my_occurrences, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerViewMyOccurrences = view.findViewById(R.id.recyclerView_my_occurrences);
        tvEmptyList = view.findViewById(R.id.tv_empty_my_occurrences);

        occurrenceList = new ArrayList<>();
        if (getContext() != null) {
            // Reutilize o seu MyOccurrenceAdapter, ele deve funcionar bem
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
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Usuário não autenticado.", Toast.LENGTH_SHORT).show();
            showEmptyListMessage(true);
            return;
        }

        String userId = currentUser.getUid();

        db.collection("ocorrencias")
                .whereEqualTo("userId", userId) // <<< A MUDANÇA PRINCIPAL ESTÁ AQUI
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
                        if (adapter != null) {
                            adapter.setOccurrences(occurrenceList);
                        }
                        showEmptyListMessage(occurrenceList.isEmpty());
                    } else {
                        Toast.makeText(getContext(), "Erro ao carregar ocorrências.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showEmptyListMessage(boolean show) {
        if (getContext() == null) return;
        if (show) {
            recyclerViewMyOccurrences.setVisibility(View.GONE);
            tvEmptyList.setVisibility(View.VISIBLE);
        } else {
            recyclerViewMyOccurrences.setVisibility(View.VISIBLE);
            tvEmptyList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onOccurrenceClick(Ocorrencia ocorrencia) {
        if (getContext() == null) return;
        // Navegar para a tela de detalhes ao clicar
        Intent intent = new Intent(getActivity(), OccurrenceDetailActivity.class);
        intent.putExtra("OCCURRENCE_ID", ocorrencia.getId()); // Passa o ID para a próxima tela
        startActivity(intent);
    }
}

