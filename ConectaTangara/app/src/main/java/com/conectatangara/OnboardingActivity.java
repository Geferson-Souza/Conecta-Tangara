package com.conectatangara;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Button buttonNext;
    private Button buttonSkip;
    private TabLayout tabLayout;
    private OnboardingAdapter adapter;

    private static final String PREFS_NAME = "ConectaTangaraPrefs";
    private static final String KEY_FIRST_RUN = "isFirstRun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.view_pager_onboarding);
        buttonNext = findViewById(R.id.button_next);
        buttonSkip = findViewById(R.id.button_skip);
        tabLayout = findViewById(R.id.tab_layout_indicator);

        adapter = new OnboardingAdapter(this);
        viewPager.setAdapter(adapter);

        // Vincula o TabLayout ao ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Não precisamos de texto ou ícones aqui, apenas os pontos
        }).attach();

        // Listener para o botão Pular
        buttonSkip.setOnClickListener(v -> finishOnboarding());

        // Listener para o botão Avançar/Concluir
        buttonNext.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem < adapter.getItemCount() - 1) {
                // Se não for a última tela, avança
                viewPager.setCurrentItem(currentItem + 1);
            } else {
                // Se for a última tela, finaliza o onboarding
                finishOnboarding();
            }
        });

        // Atualiza o texto do botão Avançar na última tela
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == adapter.getItemCount() - 1) {
                    buttonNext.setText("Concluir");
                    buttonSkip.setVisibility(View.INVISIBLE); // Opcional: esconder Pular na última
                } else {
                    buttonNext.setText("Avançar");
                    buttonSkip.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void finishOnboarding() {
        // Salva que o onboarding foi concluído
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_FIRST_RUN, false);
        editor.apply();

        // Navega para a LoginActivity
        Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Fecha a OnboardingActivity
    }
}

