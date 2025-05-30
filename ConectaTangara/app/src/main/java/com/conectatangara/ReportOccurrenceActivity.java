package com.conectatangara; // Certifique-se que este é o seu pacote correto

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location; // Import necessário
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast; // Import correto para Toast

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager; // Mantido caso descomente o RecyclerView
import androidx.recyclerview.widget.RecyclerView;   // Mantido caso descomente o RecyclerView

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
// Remova a importação de MediaPreviewAdapter se não for usá-la agora
// import com.conectatangara.adapters.MediaPreviewAdapter;

public class ReportOccurrenceActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText editTextOccurrenceTitle;
    private TextInputEditText editTextOccurrenceDescription;
    private Spinner spinnerOccurrenceCategory;
    private Button buttonUseCurrentLocation;
    private ImageView imageViewMapPreview;
    private Button buttonAddMedia;
    private RecyclerView recyclerViewMediaPreviews;
    private Button buttonSubmitOccurrence;

    private FusedLocationProviderClient fusedLocationClient;
    private LatLng currentSelectedLocation;

    private final ArrayList<Uri> selectedMediaUris = new ArrayList<>();
    // private MediaPreviewAdapter mediaAdapter;

    private ActivityResultLauncher<String> requestLocationPermissionLauncher;
    private ActivityResultLauncher<Intent> pickMediaLauncher;
    private ActivityResultLauncher<Intent> captureImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_occurrence);

        initializeUI();
        setupToolbar();
        setupCategorySpinner();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setupActivityResultLaunchers();
        setupClickListeners();
        // setupMediaRecyclerView();
    }

    private void initializeUI() {
        toolbar = findViewById(R.id.toolbar_report);
        editTextOccurrenceTitle = findViewById(R.id.editTextOccurrenceTitle);
        editTextOccurrenceDescription = findViewById(R.id.editTextOccurrenceDescription);
        spinnerOccurrenceCategory = findViewById(R.id.spinnerOccurrenceCategory);
        buttonUseCurrentLocation = findViewById(R.id.buttonUseCurrentLocation);
        imageViewMapPreview = findViewById(R.id.imageViewMapPreview);
        buttonAddMedia = findViewById(R.id.buttonAddMedia);
        recyclerViewMediaPreviews = findViewById(R.id.recyclerViewMediaPreviews);
        buttonSubmitOccurrence = findViewById(R.id.buttonSubmitOccurrence);
    }

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
                        Toast.makeText(this, R.string.message_location_permission_denied, android.widget.Toast.LENGTH_SHORT).show(); // Qualificação explícita
                    }
                });

        pickMediaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri mediaUri = result.getData().getData();
                        if (mediaUri != null) {
                            selectedMediaUris.add(mediaUri);
                            Toast.makeText(this, R.string.message_media_added, android.widget.Toast.LENGTH_SHORT).show(); // Qualificação explícita
                            // TODO: Atualizar o RecyclerView de mídias
                            // if (mediaAdapter != null) mediaAdapter.notifyItemInserted(selectedMediaUris.size() - 1);
                        }
                    }
                });

        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = null;
                        if (extras != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                imageBitmap = extras.getParcelable("data", Bitmap.class);
                            } else {
                                @SuppressWarnings("deprecation")
                                Bitmap bmp = (Bitmap) extras.get("data");
                                imageBitmap = bmp;
                            }
                        }
                        if (imageBitmap != null) {
                            Toast.makeText(this, R.string.message_image_captured, android.widget.Toast.LENGTH_SHORT).show(); // Qualificação explícita
                            // TODO: Salvar o Bitmap em um arquivo, obter a Uri e adicionar à lista selectedMediaUris
                        }
                    }
                }
        );
    }

    private void setupClickListeners() {
        buttonUseCurrentLocation.setOnClickListener(v -> requestLocationPermission());

        buttonAddMedia.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickMediaLauncher.launch(intent);
        });

        buttonSubmitOccurrence.setOnClickListener(v -> submitOccurrence());
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.message_location_permission_denied, android.widget.Toast.LENGTH_LONG).show(); // Qualificação explícita
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> { // location é do tipo android.location.Location
                    if (location != null) {
                        currentSelectedLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        imageViewMapPreview.setImageResource(R.drawable.ic_location_on_map);
                        Toast.makeText(ReportOccurrenceActivity.this, R.string.message_location_updated, android.widget.Toast.LENGTH_SHORT).show(); // LINHA 200 COM QUALIFICAÇÃO EXPLÍCITA
                    } else {
                        Toast.makeText(ReportOccurrenceActivity.this, R.string.message_location_not_found, android.widget.Toast.LENGTH_LONG).show(); // Qualificação explícita
                    }
                })
                .addOnFailureListener(this, e -> {
                    Toast.makeText(ReportOccurrenceActivity.this, getString(R.string.message_location_error, e.getMessage()), android.widget.Toast.LENGTH_LONG).show(); // Qualificação explícita
                });
    }

    private void submitOccurrence() {
        String title = String.valueOf(editTextOccurrenceTitle.getText()).trim();
        String description = String.valueOf(editTextOccurrenceDescription.getText()).trim();
        String category = "";
        if (spinnerOccurrenceCategory.getSelectedItem() != null &&
                !spinnerOccurrenceCategory.getSelectedItem().toString().equals(getString(R.string.prompt_select_category))) {
            category = spinnerOccurrenceCategory.getSelectedItem().toString();
        }

        if (title.isEmpty() || description.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, R.string.message_fill_required_fields, android.widget.Toast.LENGTH_LONG).show(); // Qualificação explícita
            return;
        }
        if (currentSelectedLocation == null) {
            Toast.makeText(this, R.string.message_define_location, android.widget.Toast.LENGTH_LONG).show(); // Qualificação explícita
            return;
        }

        Toast.makeText(this, R.string.message_occurrence_submitted, android.widget.Toast.LENGTH_LONG).show(); // Qualificação explícita
        finish();
    }

    /*
    // Descomente e implemente se for usar RecyclerView para mídias
    private void setupMediaRecyclerView() {
        recyclerViewMediaPreviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // Crie sua classe MediaPreviewAdapter
        // mediaAdapter = new MediaPreviewAdapter(this, selectedMediaUris, uri -> {
        //     // Lógica para remover mídia, se necessário
        //     selectedMediaUris.remove(uri);
        //     mediaAdapter.notifyDataSetChanged();
        // });
        // recyclerViewMediaPreviews.setAdapter(mediaAdapter);
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
