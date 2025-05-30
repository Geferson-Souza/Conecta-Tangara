package com.conectatangara;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 2500; // 2.5 segundos
    private static final String PREFS_NAME = "ConectaTangaraPrefs";
    private static final String KEY_FIRST_RUN = "isFirstRun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            // Obtém as SharedPreferences
            SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            boolean isFirstRun = preferences.getBoolean(KEY_FIRST_RUN, true);

            Intent intent;
            if (isFirstRun) {
                // Primeira vez: vai para Onboarding
                intent = new Intent(SplashActivity.this, OnboardingActivity.class);
            } else {
                // Não é a primeira vez: vai para Login (ou Main, se já logado)
                // Por enquanto, vamos direto para Login
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            startActivity(intent);
            finish(); // Fecha a SplashActivity

        }, SPLASH_TIMEOUT);
    }
}
