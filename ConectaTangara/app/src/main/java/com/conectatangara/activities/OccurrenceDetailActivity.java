package com.conectatangara.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.conectatangara.R;
import com.conectatangara.models.Ocorrencia;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class OccurrenceDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Toolbar toolbar;
    private TextView tvCategory, tvTitle, tvDescription, tvStatus, tvDate;
    private Button btnSupport;
    private MapView mapView;
    private GoogleMap googleMap;
    private GeoPoint occurrenceLocation;

    private FirebaseFirestore db;
    private String occurrenceId;
    private DocumentReference occurrenceRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occurrence_detail);

        // Inicializa o Firestore
        db = FirebaseFirestore.getInstance();

        // Pega o ID da ocorrência passado pelo Intent
        occurrenceId = getIntent().getStringExtra("OCCURRENCE_ID");
        if (occurrenceId == null) {
            Toast.makeText(this, "Erro: ID da ocorrência não encontrado.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        occurrenceRef = db.collection("ocorrencias").document(occurrenceId);

        // Configura a Toolbar
        toolbar = findViewById(R.id.toolbar_occurrence_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Inicializa as Views
        tvCategory = findViewById(R.id.tv_detail_category);
        tvTitle = findViewById(R.id.tv_detail_title);
        tvDescription = findViewById(R.id.tv_detail_description);
        tvStatus = findViewById(R.id.tv_detail_status);
        tvDate = findViewById(R.id.tv_detail_date);
        btnSupport = findViewById(R.id.button_support);
        mapView = findViewById(R.id.map_view_detail);

        // Inicializa o MapView
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Carrega os dados
        loadOccurrenceData();

        // Configura o clique do botão de apoiar
        btnSupport.setOnClickListener(v -> supportOccurrence());
    }

    private void loadOccurrenceData() {
        occurrenceRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Ocorrencia ocorrencia = documentSnapshot.toObject(Ocorrencia.class);
                if (ocorrencia != null) {
                    // Preenche os campos de texto
                    tvCategory.setText(ocorrencia.getCategoria());
                    tvTitle.setText(ocorrencia.getTitulo());
                    tvDescription.setText(ocorrencia.getDescricao());
                    tvStatus.setText(ocorrencia.getStatus());

                    if (ocorrencia.getDataRegistro() != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        tvDate.setText("Registrado em " + sdf.format(ocorrencia.getDataRegistro()));
                    }

                    // Guarda a localização para usar no mapa
                    occurrenceLocation = ocorrencia.getLocalizacao();
                    updateMapLocation();

                }
            } else {
                Toast.makeText(this, "Ocorrência não encontrada.", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Erro ao carregar dados.", Toast.LENGTH_SHORT).show();
        });
    }

    private void supportOccurrence() {
        // Incrementa o contador de "apoios" no Firestore
        occurrenceRef.update("apoios", FieldValue.increment(1))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Ocorrência apoiada! Obrigado.", Toast.LENGTH_SHORT).show();
                    btnSupport.setEnabled(false); // Desabilita o botão após apoiar
                    btnSupport.setText("Apoiado");
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Erro ao apoiar ocorrência.", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.getUiSettings().setAllGesturesEnabled(false); // Desativa gestos no mapa de detalhe
        updateMapLocation();
    }

    private void updateMapLocation() {
        if (googleMap != null && occurrenceLocation != null) {
            LatLng latLng = new LatLng(occurrenceLocation.getLatitude(), occurrenceLocation.getLongitude());
            googleMap.clear(); // Limpa marcadores antigos
            googleMap.addMarker(new MarkerOptions().position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
        }
    }

    // Métodos de ciclo de vida para o MapView
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
