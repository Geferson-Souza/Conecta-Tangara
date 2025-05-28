package com.conectatangara; // Ou o pacote correto (ex: com.conectatangara.map)

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

// Import da Classe R do SEU projeto


/**
 * Fragmento para exibir o mapa interativo de ocorrências.
 */
public class OccurrencesMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap; // Variável para guardar a referência do Google Map

    /**
     * Construtor padrão obrigatório para Fragments.
     */
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
        // É importante usar getChildFragmentManager() aqui, pois estamos dentro de um Fragment.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_container);

        // Verifica se o mapFragment foi encontrado antes de chamar getMapAsync
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Aqui você pode adicionar o código para configurar seus filtros (Spinners, Chips)
        // Ex: ChipGroup chipGroup = view.findViewById(R.id.chip_group_status_map);
    }

    /**
     * Este método é chamado quando o mapa está pronto para ser usado.
     * É aqui que você adiciona marcadores, listeners, move a câmera, etc.
     * @param googleMap A instância do GoogleMap.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // TODO: Implementar a lógica de busca e exibição de ocorrências

        // Exemplo: Adiciona um marcador em Tangará da Serra e move a câmera
        LatLng tangara = new LatLng(-14.6195, -57.4895); // Coordenadas aproximadas
        mMap.addMarker(new MarkerOptions()
                .position(tangara)
                .title("Tangará da Serra"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tangara, 12f)); // 12f é o nível de zoom

        // TODO: Configurar listeners de clique nos marcadores
        // TODO: Configurar a InfoWindow customizada (se necessário)
        // TODO: Implementar a lógica dos filtros para atualizar os marcadores
    }
}