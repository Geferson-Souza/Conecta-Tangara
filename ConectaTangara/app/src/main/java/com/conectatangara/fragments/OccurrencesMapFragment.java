package com.conectatangara.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.conectatangara.R;
import com.conectatangara.models.Ocorrencia;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class OccurrencesMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_occurrences_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment_container);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Move a câmera para o centro de Tangará da Serra
        LatLng tangara = new LatLng(-14.619, -57.490);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tangara, 12f));
        loadOccurrencesOnMap();
    }

    private void loadOccurrencesOnMap() {
        db.collection("ocorrencias")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && mMap != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Ocorrencia ocorrencia = document.toObject(Ocorrencia.class);
                            // Adiciona o marcador apenas se a ocorrência tiver localização
                            if (ocorrencia.getLocalizacao() != null) {
                                LatLng position = new LatLng(ocorrencia.getLocalizacao().getLatitude(), ocorrencia.getLocalizacao().getLongitude());
                                mMap.addMarker(new MarkerOptions()
                                        .position(position)
                                        .title(ocorrencia.getTitulo())
                                        .snippet(ocorrencia.getStatus()));
                            }
                        }
                    } else {
                        Log.w("MapFragment", "Erro ao buscar ocorrências.", task.getException());
                    }
                });
    }
}
