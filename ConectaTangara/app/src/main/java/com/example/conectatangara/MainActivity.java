package com.example.conectatangara;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton; // << Adicionado este import
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// A importação de R.java não é necessária na maioria dos casos no Android Studio
// import com.example.conectatangara.R;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.example.conectatangara.utils.DatabaseSeeder;

public class MainActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextSenha;
    private Button buttonLogin, buttonRegister;
    private ImageButton buttonGoogle; // << Corrigido: Agora é um ImageButton
    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001; // Código de requisição para login com Google

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Chamar o Seeder - apenas enquanto estiver em desenvolvimento/testes
        // Certifique-se de que DatabaseSeeder.seedDatabase() não execute em produção
        boolean ENABLE_SEEDER = false; // <- Mude para false para desativar

        if (ENABLE_SEEDER) {
            DatabaseSeeder.seedDatabase();
        }   // use código acima somente em produção - se quiser popular o DataBase com o arquivo DatabaseSeeder.java

        mAuth = FirebaseAuth.getInstance();

        // Vincula os campos
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonGoogle = findViewById(R.id.buttonGoogle); // << Corrigido: Usando a variável buttonGoogle

        // Botão de login (e-mail/senha)
        buttonLogin.setOnClickListener(v -> loginUsuario());

        // Botão de registro
        buttonRegister.setOnClickListener(v -> registrarUsuario());

        //Botão temporario para o DatabaseSeeder
        Button btnSeed = findViewById(R.id.btnSeed);
        btnSeed.setOnClickListener(v -> DatabaseSeeder.seedDatabase());


        // Configuração do Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Verifique se esse valor está no strings.xml
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Botão de login com Google
        buttonGoogle.setOnClickListener(v -> signInWithGoogle());
    }

    private void registrarUsuario() {
        String email = editTextEmail.getText().toString().trim();
        String senha = editTextSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha o e-mail e a senha", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // FirebaseUser user = mAuth.getCurrentUser(); // Não é estritamente necessário se não for usar a info do user aqui
                        Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        // TODO: Redirecionar usuário para a tela principal ou de perfil
                    } else {
                        // Task.getException() pode ser nulo ou retornar diferentes tipos de exceções
                        String errorMessage = "Erro ao cadastrar.";
                        if (task.getException() != null) {
                            errorMessage += " " + task.getException().getMessage();
                        }
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loginUsuario() {
        String email = editTextEmail.getText().toString().trim();
        String senha = editTextSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha o e-mail e a senha", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // FirebaseUser user = mAuth.getCurrentUser(); // Não é estritamente necessário se não for usar a info do user aqui
                        Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        // TODO: Redirecionar usuário para a tela principal
                    } else {
                        // Task.getException() pode ser nulo ou retornar diferentes tipos de exceções
                        String errorMessage = "Erro ao fazer login.";
                        if (task.getException() != null) {
                            errorMessage += " " + task.getException().getMessage();
                        }
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    // --- LOGIN COM GOOGLE ---

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
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Melhorar o tratamento de erro em produção
                Toast.makeText(this, "Falha no login com Google: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // FirebaseUser user = mAuth.getCurrentUser(); // Não é estritamente necessário
                        Toast.makeText(MainActivity.this, "Login com Google realizado!", Toast.LENGTH_SHORT).show();
                        // TODO: Redirecionar usuário para a tela principal
                    } else {
                        // Task.getException() pode ser nulo ou retornar diferentes tipos de exceções
                        String errorMessage = "Erro na autenticação com Google.";
                        if (task.getException() != null) {
                            errorMessage += " " + task.getException().getMessage();
                        }
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}