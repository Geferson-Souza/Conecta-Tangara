package com.example.conectatangara; // Substitua pelo seu nome de pacote

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

// É importante adicionar a anotação SuppressLint para Fullscreen
// apenas se você estiver usando APIs que exigem isso e você
// tem certeza de que está lidando com as implicações de visibilidade da UI.
// Para a Splash Screen, o tema NoActionBar é geralmente preferido.
public class SplashActivity extends AppCompatActivity {

    // Tempo que a Splash Screen ficará visível (em milissegundos)
    private static final int SPLASH_TIMEOUT = 2500; // 2.5 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Opcional: Tornar a Splash Screen em tela cheia (esconder a barra de status e a barra de navegação)
        // Isso é mais comum se o seu tema não for NoActionBar.
        // Se o tema já for NoActionBar, a barra de ação já está oculta.
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //         WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        // Handler para iniciar a próxima Activity após o timeout
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Este método será executado uma vez após o SPLASH_TIMEOUT
                // Inicie sua Activity principal (ou LoginActivity) aqui
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class); // Mude para MainActivity se o login não for a primeira tela após splash
                startActivity(intent);

                // Feche esta activity
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
