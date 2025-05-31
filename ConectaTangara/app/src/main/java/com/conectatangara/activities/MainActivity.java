package com.conectatangara.activities;

import android.os.Bundle;
import android.view.MenuItem; // Necessário para o listener da BottomNavigationView

import androidx.annotation.NonNull; // Necessário para o listener da BottomNavigationView
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.conectatangara.R;
import com.conectatangara.fragments.HomeFragment;
import com.conectatangara.fragments.IndicatorsFragment;
import com.conectatangara.fragments.MyOccurrencesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView; // Import correto para o listener

public class MainActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Usa o layout do artefato activity_main_xml_with_toolbar_nav

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar); // Define esta Toolbar como a ActionBar da Activity

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        // Carrega o fragmento inicial (HomeFragment) se não houver um estado salvo
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), "HOME_FRAGMENT");
            bottomNavigationView.setSelectedItemId(R.id.nav_home); // Garante que o item correto esteja selecionado
            if (getSupportActionBar() != null) { // Define o título inicial
                getSupportActionBar().setTitle("Início"); // Ou R.string.title_home
            }
        }
    }

    private final NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    String tag = null;
                    String title = getString(R.string.app_name); // Título padrão

                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_home) {
                        selectedFragment = findOrCreateFragment("HOME_FRAGMENT", HomeFragment.class);
                        tag = "HOME_FRAGMENT";
                        title = "Início"; // Ou R.string.title_home
                    } else if (itemId == R.id.nav_my_occurrences) {
                        selectedFragment = findOrCreateFragment("MY_OCCURRENCES_FRAGMENT", MyOccurrencesFragment.class);
                        tag = "MY_OCCURRENCES_FRAGMENT";
                        title = getString(R.string.toolbar_title_my_occurrences);
                    } else if (itemId == R.id.nav_indicators) {
                        selectedFragment = findOrCreateFragment("INDICATORS_FRAGMENT", IndicatorsFragment.class);
                        tag = "INDICATORS_FRAGMENT";
                        title = "Indicadores"; // Ou R.string.title_indicators
                    }

                    if (selectedFragment != null) {
                        loadFragment(selectedFragment, tag);
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setTitle(title);
                        }
                        return true;
                    }
                    return false;
                }
            };

    // Método para encontrar um fragmento existente pela tag ou criar um novo se não existir
    private <T extends Fragment> T findOrCreateFragment(String tag, Class<T> fragmentClass) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) {
            try {
                fragment = fragmentClass.newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                // Em Java, newInstance() pode lançar estas exceções.
                // Para Kotlin, seria fragmentClass.java.newInstance()
                // Ou melhor ainda, usar FragmentFactory se a complexidade aumentar.
                e.printStackTrace(); // Trate o erro apropriadamente
                return null;
            }
        }
        return (T) fragment;
    }


    private void loadFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            // Opcional: Animações de transição
            // transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

            transaction.replace(R.id.main_fragment_container, fragment, tag);
            // Não adicionamos à pilha de retorno para fragmentos de nível superior da BottomNav
            // para evitar que o botão "voltar" navegue entre eles.
            // O comportamento padrão do botão voltar fechará o app se estiver no primeiro fragmento.
            transaction.commit();
        }
    }

    // Este método pode ser chamado por fragmentos para atualizar o título da Toolbar,
    // embora agora o listener da BottomNav já faça isso.
    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    // Lidar com o botão "up" da Toolbar (se habilitado em algum fragmento)
    // @Override
    // public boolean onOptionsItemSelected(MenuItem item) {
    //     if (item.getItemId() == android.R.id.home) {
    //         // Se você usar NavController, ele lida com isso.
    //         // Se usar addToBackStack, pode fazer:
    //         // getSupportFragmentManager().popBackStack();
    //         // return true;
    //     }
    //     return super.onOptionsItemSelected(item);
    // }

    // Lidar com o botão "voltar" do sistema para navegar entre os itens da BottomNav
    // ou fechar o app se estiver na tela inicial.
    @Override
    public void onBackPressed() {
        if (bottomNavigationView.getSelectedItemId() == R.id.nav_home) {
            super.onBackPressed(); // Permite fechar o app se estiver na Home
        } else {
            bottomNavigationView.setSelectedItemId(R.id.nav_home); // Volta para a Home
        }
    }
}
