package com.conectatangara.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
// import androidx.appcompat.app.AppCompatActivity; // Não mais necessário para setSupportActionBar aqui
// import androidx.appcompat.widget.Toolbar; // Não mais necessário aqui
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatangara.R;
import com.conectatangara.activities.MainActivity; // Import para chamar o método da MainActivity
import com.conectatangara.activities.ReportOccurrenceActivity; // Para navegação do FAB
import android.content.Intent; // Para navegação do FAB
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

// Definição da classe Shortcut (modelo de dados para os atalhos)
class Shortcut {
    private String title;
    private int iconResId;
    private int iconColorResId;

    public Shortcut(String title, int iconResId, int iconColorResId) {
        this.title = title;
        this.iconResId = iconResId;
        this.iconColorResId = iconColorResId;
    }

    public String getTitle() { return title; }
    public int getIconResId() { return iconResId; }
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
        // Usar ContextCompat para obter a cor é mais seguro
        holder.iconImageView.setColorFilter(androidx.core.content.ContextCompat.getColor(holder.itemView.getContext(), shortcut.getIconColorResId()));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(shortcut));
    }

    @Override
    public int getItemCount() {
        return shortcutList.size();
    }

    static class ShortcutViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView titleTextView;

        public ShortcutViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.imageViewShortcutIcon); // Certifique-se que este ID existe em item_shortcut_card.xml
            titleTextView = itemView.findViewById(R.id.textViewShortcutTitle); // Certifique-se que este ID existe em item_shortcut_card.xml
        }
    }
}

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewShortcuts;
    private ShortcutAdapter shortcutAdapter;
    private List<Shortcut> shortcutItems;
    private FloatingActionButton fabRegisterOccurrence;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Informa que este fragmento deseja adicionar itens ao menu da Toolbar da Activity
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewShortcuts = view.findViewById(R.id.recyclerViewShortcuts);
        recyclerViewShortcuts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        prepareShortcutData();

        // ########## INÍCIO DA MUDANÇA ##########
        shortcutAdapter = new ShortcutAdapter(shortcutItems, shortcut -> {
            // Lógica de navegação implementada
            String title = shortcut.getTitle();

            if (title.equals(getString(R.string.shortcut_title_register_occurrence))) {
                startActivity(new Intent(getActivity(), ReportOccurrenceActivity.class));
            } else if (getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (title.equals(getString(R.string.shortcut_title_my_occurrences))) {
                    mainActivity.navigateToBottomNavItem(R.id.nav_my_occurrences);
                } else if (title.equals(getString(R.string.shortcut_title_view_occurrences))) {
                    mainActivity.navigateToBottomNavItem(R.id.nav_public_map);
                } else if (title.equals(getString(R.string.shortcut_title_public_data))) {
                    mainActivity.navigateToBottomNavItem(R.id.nav_indicators);
                } else {
                    // Para os outros cards que ainda não têm destino
                    Toast.makeText(getContext(), shortcut.getTitle() + ": Em desenvolvimento", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // ########## FIM DA MUDANÇA ##########
        recyclerViewShortcuts.setAdapter(shortcutAdapter);

        fabRegisterOccurrence = view.findViewById(R.id.fabRegisterOccurrence);
        if (fabRegisterOccurrence.getDrawable() == null) {
            fabRegisterOccurrence.setImageResource(R.drawable.ic_add_fab);
        }
        fabRegisterOccurrence.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ReportOccurrenceActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.app_name));
        }
    }

    private void prepareShortcutData() {
        shortcutItems = new ArrayList<>();
        shortcutItems.add(new Shortcut(getString(R.string.shortcut_title_register_occurrence), R.drawable.ic_register_occurrences, R.color.red_pantone));
        shortcutItems.add(new Shortcut(getString(R.string.shortcut_title_my_occurrences), R.drawable.ic_my_occurrences, R.color.cerulean));
        shortcutItems.add(new Shortcut(getString(R.string.shortcut_title_view_occurrences), R.drawable.ic_view_occurrences, R.color.cerulean));
        shortcutItems.add(new Shortcut(getString(R.string.shortcut_title_services), R.drawable.ic_services, R.color.cerulean));
        shortcutItems.add(new Shortcut(getString(R.string.shortcut_title_events), R.drawable.ic_events, R.color.cerulean));
        shortcutItems.add(new Shortcut(getString(R.string.shortcut_title_news), R.drawable.ic_news, R.color.cerulean));
        shortcutItems.add(new Shortcut(getString(R.string.shortcut_title_public_data), R.drawable.ic_public, R.color.cerulean));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // ########## INÍCIO DA MUDANÇA ##########
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.home_toolbar_menu, menu);
        // ########## FIM DA MUDANÇA ##########
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // ########## INÍCIO DA MUDANÇA ##########
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            // Navega para o fragmento de perfil
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToBottomNavItem(R.id.nav_profile);
            }
            return true;
        } else if (id == R.id.action_notifications) {
            Toast.makeText(getContext(), "Notificações clicado", Toast.LENGTH_SHORT).show();
            // TODO: Implementar navegação para a tela de Notificações
            return true;
        } else if (id == R.id.action_logout) {
            // Chama o método de logout da MainActivity
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).logout();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
        // ########## FIM DA MUDANÇA ##########
    }
}
