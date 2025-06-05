package com.conectatangara.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Para debug
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.conectatangara.R;
import com.conectatangara.utils.DatabaseSeeder;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser; // Import para FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore; // Import para Firestore

import java.util.HashMap; // Para criar dados do usuário
import java.util.Map; // Para criar dados do usuário

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity"; // Para logs

    private EditText editTextEmail, editTextSenha;
    private Button buttonLogin, buttonRegister;
    private ImageButton buttonGoogle;
    private Button btnSeed; // Botão para o Seeder

    private FirebaseAuth mAuth;
    private FirebaseFirestore db; // Instância do Firestore
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Inicializa o Firestore

        // Verifica se o usuário já está logado (ex: se o app foi fechado e reaberto)
        // Se sim, vai direto para a MainActivity
         if (mAuth.getCurrentUser() != null) {
             goToMainActivity();
            return; // Importante para não continuar o onCreate da LoginActivity
         }

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonGoogle = findViewById(R.id.buttonGoogle);
        btnSeed = findViewById(R.id.btnSeed); // Assumindo que o ID é btnSeed no XML

        // Chamar o Seeder - apenas para desenvolvimento/testes
        boolean ENABLE_SEEDER_ON_CREATE = false; // Mude para true para popular na criação da Activity (apenas uma vez)
        if (ENABLE_SEEDER_ON_CREATE) {
            DatabaseSeeder.seedDatabase(db); // Passa a instância 'db'
            Toast.makeText(this, "Seeder chamado no onCreate (desative após o uso)", Toast.LENGTH_LONG).show();
        }

        buttonLogin.setOnClickListener(v -> loginUsuario());
        buttonRegister.setOnClickListener(v -> registrarUsuario());

        btnSeed.setOnClickListener(v -> {
            DatabaseSeeder.seedDatabase(db); // Passa a instância 'db'
            Toast.makeText(LoginActivity.this, "Populando banco de dados...", Toast.LENGTH_SHORT).show();
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Certifique-se que esta string está correta
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        buttonGoogle.setOnClickListener(v -> signInWithGoogle());
    }

    private void registrarUsuario() {
        String email = String.valueOf(editTextEmail.getText()).trim();
        String senha = String.valueOf(editTextSenha.getText()).trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Insira um e-mail válido");
            editTextEmail.requestFocus();
            return;
        }
        if (senha.isEmpty() || senha.length() < 6) {
            editTextSenha.setError("A senha deve ter pelo menos 6 caracteres");
            editTextSenha.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            criarDocumentoUsuarioFirestore(firebaseUser.getUid(), firebaseUser.getEmail(), "Novo Usuário (Cadastro)", "cidadao");
                        }
                        Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        goToMainActivity();
                    } else {
                        String errorMessage = "Erro ao cadastrar.";
                        if (task.getException() != null) {
                            Log.e(TAG, "Erro no cadastro: ", task.getException());
                            errorMessage += " " + task.getException().getMessage();
                        }
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loginUsuario() {
        String email = String.valueOf(editTextEmail.getText()).trim();
        String senha = String.valueOf(editTextSenha.getText()).trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Insira um e-mail válido");
            editTextEmail.requestFocus();
            return;
        }
        if (senha.isEmpty()) {
            editTextSenha.setError("Insira a senha");
            editTextSenha.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        goToMainActivity();
                    } else {
                        String errorMessage = "Erro ao fazer login.";
                        if (task.getException() != null) {
                            Log.e(TAG, "Erro no login: ", task.getException());
                            errorMessage += " Verifique e-mail e senha."; // Mensagem mais genérica para o usuário
                        }
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN); // Este método é obsoleto, mas funcional por enquanto
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                } else {
                    Log.w(TAG, "onActivityResult: GoogleSignInAccount é nulo.");
                    Toast.makeText(this, "Falha no login com Google: conta nula.", Toast.LENGTH_LONG).show();
                }
            } catch (ApiException e) {
                Log.e(TAG, "Falha no login com Google (ApiException): ", e);
                Toast.makeText(this, "Falha no login com Google. Código: " + e.getStatusCode(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                        if (firebaseUser != null && isNewUser) {
                            // Se é um novo usuário via Google, cria o documento no Firestore
                            String nome = firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "Usuário Google";
                            criarDocumentoUsuarioFirestore(firebaseUser.getUid(), firebaseUser.getEmail(), nome, "cidadao");
                        }
                        Toast.makeText(LoginActivity.this, "Login com Google realizado!", Toast.LENGTH_SHORT).show();
                        goToMainActivity();
                    } else {
                        String errorMessage = "Erro na autenticação com Google.";
                        if (task.getException() != null) {
                            Log.e(TAG, "Erro na autenticação com Google (Firebase): ", task.getException());
                            errorMessage += " " + task.getException().getMessage();
                        }
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void criarDocumentoUsuarioFirestore(String uid, String email, String nomeCompleto, String tipoUsuario) {
        if (uid == null || email == null) {
            Log.e(TAG, "UID ou Email nulos ao tentar criar documento do usuário.");
            return;
        }
        Map<String, Object> userData = new HashMap<>();
        userData.put("nomeCompleto", nomeCompleto);
        userData.put("email", email);
        userData.put("tipoUsuario", tipoUsuario);
        userData.put("dataCadastro", com.google.firebase.Timestamp.now()); // Usa Timestamp do Firebase

        db.collection("usuarios").document(uid).set(userData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Documento do usuário criado/atualizado no Firestore para UID: " + uid))
                .addOnFailureListener(e -> Log.e(TAG, "Erro ao criar/atualizar documento do usuário no Firestore", e));
    }

    private void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
