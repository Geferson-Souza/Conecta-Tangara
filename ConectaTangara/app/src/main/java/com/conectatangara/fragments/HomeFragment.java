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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatangara.R;
import com.conectatangara.activities.MainActivity;
import com.conectatangara.activities.ReportOccurrenceActivity;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

// As classes Shortcut e ShortcutAdapter são mantidas como estavam no seu arquivo original.
class Shortcut {
    private String title;
    private int iconResId;
    private int iconColorResId;
    public Shortcut(String title, int iconResId, int iconColorResId) {
        this.title = title; this.iconResId = iconResId; this.iconColorResId = iconColorResId;
    }
    public String getTitle() { return title; }
    public int getIconResId() { return iconResId; }
    public int getIconColorResId() { return iconColorResId; }
}

class ShortcutAdapter extends RecyclerView.Adapter<ShortcutAdapter.ShortcutViewHolder> {
    private List<Shortcut> shortcutList;
    private OnItemClickListener listener;
    public interface OnItemClickListener { void onItemClick(Shortcut shortcut); }
    public ShortcutAdapter(List<Shortcut> shortcutList, OnItemClickListener listener) {
        this.shortcutList = shortcutList; this.listener = listener;
    }
    @NonNull @Override
    public ShortcutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shortcut_card, parent, false);
        return new ShortcutViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ShortcutViewHolder holder, int position) {
        Shortcut shortcut = shortcutList.get(position);
        holder.titleTextView.setText(shortcut.getTitle());
        holder.iconImageView.setImageResource(shortcut.getIconResId());
        holder.iconImageView.setColorFilter(androidx.core.content.ContextCompat.getColor(holder.itemView.getContext(), shortcut.getIconColorResId()));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(shortcut));
    }
    @Override
    public int getItemCount() { return shortcutList.size(); }
    static class ShortcutViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView; TextView titleTextView;
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
    private FloatingActionButton fabRegisterOccurrence;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewShortcuts = view.findViewById(R.id.recyclerViewShortcuts);
        recyclerViewShortcuts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        prepareShortcutData(); // A lista de atalhos foi atualizada neste método

        shortcutAdapter = new ShortcutAdapter(shortcutItems, shortcut -> {
            String title = shortcut.getTitle();
            if (getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();

                // Navega para Registrar Ocorrência (Activity)
                if (title.equals(getString(R.string.shortcut_title_register_occurrence))) {
                    startActivity(new Intent(getActivity(), ReportOccurrenceActivity.class));

                    // ########## ATUALIZAÇÃO DA LÓGICA DE CLIQUE ##########
                } else if (title.equals(getString(R.string.shortcut_title_public_occurrences))) {
                    mainActivity.navigateToPublicOccurrences();

                    // Navegação para os itens da barra inferior
                } else if (title.equals(getString(R.string.shortcut_title_my_occurrences))) {
                    mainActivity.navigateToBottomNavItem(R.id.nav_my_occurrences);
                } else if (title.equals(getString(R.string.shortcut_title_view_occurrences))) {
                    mainActivity.navigateToBottomNavItem(R.id.nav_public_map);
                } else if (title.equals(getString(R.string.shortcut_title_public_data))) {
                    mainActivity.navigateToBottomNavItem(R.id.nav_indicators);
                } else {
                    // Fallback para funcionalidades em desenvolvimento
                    Toast.makeText(getContext(), shortcut.getTitle() + ": Em desenvolvimento", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        // ########## LISTA DE CARDS ATUALIZADA ##########
        shortcutItems.add(new Shortcut(getString(R.string.shortcut_title_register_occurrence), R.drawable.ic_register_occurrences, R.color.red_pantone));
        shortcutItems.add(new Shortcut(getString(R.string.shortcut_title_my_occurrences), R.drawable.ic_my_occurrences, R.color.cerulean));

        // Substituído "Eventos da Cidade" por "Ocorrências Públicas"
        shortcutItems.add(new Shortcut(getString(R.string.shortcut_title_public_occurrences), R.drawable.ic_public_occurrences, R.color.cerulean));

        shortcutItems.add(new Shortcut(getString(R.string.shortcut_title_view_occurrences), R.drawable.ic_view_occurrences, R.color.cerulean));
        shortcutItems.add(new Shortcut(getString(R.string.shortcut_title_services), R.drawable.ic_services, R.color.cerulean));
        shortcutItems.add(new Shortcut(getString(R.string.shortcut_title_news), R.drawable.ic_news, R.color.cerulean));
        shortcutItems.add(new Shortcut(getString(R.string.shortcut_title_public_data), R.drawable.ic_public, R.color.cerulean));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.home_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToBottomNavItem(R.id.nav_profile);
            }
            return true;
        } else if (id == R.id.action_notifications) {
            Toast.makeText(getContext(), "Notificações clicado", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_logout) {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).logout();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
