package com.conectatangara.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.conectatangara.R;
import com.conectatangara.fragments.HomeFragment;
import com.conectatangara.fragments.IndicatorsFragment;
import com.conectatangara.fragments.MyOccurrencesFragment;
import com.conectatangara.fragments.ProfileFragment; // IMPORTAR O PROFILE FRAGMENT
import com.conectatangara.fragments.OccurrencesMapFragment; // Se você tiver o item de menu para ele
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), "HOME_FRAGMENT_TAG"); // Usando tags diferentes para cada
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(getString(R.string.nav_title_home));
            }
        }
    }

    private final NavigationBarView.OnItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                String tag = null;
                String title = getString(R.string.app_name); // Título padrão

                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    selectedFragment = findOrCreateFragment("HOME_FRAGMENT_TAG", HomeFragment.class);
                    tag = "HOME_FRAGMENT_TAG";
                    title = getString(R.string.nav_title_home);
                } else if (itemId == R.id.nav_my_occurrences) {
                    selectedFragment = findOrCreateFragment("MY_OCCURRENCES_FRAGMENT_TAG", MyOccurrencesFragment.class);
                    tag = "MY_OCCURRENCES_FRAGMENT_TAG";
                    title = getString(R.string.nav_title_my_occurrences);
                } else if (itemId == R.id.nav_indicators) {
                    selectedFragment = findOrCreateFragment("INDICATORS_FRAGMENT_TAG", IndicatorsFragment.class);
                    tag = "INDICATORS_FRAGMENT_TAG";
                    title = getString(R.string.nav_title_indicators);
                } else if (itemId == R.id.nav_public_map) { // Assumindo que você tem este ID no menu
                    selectedFragment = findOrCreateFragment("PUBLIC_MAP_FRAGMENT_TAG", OccurrencesMapFragment.class);
                    tag = "PUBLIC_MAP_FRAGMENT_TAG";
                    title = getString(R.string.nav_title_public_map); // Certifique-se que esta string existe
                } else if (itemId == R.id.nav_profile) { // CASO PARA PERFIL
                    selectedFragment = findOrCreateFragment("PROFILE_FRAGMENT_TAG", ProfileFragment.class);
                    tag = "PROFILE_FRAGMENT_TAG";
                    title = getString(R.string.nav_title_profile); // Certifique-se que esta string existe
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment, tag);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(title);
                    }
                    return true;
                }
                return false;
            };

    private <T extends Fragment> T findOrCreateFragment(String tag, Class<T> fragmentClass) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) {
            try {
                fragment = fragmentClass.newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                return null;
            }
        }
        return (T) fragment;
    }

    private void loadFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container, fragment, tag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE) // Opcional: animação suave
                    .commit();
        }
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        if (bottomNavigationView.getSelectedItemId() == R.id.nav_home) {
            super.onBackPressed();
        } else {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }
}
