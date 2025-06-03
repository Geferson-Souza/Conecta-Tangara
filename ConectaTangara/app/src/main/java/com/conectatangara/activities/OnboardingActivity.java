package com.conectatangara.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.conectatangara.R; // Certifique-se que o import está correto
import com.conectatangara.adapters.OnboardingAdapter; // Certifique-se que o import está correto
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Button buttonNext;
    private Button buttonSkip;
    private TabLayout tabLayout;
    private OnboardingAdapter adapter;

    private static final String PREFS_NAME = "ConectaTangaraPrefs"; // Deve ser a mesma constante da SplashActivity
    private static final String KEY_FIRST_RUN = "isFirstRun"; // Deve ser a mesma constante da SplashActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // O tema NoActionBar para a OnboardingActivity geralmente é definido no AndroidManifest.xml
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.view_pager_onboarding);
        buttonNext = findViewById(R.id.button_next);
        buttonSkip = findViewById(R.id.button_skip);
        tabLayout = findViewById(R.id.tab_layout_indicator);

        adapter = new OnboardingAdapter(this); // Passa a Activity (que é um FragmentActivity)
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Para os pontos do indicador, não precisamos de texto ou ícone no tab
        }).attach();

        buttonSkip.setOnClickListener(v -> finishOnboarding());

        buttonNext.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem < adapter.getItemCount() - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            } else {
                finishOnboarding();
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == adapter.getItemCount() - 1) {
                    buttonNext.setText(getString(R.string.onboarding_button_finish)); // Usar string resource
                    buttonSkip.setVisibility(View.GONE); // Esconder "Pular" na última tela
                } else {
                    buttonNext.setText(getString(R.string.onboarding_button_next)); // Usar string resource
                    buttonSkip.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void finishOnboarding() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_FIRST_RUN, false); // Marca que o onboarding foi concluído
        editor.apply();

        Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Fecha a OnboardingActivity
    }
}
