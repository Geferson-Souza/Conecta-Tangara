package com.conectatangara.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.conectatangara.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.conectatangara.utils.DatabaseSeeder;
import com.google.firebase.firestore.FirebaseFirestore; // << ADICIONE ESTE IMPORT

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextSenha;
    private Button buttonLogin, buttonRegister;
    private ImageButton buttonGoogle;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db; // << ADICIONE ESTA DECLARAÇÃO

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // << INICIALIZE O FIRESTORE

        // Chamar o Seeder
        boolean ENABLE_SEEDER = false; // Mude para true para popular, depois volte para false
        if (ENABLE_SEEDER) {
            DatabaseSeeder.seedDatabase(db); // << PASSE A INSTÂNCIA 'db'
        }

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonGoogle = findViewById(R.id.buttonGoogle);

        buttonLogin.setOnClickListener(v -> loginUsuario());
        buttonRegister.setOnClickListener(v -> registrarUsuario());

        Button btnSeed = findViewById(R.id.btnSeed);
        btnSeed.setOnClickListener(v -> {
            DatabaseSeeder.seedDatabase(db); // << PASSE A INSTÂNCIA 'db'
            Toast.makeText(LoginActivity.this, "Tentativa de popular o banco...", Toast.LENGTH_SHORT).show();
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        buttonGoogle.setOnClickListener(v -> signInWithGoogle());
    }

    private void registrarUsuario() {
        String email = String.valueOf(editTextEmail.getText()).trim(); // Mais seguro
        String senha = String.valueOf(editTextSenha.getText()).trim(); // Mais seguro

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha o e-mail e a senha", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        // TODO: Após o cadastro, você pode querer criar um documento para este usuário na coleção 'usuarios' do Firestore
                        // Ex: String userId = mAuth.getCurrentUser().getUid();
                        //     Map<String, Object> userData = new HashMap<>();
                        //     userData.put("nomeCompleto", "Novo Usuário"); // Pegar de campos de nome, se houver
                        //     userData.put("email", email);
                        //     userData.put("tipoUsuario", "cidadao");
                        //     userData.put("dataCadastro", com.google.firebase.Timestamp.now());
                        //     db.collection("usuarios").document(userId).set(userData);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMessage = "Erro ao cadastrar.";
                        if (task.getException() != null) {
                            errorMessage += " " + task.getException().getMessage();
                        }
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loginUsuario() {
        String email = String.valueOf(editTextEmail.getText()).trim(); // Mais seguro
        String senha = String.valueOf(editTextSenha.getText()).trim(); // Mais seguro

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha o e-mail e a senha", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMessage = "Erro ao fazer login.";
                        if (task.getException() != null) {
                            errorMessage += " " + task.getException().getMessage();
                        }
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) { // Verificação de nulidade
                    firebaseAuthWithGoogle(account);
                } else {
                    Toast.makeText(this, "Falha no login com Google: conta nula.", Toast.LENGTH_LONG).show();
                }
            } catch (ApiException e) {
                Toast.makeText(this, "Falha no login com Google: " + e.getMessage() + " (Code: " + e.getStatusCode() + ")", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login com Google realizado!", Toast.LENGTH_SHORT).show();
                        // TODO: Verificar se é a primeira vez do usuário com este login Google
                        // e criar um documento na coleção 'usuarios' do Firestore se necessário.
                        // FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        // if (firebaseUser != null && task.getResult().getAdditionalUserInfo().isNewUser()) {
                        //    Map<String, Object> userData = new HashMap<>();
                        //    userData.put("nomeCompleto", firebaseUser.getDisplayName());
                        //    userData.put("email", firebaseUser.getEmail());
                        //    userData.put("tipoUsuario", "cidadao");
                        //    userData.put("dataCadastro", com.google.firebase.Timestamp.now());
                        //    // userData.put("fotoUrl", firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null);
                        //    db.collection("usuarios").document(firebaseUser.getUid()).set(userData);
                        // }

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMessage = "Erro na autenticação com Google.";
                        if (task.getException() != null) {
                            errorMessage += " " + task.getException().getMessage();
                        }
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
