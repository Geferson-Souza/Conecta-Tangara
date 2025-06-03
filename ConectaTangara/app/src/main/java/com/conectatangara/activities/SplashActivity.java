package com.conectatangara.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.conectatangara.R; // Certifique-se que este import está correto

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 2500; // 2.5 segundos
    private static final String PREFS_NAME = "ConectaTangaraPrefs";
    private static final String KEY_FIRST_RUN = "isFirstRun"; // Chave para verificar o primeiro acesso

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // O tema NoActionBar para a SplashActivity geralmente é definido no AndroidManifest.xml
        // Ex: android:theme="@style/Theme.ConectaTangara.FullScreen" ou Theme.ConectaTangara.NoActionBar
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            boolean isFirstRun = preferences.getBoolean(KEY_FIRST_RUN, true);

            Intent intent;
            if (isFirstRun) {
                // Primeira vez: vai para OnboardingActivity
                intent = new Intent(SplashActivity.this, OnboardingActivity.class);
            } else {
                // Não é a primeira vez: verificar se está logado
                // TODO: Implementar verificação de usuário logado no Firebase Auth
                // FirebaseAuth auth = FirebaseAuth.getInstance();
                // if (auth.getCurrentUser() != null) {
                //    intent = new Intent(SplashActivity.this, MainActivity.class);
                // } else {
                //    intent = new Intent(SplashActivity.this, LoginActivity.class);
                // }
                // Por enquanto, vamos direto para Login se não for o primeiro acesso
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            startActivity(intent);
            finish(); // Fecha a SplashActivity para que o usuário não possa voltar para ela
        }, SPLASH_TIMEOUT);
    }
}
