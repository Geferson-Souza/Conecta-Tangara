package com.conectatangara.fragments;

import android.content.Intent;
import android.net.Uri; // Import para abrir links
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.conectatangara.R;
import com.conectatangara.activities.LoginActivity;
import com.conectatangara.activities.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    // ... (suas variáveis de instância permanecem as mesmas)
    private ImageView ivProfileAvatar;
    private TextView tvProfileUserName;
    private TextView tvProfileUserEmail;
    private Button buttonEditProfile;
    private TextView tvChangePassword;
    private TextView tvMyOccurrencesShortcut;
    private MaterialSwitch switchNotificationsGeneral;
    private MaterialSwitch switchNotificationsStatus;
    private TextView tvAboutApp;
    private TextView tvTermsOfUse;
    private TextView tvPrivacyPolicy;
    private Button buttonLogout;
    private FirebaseAuth mAuth;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivProfileAvatar = view.findViewById(R.id.iv_profile_avatar);
        tvProfileUserName = view.findViewById(R.id.tv_profile_user_name);
        tvProfileUserEmail = view.findViewById(R.id.tv_profile_user_email);
        buttonEditProfile = view.findViewById(R.id.button_edit_profile);
        tvChangePassword = view.findViewById(R.id.tv_change_password);
        tvMyOccurrencesShortcut = view.findViewById(R.id.tv_my_occurrences_shortcut);
        switchNotificationsGeneral = view.findViewById(R.id.switch_notifications_general);
        switchNotificationsStatus = view.findViewById(R.id.switch_notifications_status);
        tvAboutApp = view.findViewById(R.id.tv_about_app);
        tvTermsOfUse = view.findViewById(R.id.tv_terms_of_use);
        tvPrivacyPolicy = view.findViewById(R.id.tv_privacy_policy);
        buttonLogout = view.findViewById(R.id.button_logout);

        loadUserProfileData();
        setupClickListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.toolbar_title_profile));
        }
        loadUserProfileData();
    }

    private void loadUserProfileData() {
        // Seu código original aqui... (sem alterações)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && getContext() != null) {
            if (currentUser.getDisplayName() != null && !currentUser.getDisplayName().isEmpty()) {
                tvProfileUserName.setText(currentUser.getDisplayName());
            } else if (currentUser.getEmail() != null && !currentUser.getEmail().isEmpty()) {
                tvProfileUserName.setText(currentUser.getEmail());
            } else {
                tvProfileUserName.setText(getString(R.string.profile_user_name_placeholder));
            }
            tvProfileUserEmail.setText(currentUser.getEmail());
            if (currentUser.getPhotoUrl() != null) {
                Glide.with(getContext().getApplicationContext())
                        .load(currentUser.getPhotoUrl())
                        .placeholder(R.drawable.ic_profile_avatar_placeholder)
                        .error(R.drawable.ic_profile_avatar_placeholder)
                        .circleCrop()
                        .into(ivProfileAvatar);
            } else {
                ivProfileAvatar.setImageResource(R.drawable.ic_profile_avatar_placeholder);
            }
        }
    }

    private void setupClickListeners() {
        buttonEditProfile.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Editar Perfil - Em desenvolvimento", Toast.LENGTH_SHORT).show();
        });

        tvChangePassword.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Alterar Senha - Em desenvolvimento", Toast.LENGTH_SHORT).show();
        });

        tvMyOccurrencesShortcut.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToBottomNavItem(R.id.nav_my_occurrences);
            }
        });

        switchNotificationsGeneral.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String status = isChecked ? "ativadas" : "desativadas";
            Toast.makeText(getContext(), "Notificações gerais " + status, Toast.LENGTH_SHORT).show();
        });

        switchNotificationsStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String status = isChecked ? "ativadas" : "desativadas";
            Toast.makeText(getContext(), "Notificações de status " + status, Toast.LENGTH_SHORT).show();
        });

        // ########## INÍCIO DA MUDANÇA ##########
        tvAboutApp.setOnClickListener(v -> {
            // Mostra um diálogo simples com informações do app
            if (getContext() == null) return;
            new AlertDialog.Builder(getContext())
                    .setTitle("Sobre o Conecta Tangará")
                    .setMessage("Versão 1.0\n\nDesenvolvido para facilitar a comunicação entre cidadãos e a prefeitura.")
                    .setPositiveButton("OK", null)
                    .show();
        });

        tvTermsOfUse.setOnClickListener(v -> {
            // Mostra um Toast e, opcionalmente, abre um link
            Toast.makeText(getContext(), "Abrindo Termos de Uso...", Toast.LENGTH_SHORT).show();
            // TODO: Substituir a URL pelo link real dos seus termos de uso
            // openUrl("https://www.seusite.com/termos");
        });

        tvPrivacyPolicy.setOnClickListener(v -> {
            // Mostra um Toast e, opcionalmente, abre um link
            Toast.makeText(getContext(), "Abrindo Política de Privacidade...", Toast.LENGTH_SHORT).show();
            // TODO: Substituir a URL pelo link real da sua política de privacidade
            // openUrl("https://www.seusite.com/privacidade");
        });
        // ########## FIM DA MUDANÇA ##########

        buttonLogout.setOnClickListener(v -> showLogoutConfirmationDialog());
    }

    // Método auxiliar para abrir uma URL no navegador (opcional)
    private void openUrl(String url) {
        if (getActivity() != null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }

    private void showLogoutConfirmationDialog() {
        // Seu código original aqui... (sem alterações)
        if (getContext() == null) return;
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.profile_confirm_logout_title))
                .setMessage(getString(R.string.profile_confirm_logout_message))
                .setPositiveButton(getString(R.string.dialog_yes), (dialog, which) -> logoutUser())
                .setNegativeButton(getString(R.string.dialog_no), null)
                .setIcon(R.drawable.ic_logout)
                .show();
    }

    private void logoutUser() {
        // A lógica de logout centralizada está correta.
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).logout();
        }
    }
}
