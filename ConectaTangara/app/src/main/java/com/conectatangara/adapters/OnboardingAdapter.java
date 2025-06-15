package com.conectatangara.adapters; // Ou seu pacote de adapters

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.conectatangara.R;
import com.conectatangara.fragments.OnboardingFragment;

public class OnboardingAdapter extends FragmentStateAdapter {

    // Dados para cada tela (Você precisará definir estes textos e imagens)
    private static final String[] TITLES = {"Bem-vindo ao Conecta Tangará!", "Registre Ocorrências", "Acompanhe e Participe"};
    private static final String[] DESCRIPTIONS = {
            "Sua voz conectada diretamente com a gestão da cidade.",
            "Reporte problemas de infraestrutura e serviços de forma fácil e rápida.",
            "Veja o andamento das solicitações e colabore para uma Tangará melhor."
    };
    private static final int[] IMAGES = {
            R.drawable.ic_onboarding_1, // Crie estes drawables
            R.drawable.ic_onboarding_2,
            R.drawable.ic_onboarding_3
    };

    public OnboardingAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Cria um novo fragment para cada posição
        return OnboardingFragment.newInstance(IMAGES[position], TITLES[position], DESCRIPTIONS[position]);
    }

    @Override
    public int getItemCount() {
        return TITLES.length; // Retorna o número total de telas
    }
}
