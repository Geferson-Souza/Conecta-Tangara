package com.conectatangara; // Pacote do usuário

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView; // Import necessário para ShortcutViewHolder
import android.widget.TextView;  // Import necessário para ShortcutViewHolder
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

// Definição da classe Shortcut (modelo de dados para os atalhos)
class Shortcut {
    private String title;
    private int iconResId; // Recurso do ícone (ex: R.drawable.ic_register)
    private int iconColorResId; // Recurso da cor do ícone (ex: R.color.cerulean)

    public Shortcut(String title, int iconResId, int iconColorResId) {
        this.title = title;
        this.iconResId = iconResId;
        this.iconColorResId = iconColorResId;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public int getIconColorResId() { return iconColorResId; }
}

// Definição do Adapter para o RecyclerView
class ShortcutAdapter extends RecyclerView.Adapter<ShortcutAdapter.ShortcutViewHolder> {

    private List<Shortcut> shortcutList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Shortcut shortcut);
    }

    public ShortcutAdapter(List<Shortcut> shortcutList, OnItemClickListener listener) {
        this.shortcutList = shortcutList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShortcutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shortcut_card, parent, false);
        return new ShortcutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortcutViewHolder holder, int position) {
        Shortcut shortcut = shortcutList.get(position);
        holder.titleTextView.setText(shortcut.getTitle());
        holder.iconImageView.setImageResource(shortcut.getIconResId());
        // Definindo a cor do ícone
        holder.iconImageView.setColorFilter(holder.itemView.getContext().getResources().getColor(shortcut.getIconColorResId()));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(shortcut));
    }

    @Override
    public int getItemCount() {
        return shortcutList.size();
    }

    static class ShortcutViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView; // Removido android.widget.
        TextView titleTextView;  // Removido android.widget.

        public ShortcutViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.imageViewShortcutIcon);
            titleTextView = itemView.findViewById(R.id.textViewShortcutTitle);
        }
    }
}


public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewShortcuts;
    private ShortcutAdapter shortcutAdapter;
    private List<Shortcut> shortcutItems;
    private FloatingActionButton fabRegisterOccurrence; // Declaração do FAB

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Informa que este fragmento deseja adicionar itens ao menu da Toolbar
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla o layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Configura a Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Conecta Tangará");
            }
        }


        // Inicializa o RecyclerView
        recyclerViewShortcuts = view.findViewById(R.id.recyclerViewShortcuts);
        recyclerViewShortcuts.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 colunas

        // Prepara os dados dos atalhos
        prepareShortcutData();

        // Inicializa e configura o Adapter
        shortcutAdapter = new ShortcutAdapter(shortcutItems, shortcut -> {
            // Ação ao clicar em um item do grid
            Toast.makeText(getContext(), "Clicou em: " + shortcut.getTitle(), Toast.LENGTH_SHORT).show();
            // TODO: Implementar a navegação para a respectiva tela/funcionalidade
        });
        recyclerViewShortcuts.setAdapter(shortcutAdapter);

        // Inicializa o FAB
        fabRegisterOccurrence = view.findViewById(R.id.fabRegisterOccurrence);
        // Define o ícone correto para o FAB
        fabRegisterOccurrence.setImageResource(R.drawable.ic_add_fab); // Ícone padronizado para o FAB
        fabRegisterOccurrence.setOnClickListener(v -> {
            // Ação ao clicar no FAB
            Toast.makeText(getContext(), "FAB Registrar Ocorrência Clicado", Toast.LENGTH_SHORT).show();
            // TODO: Navegar para a tela de registro de ocorrência
        });

        return view;
    }

    private void prepareShortcutData() {
        shortcutItems = new ArrayList<>();
        // Adiciona os itens do menu com os nomes de drawable corretos
        shortcutItems.add(new Shortcut("Registrar Ocorrência", R.drawable.ic_register_occurrences, R.color.red_pantone)); // Corrigido: ic_register_occurrence
        shortcutItems.add(new Shortcut("Minhas Ocorrências", R.drawable.ic_my_occurrences, R.color.cerulean));
        shortcutItems.add(new Shortcut("Ver Ocorrências", R.drawable.ic_view_occurrences, R.color.cerulean));
        shortcutItems.add(new Shortcut("Serviços", R.drawable.ic_services, R.color.cerulean));
        shortcutItems.add(new Shortcut("Eventos", R.drawable.ic_events, R.color.cerulean));
        shortcutItems.add(new Shortcut("Notícias", R.drawable.ic_news, R.color.cerulean));
        shortcutItems.add(new Shortcut("Dados Públicos", R.drawable.ic_public, R.color.cerulean)); // Corrigido: ic_public_data
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear(); // Limpa itens anteriores para evitar duplicatas
        // Adiciona os ícones de Perfil e Notificações programaticamente
        // Certifique-se de ter res/values/ids.xml com action_profile e action_notifications
        menu.add(0, R.id.action_profile, 1, "Perfil")
                .setIcon(R.drawable.ic_profile)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, R.id.action_notifications, 2, "Notificações")
                .setIcon(R.drawable.ic_notifications)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            Toast.makeText(getContext(), "Perfil clicado", Toast.LENGTH_SHORT).show();
            // TODO: Implementar navegação para a tela de Perfil
            return true;
        } else if (id == R.id.action_notifications) {
            Toast.makeText(getContext(), "Notificações clicado", Toast.LENGTH_SHORT).show();
            // TODO: Implementar navegação para a tela de Notificações
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
