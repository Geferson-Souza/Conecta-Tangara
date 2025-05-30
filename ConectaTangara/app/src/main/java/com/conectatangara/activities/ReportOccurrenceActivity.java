package com.conectatangara.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatangara.R; // Import para R
import com.conectatangara.adapters.MediaPreviewAdapter; // Import para o Adapter
import com.conectatangara.interfaces.OnMediaRemoveListener; // Import para a Interface
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File; // Para salvar imagem da câmera
import java.io.FileOutputStream; // Para salvar imagem da câmera
import java.io.IOException; // Para salvar imagem da câmera
import java.text.SimpleDateFormat; // Para nome do arquivo de imagem
import java.util.ArrayList;
import java.util.Date; // Para nome do arquivo de imagem
import java.util.Locale; // Para nome do arquivo de imagem

public class ReportOccurrenceActivity extends AppCompatActivity implements OnMediaRemoveListener {

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
    private MediaPreviewAdapter mediaAdapter;

    private ActivityResultLauncher<String> requestLocationPermissionLauncher;
    private ActivityResultLauncher<Intent> pickMediaLauncher;
    private ActivityResultLauncher<Uri> takePictureLauncher; // Modificado para Uri
    private Uri currentPhotoUri; // Para armazenar a Uri da foto tirada pela câmera


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
        setupMediaRecyclerView();
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
                        Toast.makeText(this, R.string.message_location_permission_denied, android.widget.Toast.LENGTH_SHORT).show();
                    }
                });

        pickMediaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        if (result.getData().getClipData() != null) { // Múltiplas imagens selecionadas
                            int count = result.getData().getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                Uri mediaUri = result.getData().getClipData().getItemAt(i).getUri();
                                addMediaToPreview(mediaUri);
                            }
                        } else if (result.getData().getData() != null) { // Uma única imagem selecionada
                            Uri mediaUri = result.getData().getData();
                            addMediaToPreview(mediaUri);
                        }
                        if (!selectedMediaUris.isEmpty()) {
                            Toast.makeText(this, R.string.message_media_added, android.widget.Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Launcher para tirar foto com a câmera
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success && currentPhotoUri != null) {
                        addMediaToPreview(currentPhotoUri);
                        Toast.makeText(this, R.string.message_image_captured, android.widget.Toast.LENGTH_SHORT).show();
                    } else if (currentPhotoUri != null) {
                        // Se não teve sucesso mas a Uri foi criada, podemos deletar o arquivo vazio
                        new File(currentPhotoUri.getPath()).delete();
                    }
                });
    }

    private void addMediaToPreview(Uri mediaUri) {
        if (mediaUri != null) {
            if (selectedMediaUris.size() < 5) { // Exemplo: Limite de 5 mídias
                // selectedMediaUris.add(mediaUri); // O adapter agora tem seu próprio método
                mediaAdapter.addMediaItem(mediaUri); // Usa o método do adapter
                recyclerViewMediaPreviews.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Limite de mídias atingido (5).", android.widget.Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void setupClickListeners() {
        buttonUseCurrentLocation.setOnClickListener(v -> requestLocationPermission());
        buttonAddMedia.setOnClickListener(v -> openMediaChooser());
        buttonSubmitOccurrence.setOnClickListener(v -> submitOccurrence());
    }

    private void openMediaChooser() {
        // Intent para galeria (permite selecionar múltiplas imagens)
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Permite seleção múltipla
        pickIntent.setType("image/*"); // Apenas imagens por enquanto

        // Intent para câmera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            Toast.makeText(this, "Erro ao criar arquivo para foto.", Toast.LENGTH_SHORT).show();
        }
        if (photoFile != null) {
            // currentPhotoUri = FileProvider.getUriForFile(this,
            //        "com.conectatangara.fileprovider", // PRECISA CONFIGURAR FileProvider no Manifest e res/xml
            //        photoFile);
            // takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
            // POR SIMPLICIDADE, VAMOS USAR O MÉTODO ANTIGO QUE RETORNA UM THUMBNAIL
            // OU, MELHOR AINDA, USAR O ActivityResultContracts.TakePicture() que simplifica isso
            // A versão atual do takePictureLauncher usa ActivityResultContracts.TakePicture()
            // que espera uma Uri de output, mas para isso precisa configurar FileProvider.
            // Vamos simplificar e lançar um chooser entre galeria e câmera (se houver app de câmera)

            // Intent para galeria
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            galleryIntent.setType("image/*"); // Apenas imagens

            // Intent para câmera (precisaria do FileProvider para salvar em Uri)
            // Por enquanto, vamos focar na galeria que é mais simples sem FileProvider
            // Se quiser câmera, precisaremos configurar FileProvider e usar currentPhotoUri com EXTRA_OUTPUT

            // Criar um chooser
            Intent chooserIntent = Intent.createChooser(galleryIntent, "Selecionar Mídia de");

            // Array de intents iniciais (adicionar câmera se disponível)
            // ArrayList<Intent> extraIntents = new ArrayList<>();
            // if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //     extraIntents.add(takePictureIntent);
            // }
            // chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents.toArray(new Intent[0]));

            pickMediaLauncher.launch(chooserIntent); // Lança o chooser com a galeria
            // Para câmera, precisaria de um botão separado ou adicionar ao chooser com FileProvider
        } else {
            // Se não conseguiu criar o arquivo para a foto (ou se não quer câmera por enquanto),
            // lança apenas a galeria.
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            galleryIntent.setType("image/*");
            pickMediaLauncher.launch(galleryIntent);
        }
    }


    private File createImageFile() throws IOException {
        // Cria um nome de arquivo de imagem único
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES); // Salva em diretório privado do app
        File image = File.createTempFile(
                imageFileName,  /* prefixo */
                ".jpg",         /* sufixo */
                storageDir      /* diretório */
        );
        // Salva o caminho do arquivo para usar com ACTION_VIEW Intents
        // currentPhotoPath = image.getAbsolutePath(); // Se precisar do path absoluto
        currentPhotoUri = Uri.fromFile(image); // IMPORTANTE: Isso pode não funcionar com FileProvider.
        // Para FileProvider, use FileProvider.getUriForFile(...)
        return image;
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
            Toast.makeText(this, R.string.message_location_permission_denied, android.widget.Toast.LENGTH_LONG).show();
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentSelectedLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        imageViewMapPreview.setImageResource(R.drawable.ic_location_on_map);
                        Toast.makeText(ReportOccurrenceActivity.this, R.string.message_location_updated, android.widget.Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReportOccurrenceActivity.this, R.string.message_location_not_found, android.widget.Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(this, e -> {
                    Toast.makeText(ReportOccurrenceActivity.this, getString(R.string.message_location_error, e.getMessage()), android.widget.Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, R.string.message_fill_required_fields, android.widget.Toast.LENGTH_LONG).show();
            return;
        }
        if (currentSelectedLocation == null) {
            Toast.makeText(this, R.string.message_define_location, android.widget.Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, R.string.message_occurrence_submitted, android.widget.Toast.LENGTH_LONG).show();
        // TODO: Lógica para criar o objeto da ocorrência e salvar no Firestore
        // TODO: Lógica para fazer upload das mídias (selectedMediaUris) para o Firebase Storage
        finish();
    }

    private void setupMediaRecyclerView() {
        recyclerViewMediaPreviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mediaAdapter = new MediaPreviewAdapter(this, selectedMediaUris, this);
        recyclerViewMediaPreviews.setAdapter(mediaAdapter);

        if (selectedMediaUris.isEmpty()) {
            recyclerViewMediaPreviews.setVisibility(View.GONE);
        } else {
            recyclerViewMediaPreviews.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMediaRemove(Uri mediaUri, int position) {
        // A lógica de remoção agora está no adapter para obter a posição correta.
        // O adapter chamará este método, mas a remoção da lista e a notificação
        // já foram feitas no adapter. Ou podemos fazer aqui.
        // Para consistência, vamos deixar o adapter manipular sua própria lista e notificar
        // mas a Activity precisa ser informada para, por exemplo, esconder o RecyclerView.

        // Se você preferir que a Activity gerencie a lista 'selectedMediaUris'
        // e o adapter apenas exiba:
        if (position >= 0 && position < selectedMediaUris.size()) {
            selectedMediaUris.remove(position); // Remove da lista da activity
            mediaAdapter.notifyItemRemoved(position);
            mediaAdapter.notifyItemRangeChanged(position, selectedMediaUris.size());
            if (selectedMediaUris.isEmpty()) {
                recyclerViewMediaPreviews.setVisibility(View.GONE);
            }
            Toast.makeText(this, "Mídia removida pela Activity", Toast.LENGTH_SHORT).show();
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
}

