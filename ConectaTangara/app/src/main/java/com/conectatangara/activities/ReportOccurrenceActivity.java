package com.conectatangara.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatangara.R;
import com.conectatangara.adapters.MediaPreviewAdapter;
import com.conectatangara.interfaces.OnMediaRemoveListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ReportOccurrenceActivity extends AppCompatActivity implements OnMediaRemoveListener, OnMapReadyCallback {

    // --- Variáveis de UI ---
    private Toolbar toolbar;
    private TextInputEditText editTextOccurrenceDescription;
    private Spinner spinnerOccurrenceCategory;
    private Button buttonUseCurrentLocation;
    private MapView mapViewPreview;
    private Button buttonAddMedia;
    private RecyclerView recyclerViewMediaPreviews;
    private Button buttonSubmitOccurrence;

    // --- Variáveis de Lógica ---
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap googleMap;
    private LatLng currentSelectedLocation;
    private final ArrayList<Uri> selectedMediaUris = new ArrayList<>();
    private MediaPreviewAdapter mediaAdapter;

    // --- Launchers ---
    private ActivityResultLauncher<String> requestLocationPermissionLauncher;
    private ActivityResultLauncher<Intent> pickMediaLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_occurrence);

        initializeUI(savedInstanceState);
        setupKeyboardDismissal(); // Adicionado para corrigir o problema do teclado
        setupToolbar();
        setupCategorySpinner();
        setupMediaRecyclerView();
        setupActivityResultLaunchers();
        setupClickListeners();
    }

    private void initializeUI(Bundle savedInstanceState) {
        toolbar = findViewById(R.id.toolbar_report);
        editTextOccurrenceDescription = findViewById(R.id.editTextOccurrenceDescription);
        spinnerOccurrenceCategory = findViewById(R.id.spinnerOccurrenceCategory);
        buttonUseCurrentLocation = findViewById(R.id.buttonUseCurrentLocation);
        buttonAddMedia = findViewById(R.id.buttonAddMedia);
        recyclerViewMediaPreviews = findViewById(R.id.recyclerViewMediaPreviews);
        buttonSubmitOccurrence = findViewById(R.id.buttonSubmitOccurrence);
        mapViewPreview = findViewById(R.id.mapViewPreview);
        mapViewPreview.onCreate(savedInstanceState);
        mapViewPreview.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    // ########## INÍCIO DA MUDANÇA ##########
    // Lógica para esconder o teclado ao tocar fora de um campo de texto
    @SuppressLint("ClickableViewAccessibility")
    private void setupKeyboardDismissal() {
        findViewById(R.id.report_occurrence_root_layout).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                    focusedView.clearFocus();
                }
            }
            return false; // Retorna false para não consumir o evento de toque
        });
    }
    // ########## FIM DA MUDANÇA ##########

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.toolbar_title_report_occurrence);
        }
    }

    private void setupCategorySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.occurrence_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOccurrenceCategory.setAdapter(adapter);
    }

    private void setupActivityResultLaunchers() {
        requestLocationPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        getCurrentLocation();
                    } else {
                        Toast.makeText(this, R.string.message_location_permission_denied, Toast.LENGTH_SHORT).show();
                    }
                });

        pickMediaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        if (result.getData().getClipData() != null) {
                            int count = result.getData().getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                addMediaToPreview(result.getData().getClipData().getItemAt(i).getUri());
                            }
                        } else if (result.getData().getData() != null) {
                            addMediaToPreview(result.getData().getData());
                        }
                    }
                });
    }

    private void addMediaToPreview(Uri mediaUri) {
        if (mediaUri != null && selectedMediaUris.size() < 5) {
            mediaAdapter.addMediaItem(mediaUri);
            recyclerViewMediaPreviews.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Limite de 5 mídias atingido.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupClickListeners() {
        buttonUseCurrentLocation.setOnClickListener(v -> requestLocationPermission());
        buttonAddMedia.setOnClickListener(v -> openMediaChooser());
        buttonSubmitOccurrence.setOnClickListener(v -> submitOccurrence());
    }

    private void openMediaChooser() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setType("image/*");
        pickMediaLauncher.launch(galleryIntent);
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            getCurrentLocation();
        }
    }

    // ########## INÍCIO DA MUDANÇA ##########
    // Lógica de localização mais robusta usando getCurrentLocation
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        Toast.makeText(this, "Buscando localização atual...", Toast.LENGTH_SHORT).show();
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentSelectedLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        updateMapLocation();
                        Toast.makeText(ReportOccurrenceActivity.this, R.string.message_location_updated, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReportOccurrenceActivity.this, R.string.message_location_not_found, Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(this, e -> {
                    Toast.makeText(ReportOccurrenceActivity.this, getString(R.string.message_location_error, e.getMessage()), Toast.LENGTH_LONG).show();
                });
    }
    // ########## FIM DA MUDANÇA ##########

    private void submitOccurrence() {
        String description = String.valueOf(editTextOccurrenceDescription.getText()).trim();
        if (spinnerOccurrenceCategory.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Por favor, selecione uma categoria válida.", Toast.LENGTH_LONG).show();
            return;
        }
        String category = spinnerOccurrenceCategory.getSelectedItem().toString();

        if (description.isEmpty()) {
            editTextOccurrenceDescription.setError("A descrição é obrigatória");
            editTextOccurrenceDescription.requestFocus();
            return;
        }

        if (currentSelectedLocation == null) {
            Toast.makeText(this, R.string.message_define_location, Toast.LENGTH_LONG).show();
            return;
        }

        String title = category;

        // TODO: Lógica para criar o objeto Ocorrencia e salvar no Firestore
        // TODO: Lógica para fazer upload das mídias para o Firebase Storage

        Toast.makeText(this, R.string.message_occurrence_submitted, Toast.LENGTH_LONG).show();
        finish();
    }

    private void setupMediaRecyclerView() {
        recyclerViewMediaPreviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mediaAdapter = new MediaPreviewAdapter(this, selectedMediaUris, this);
        recyclerViewMediaPreviews.setAdapter(mediaAdapter);
    }

    @Override
    public void onMediaRemove(Uri mediaUri, int position) {
        if (position >= 0 && position < selectedMediaUris.size()) {
            selectedMediaUris.remove(position);
            mediaAdapter.notifyItemRemoved(position);
            mediaAdapter.notifyItemRangeChanged(position, selectedMediaUris.size());
            if (selectedMediaUris.isEmpty()) {
                recyclerViewMediaPreviews.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.googleMap = map;
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng tangara = new LatLng(-14.619, -57.490);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tangara, 13f));
    }

    private void updateMapLocation() {
        if (googleMap != null && currentSelectedLocation != null) {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(currentSelectedLocation).title("Local da Ocorrência"));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentSelectedLocation, 17f));
        }
    }

    @Override protected void onResume() { super.onResume(); mapViewPreview.onResume(); }
    @Override protected void onStart() { super.onStart(); mapViewPreview.onStart(); }
    @Override protected void onStop() { super.onStop(); mapViewPreview.onStop(); }
    @Override protected void onPause() { mapViewPreview.onPause(); super.onPause(); }
    @Override protected void onDestroy() { mapViewPreview.onDestroy(); super.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory(); mapViewPreview.onLowMemory(); }
    @Override protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapViewPreview.onSaveInstanceState(outState);
    }
}
