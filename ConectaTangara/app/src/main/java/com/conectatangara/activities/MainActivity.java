package com.conectatangara.activities;

import android.content.Intent;
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
import com.conectatangara.fragments.OccurrencesMapFragment;
import com.conectatangara.fragments.ProfileFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

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
            loadFragment(new HomeFragment(), "HOME_FRAGMENT_TAG");
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(getString(R.string.app_name));
            }
        }
    }

    private final NavigationBarView.OnItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                String tag = null;
                String title = getString(R.string.app_name);

                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    selectedFragment = findOrCreateFragment("HOME_FRAGMENT_TAG", HomeFragment.class);
                    tag = "HOME_FRAGMENT_TAG";
                    title = getString(R.string.app_name);
                } else if (itemId == R.id.nav_my_occurrences) {
                    selectedFragment = findOrCreateFragment("MY_OCCURRENCES_FRAGMENT_TAG", MyOccurrencesFragment.class);
                    tag = "MY_OCCURRENCES_FRAGMENT_TAG";
                    title = getString(R.string.nav_title_my_occurrences);
                } else if (itemId == R.id.nav_indicators) {
                    selectedFragment = findOrCreateFragment("INDICATORS_FRAGMENT_TAG", IndicatorsFragment.class);
                    tag = "INDICATORS_FRAGMENT_TAG";
                    title = getString(R.string.nav_title_indicators);
                } else if (itemId == R.id.nav_public_map) {
                    selectedFragment = findOrCreateFragment("PUBLIC_MAP_FRAGMENT_TAG", OccurrencesMapFragment.class);
                    tag = "PUBLIC_MAP_FRAGMENT_TAG";
                    title = getString(R.string.nav_title_public_map);
                } else if (itemId == R.id.nav_profile) {
                    selectedFragment = findOrCreateFragment("PROFILE_FRAGMENT_TAG", ProfileFragment.class);
                    tag = "PROFILE_FRAGMENT_TAG";
                    title = getString(R.string.nav_title_profile);
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
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Navega para um item específico da BottomNavigationView.
     */
    public void navigateToBottomNavItem(int itemId) {
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(itemId);
        }
    }

    /**
     * Realiza o logout do usuário (Firebase e Google) e o redireciona para a tela de Login.
     */
    public void logout() {
        // Logout do Firebase
        FirebaseAuth.getInstance().signOut();

        // Logout do Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInClient.signOut().addOnCompleteListener(this, task -> {
            // Navega para a tela de login após o logout de ambas as contas
            navigateToLogin();
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (bottomNavigationView.getSelectedItemId() != R.id.nav_home) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        } else {
            super.onBackPressed();
        }
    }
}
