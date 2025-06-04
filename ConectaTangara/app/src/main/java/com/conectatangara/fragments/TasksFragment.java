package com.conectatangara.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
// Adicione outros imports conforme necessário (RecyclerView, Adapter, etc.)

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.conectatangara.R;
import com.conectatangara.activities.MainActivity; // Se for definir título da Toolbar

public class TasksFragment extends Fragment {

    // Declare suas views aqui (RecyclerView, TextView para lista vazia, etc.)

    public TasksFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla o layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        // Inicialize suas views aqui usando view.findViewById()
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Configure seu RecyclerView, adapter, listeners, carregue dados, etc.
    }

    @Override
    public void onResume() {
        super.onResume();
        // Define o título na Toolbar da MainActivity
        if (getActivity() instanceof MainActivity) {
            // Certifique-se de ter a string R.string.toolbar_title_my_tasks em strings.xml
            ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.toolbar_title_my_tasks));
        }
        // TODO: Adicionar lógica para carregar/atualizar as tarefas
    }

    // Outros métodos do fragmento (para carregar dados, etc.)
}
    