package com.conectatangara.fragments; // Ou o pacote correto

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
// import android.widget.Toast; // Pode ser útil para debug

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.conectatangara.R;
import com.conectatangara.activities.MainActivity; // Import para chamar o método da MainActivity
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class OccurrencesMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    public OccurrencesMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Infla o layout para este fragmento
        return inflater.inflate(R.layout.fragment_public_occurrences, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtém o SupportMapFragment e notifica quando o mapa estiver pronto para ser usado.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_container); // Certifique-se que este ID existe em fragment_public_occurrences.xml

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // TODO: Aqui você pode adicionar o código para configurar seus filtros (Spinners, Chips)
        // Ex: ChipGroup chipGroup = view.findViewById(R.id.chip_group_status_map);
        //     Spinner spinnerType = view.findViewById(R.id.spinner_occurrence_type_map);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Define o título na Toolbar da MainActivity quando este fragmento estiver visível
        if (getActivity() instanceof MainActivity) {
            // Crie esta string em strings.xml: <string name="toolbar_title_public_occurrences">Mapa de Ocorrências</string>
            ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.toolbar_title_public_occurrences));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // TODO: Implementar a lógica de busca e exibição de ocorrências do Firestore
        // TODO: Aplicar filtros e atualizar marcadores

        // Exemplo: Adiciona um marcador em Tangará da Serra e move a câmera
        LatLng tangara = new LatLng(-14.6195, -57.4895); // Coordenadas aproximadas
        mMap.addMarker(new MarkerOptions()
                .position(tangara)
                .title("Tangará da Serra")); // O título do marcador pode ser o título da ocorrência
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tangara, 12f));

        // Exemplo de como configurar um listener de clique no marcador
        // mMap.setOnMarkerClickListener(marker -> {
        //     // Obter o ID da ocorrência associada a este marcador (você pode usar marker.getTag())
        //     // String occurrenceId = (String) marker.getTag();
        //     // Navegar para a tela de detalhes da ocorrência ou mostrar uma InfoWindow customizada
        //     Toast.makeText(getContext(), "Clicou no marcador: " + marker.getTitle(), Toast.LENGTH_SHORT).show();
        //     return false; // Retorne true se você consumiu o evento e não quer o comportamento padrão (InfoWindow e centralizar)
        // });
    }
}
